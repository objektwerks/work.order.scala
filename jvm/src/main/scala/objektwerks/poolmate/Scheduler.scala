package objektwerks.poolmate

import java.util.concurrent._

class Scheduler(emailer: Emailer):
  val executor = new ScheduledThreadPoolExecutor(1)
  val task = emailer.receiveEmail()
  val scheduler = executor.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS)
  scheduler.cancel(false)