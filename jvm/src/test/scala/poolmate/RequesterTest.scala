package poolmate

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.sys.process.Process

import Serializer.given

import upickle.default.*

class RequesterTest extends AnyFunSuite with Matchers with LazyLogging:
  Process("psql -d poolmate -f ddl.sql").run().exitValue()

  val conf = ConfigFactory.load("test.server.conf")
  val host = conf.getString("host")
  val port = conf.getString("port")
  val url = s"http://$host:$port/command"
  val server = EmbeddedServer(conf)

  test("requester") {
    server.start()

    val join = Join(emailAddress = conf.getString("email.to"))
    logger.info(s"*** Join: $join")

    val joinJson = write[Join](join)
    logger.info(s"*** Join json: $joinJson")

    val joinResponse = requests.post(url, data = joinJson)
    logger.info(s"*** Join response: $joinResponse")
    
    val joined = read[Joined](joinResponse.text())
    logger.info(s"*** Joined: $joined")

    val enter = Enter(joined.account.emailAddress, joined.account.pin)
    logger.info(s"*** Enter: $enter")

    val enterJson = write[Enter](enter)
    logger.info(s"*** Enter json: $enterJson")

    val enterResponse = requests.post(url, data = enterJson)
    logger.info(s"*** Enter response: $enterResponse")

    val entered = read[Entered](enterResponse.text())
    logger.info(s"*** Entered: $entered")

    require(joined.account == entered.account, "Joined account not equal to Entered account.")

    server.stop()
  }