package poolmate

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import javax.mail.Flags
import jodd.mail.{Email, ImapServer, MailServer, SmtpServer}
import jodd.mail.EmailFilter._

import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success, Using, Try}

final class Emailer(conf: Config) extends LazyLogging:
  private val host = conf.getString("email.host")
  private val port = conf.getInt("email.port")
  private val sender = conf.getString("email.sender")
  private val password = conf.getString("email.password")

  private val smtpServer: SmtpServer = MailServer.create()
    .host(host)
    .port(port)
    .ssl(true)
    .auth(sender, password)
    .buildSmtpMailServer()

  @tailrec
  private def retry[T](attempts: Int)(fn: => T): T =
    Try { fn } match {
      case Success(result) => result
      case _ if attempts >= 1 => retry(attempts - 1)(fn)
      case Failure(error) => throw error
    }

  private def sendEmail(recipients: (String, String), subject: String, message: String): Either[Throwable, String] =
    Using( smtpServer.createSession ) { session =>
      session.open()
      val email = Email.create()
        .from(sender)
        .to(recipients._1)
        .to(recipients._2)
        .subject(subject)
        .htmlMessage(message, "UTF-8")
      val messageId = session.sendMail(email)
      logger.info("*** Emailer sent message id: {}", messageId)
      messageId
    }.toEither

  // recipients: string, subject: string, html: string
  def send(recipients: (String, String), subject: String, message: String): Either[Throwable, String] =
    retry[Either[Throwable, String]](1)(sendEmail(recipients, subject, message))