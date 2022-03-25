package poolmate

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import Serializers.given

import upickle.default.*

object Requester extends LazyLogging:
  def main(args: Array[String]): Unit =
    val conf = ConfigFactory.load("server.conf")
    val host = conf.getString("host")
    val port = conf.getString("host")
    val url = s"http://$host:$port/command"
    
    logger.info(s"*** Server url: $url")

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