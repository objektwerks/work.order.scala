package objektwerks.poolmate

import castor.{Context, SimpleActor}
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import javax.mail.Flags

import jodd.mail.{Email, ImapServer, MailServer, SmtpServer}
import jodd.mail.EmailFilter._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success, Using}

final class EmailSender(conf: Config, store: Store)
                       (using context: Context) extends SimpleActor[Register] with LazyLogging:
  val host = conf.getString("email.host")
  val to = conf.getString("email.to")
  val password = conf.getString("email.password")
  val from = conf.getString("email.from")
  val subject = conf.getString("email.subject")
  val message = conf.getString("email.message")
  val email = conf.getString("email.email")
  val lic = conf.getString("email.lic")
  val pin = conf.getString("email.pin")
  val instructions = conf.getString("email.instructions")

  private val smtpServer: SmtpServer = MailServer.create()
    .ssl(true)
    .host(host)
    .auth(to, password)
    .buildSmtpMailServer()

  private val imapServer: ImapServer = MailServer.create()
    .ssl(true)
    .host(host)
    .auth(to, password)
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

  def sendEmail(register: Register): Registering =        
    Using( smtpServer.createSession ) { session =>
      session.open()
      if session.isConnected then
        val account = Account(emailAddress = register.emailAddress)

        val messageId = session.sendMail( buildEmail(account) )
        logger.info("*** Emailer sent message id: {}", messageId)

        store.addAccount(account)
        logger.info("*** Emailer added account: {}", account)

        val email = objektwerks.poolmate.Email(messageId, account.license, account.emailAddress)
        store.addEmail(email)
        logger.info("*** Emailer added email: {}", email)
      else logger.error("*** Emailer smtp server session is NOT connected!")
      Registering()
    }.get

  def receiveEmail(): Runnable =
    new Runnable() {
      override def run(): Unit =
        Using( imapServer.createSession ) { session =>
          session.open()
          if session.isConnected then
            val messages = session.receiveEmailAndMarkSeen( filter.flag(Flags.Flag.SEEN, false) )
            logger.info("*** Emailer received email and mark-seen messages: {}", messages.size)
            store.listEmails.foreach { email =>
              messages.foreach { message =>
                logger.info("*** Emailer subject {}", message.subject())
                logger.info("*** Emailer message id: {}, email id: {}", message.messageId, email.id)
                
                if message.subject != subject && message.messageId() == email.id then
                  store.processedEmail( email.copy(processed = true) )
                  logger.warn("*** Emailer [invalid] processed email: {}", email.id)
                  store.removeAccount(email.license)
                  logger.warn("*** Emailer removed account: {}", email.license)

                else if message.messageId() == email.id then
                  store.processedEmail( email.copy(processed = true, valid = true) )
                  logger.info("*** Emailer email processed and valid: {}", email)

                else logger.error("*** Emailer invalid message: {}", message.messageId())
              }
            }
          else logger.error("*** Emailer imap server session is NOT connected!")
        }.get
    }

  def run(register: Register) = sendEmail(register)