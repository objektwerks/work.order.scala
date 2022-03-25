package poolmate

import cask.main.Main

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration._
import scala.sys.process.Process

import io.undertow.Undertow
import io.undertow.server.handlers.BlockingHandler

import Serializers.given

import upickle.default.*

final class EmbeddedServer(conf: Config) extends Main with LazyLogging:
  val store = Store(conf, Store.cache(minSize = 4, maxSize = 10, expireAfter = 24.hour))
  val service = Service(store)
  val authorizer = Authorizer(service)
  val validator = Validator()
  val dispatcher = Dispatcher(authorizer, validator, service)
  val router = Router(dispatcher, store)

  override val allRoutes = Seq(router)
  
  override def host: String = conf.getString("host")

  override def port: Int = conf.getInt("port")

  override def defaultHandler: BlockingHandler =
    new BlockingHandler( CorsHandler(dispatchTrie,
                                     mainDecorators,
                                     debugMode = false,
                                     handleNotFound,
                                     handleMethodNotAllowed,
                                     handleEndpointError) )

  Main.silenceJboss()    
  val server = Undertow.builder
    .addHttpListener(port, host)
    .setHandler(defaultHandler)
    .build

  def start(): Unit =
    server.start()
    logger.info(s"*** EmbeddedServer started at http://$host:$port/")

  def stop(): Unit =
    server.stop()
    logger.info("*** EmbeddedServer stopped.")

class RequesterTest extends AnyFunSuite with Matchers with LazyLogging:
  Process("psql -d poolmate -f ddl.sql").run().exitValue()

  val conf = ConfigFactory.load("test.server.conf")
  val host = conf.getString("host")
  val port = conf.getString("host")
  val url = s"http://$host:$port/command"
  val server = EmbeddedServer(conf)

  test("requester") {
    server.start()

    val join = Join()
    logger.info(s"*** join: $join")

    val joinJson = write[Join](join)
    logger.info(s"*** Join json: $joinJson")

    val joinResponse = requests.post(url, data = joinJson)
    logger.info(s"*** Join response: $joinResponse")
    
    val joined = read[Joined](joinResponse.text())
    logger.info(s"*** Joined: $joined")

    val enter = Enter(joined.account.pin)
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