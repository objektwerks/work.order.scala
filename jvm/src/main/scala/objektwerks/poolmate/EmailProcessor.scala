package objektwerks.poolmate

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import javax.mail.Flags

import jodd.mail.{Email, ImapServer, MailServer, SmtpServer}
import jodd.mail.EmailFilter._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success, Using}

class EmailProcessor(conf: Config, store: Store) extends LazyLogging:
  private val host = conf.getString("email.host")
  private val to = conf.getString("email.to")
  private val password = conf.getString("email.password")
  private val subject = conf.getString("email.subject")

  private val imapServer: ImapServer = MailServer.create()
    .ssl(true)
    .host(host)
    .auth(to, password)
    .buildImapMailServer()

  def process(): Runnable =
    new Runnable() {
      override def run(): Unit =
        Using( imapServer.createSession ) { session =>
          session.open()
          if session.isConnected then
            val messages = session.receiveEmailAndMarkSeen( filter.flag(Flags.Flag.SEEN, false) )
            logger.info("*** Emailer processed email and mark-seen messages: {}", messages.size)
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