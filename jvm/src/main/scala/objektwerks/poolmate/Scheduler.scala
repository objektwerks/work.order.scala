package objektwerks.poolmate

import java.util.concurrent._

object Scheduler:
  val initialDelay = 3L
  val period = 30L

class Scheduler(emailer: Emailer):
  import Scheduler.*
  
  val executor = new ScheduledThreadPoolExecutor(1)
  val task = emailer.receiveEmail()
  val scheduler = executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS)
  scheduler.cancel(false)