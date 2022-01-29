package objektwerks.poolmate

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import javax.mail.Flags

import jodd.mail.{Email, ImapServer, MailServer, SmtpServer}
import jodd.mail.EmailFilter._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success, Using}

final class Emailer(conf: Config,
                    store: Store) extends LazyLogging:
  private val host = conf.getString("email.host")
  private val user = conf.getString("email.user")
  private val password = conf.getString("email.password")
  private val from = conf.getString("email.from")
  private val subject = conf.getString("email.subject")
  private val message = conf.getString("email.message")
  private val email = conf.getString("email.email")
  private val lic = conf.getString("email.lic")
  private val pin = conf.getString("email.pin")
  private val instructions = conf.getString("email.instructions")

  private val smtpServer: SmtpServer = MailServer.create()
    .ssl(true)
    .host(host)
    .auth(user, password)
    .buildSmtpMailServer()

  private val imapServer: ImapServer = MailServer.create()
    .ssl(true)
    .host(host)
    .auth(user, password)
    .buildImapMailServer()

  private def buildEmail(account: Account): Email = {
    val html = s"""
                  |<!DOCTYPE html>
                  |<html lang="en">
                  |<head>
                  |<meta charset="utf-8">
                  |<title>$subject</title>
                  |</head>
                  |<body>
                  |<p>$message</p>
                  |<p>$lic ${account.license}</p>
                  |<p>$email ${account.emailAddress}</p>
                  |<p>$pin ${account.pin}</p>
                  |<p>$instructions</p>
                  |</body>
                  |</html>
                  |""".stripMargin
    Email.create()
      .from(from)
      .to(account.emailAddress)
      .subject(subject)
      .htmlMessage(html, "UTF-8")
  }

  def sendEmail(register: Register): Unit =
    Using( smtpServer.createSession ) { session =>
      session.open()
      if session.isConnected then
        val account = Account(emailAddress = register.emailAddress)
        val messageId = session.sendMail( buildEmail(account) )
        val email = objektwerks.poolmate.Email(messageId, account.license, account.emailAddress)
        store.addEmail(email)
        logger.info("*** Emailer sent email: {}", email)
        store.addAccount(account)
        logger.info("*** Emailer added account: {}", account)
      else logger.error("*** Emailer smtp server session is NOT connected!")
      ()
    }.get

  def receiveEmail(): Runnable = new Runnable() {
    override def run(): Unit =
      Using( imapServer.createSession ) { session =>
        session.open()
        if session.isConnected then
          val messages = session.receiveEmailAndMarkSeen( filter.flag(Flags.Flag.SEEN, false) )
          logger.info("*** Emailer receiveEmailAndMarkSeen messages: {}", messages.size)
          store.listEmails.foreach { email =>
            messages.foreach { message =>
              if message.messageId() == email.id then
                store.updateEmail( email.copy(processed = true, valid = true) )
                logger.info("*** Emailer email processed and valid: {}", email)
            }
          }
        }.get
      }