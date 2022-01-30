package objektwerks.poolmate

import java.util.concurrent._

object Scheduler:
  val initialDelay = 9L
  val period = 30L

final class Scheduler(emailProcessor: EmailProcessor):
  import Scheduler.*
  
  val executor = new ScheduledThreadPoolExecutor(1)
  val task = emailProcessor.process()
  val scheduler = executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS)
  scheduler.cancel(false)