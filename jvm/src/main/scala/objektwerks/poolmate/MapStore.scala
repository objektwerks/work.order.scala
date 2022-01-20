package objektwerks.service

import objektwerks.datetime.DateTime
import objektwerks.entity.*

import scala.collection.mutable

class MapStore extends Store:
  private val accounts = mutable.Map.empty[String, Account]
  private val pools = mutable.Map.empty[Int, Pool]
  private val surfaces = mutable.Map.empty[Int, Surface]
  private val pumps = mutable.Map.empty[Int, Pump]
  private val timers = mutable.Map.empty[Int, Timer]
  private val timerSettings = mutable.Map.empty[Int, TimerSetting]
  private val heaters = mutable.Map.empty[Int, Heater]
  private val heaterSettings = mutable.Map.empty[Int, HeaterSetting]
  private val measurements = mutable.Map.empty[Int, Measurement]
  private val cleanings = mutable.Map.empty[Int, Cleaning]
  private val chemicals = mutable.Map.empty[Int, Chemical]
  private val supplies = mutable.Map.empty[Int, Supply]
  private val repairs = mutable.Map.empty[Int, Repair]
  private val emails = mutable.Map.empty[String, Email]
  private val faults = mutable.Map.empty[(Int, Int, Int), Fault]

  def register(emailAddress: String): Option[Account] =
    val account = Account(emailAddress = emailAddress)
    val email = Email(id = "1", license = account.license, address = emailAddress, message = "message")
    if Emailer.send(email) then
      accounts.addOne(account.license, account)
      addEmail(email)
      Some(account)
    else None

  def login(email: String, pin: String): Option[Account] =
    accounts.values.find(account => account.emailAddress == email && account.pin == pin)

  def isAuthorized(license: String): Boolean =
    accounts
      .get(license)
      .filter(account => account.deactivated == 0 && account.activated > 0)
      .fold(false)(_ => true)

  def deactivate(license: String): Option[Account] =
    val account = accounts.get(license)
    if account.nonEmpty then
      Some( account.get.copy(deactivated = DateTime.currentDate, activated = 0) )
    else None

  def reactivate(license: String): Option[Account] =
    val account = accounts.get(license)
    if account.nonEmpty then
      Some( account.get.copy(activated = DateTime.currentDate, deactivated = 0) )
    else None

  def listPools(): Seq[Pool] = pools.values.to(Seq)

  def addPool(pool: Pool): Pool =
    val newPool = pool.copy(id = pools.size + 1)
    pools.addOne(newPool.id, newPool)
    newPool

  def updatePool(pool: Pool): Unit = pools.update(pool.id, pool)

  def listSurfaces(): Seq[Surface] = surfaces.values.to(Seq)

  def addSurface(surface: Surface): Surface =
    val newSurface = surface.copy(id = surfaces.size + 1)
    surfaces.addOne(newSurface.id, newSurface)
    newSurface

  def updateSurface(surface: Surface): Unit = surfaces.update(surface.id, surface)

  def listPumps(): Seq[Pump] = pumps.values.to(Seq)

  def addPump(pump: Pump): Pump =
    val newPump = pump.copy(id = pumps.size + 1)
    pumps.addOne(newPump.id, newPump)
    newPump

  def updatePump(pump: Pump): Unit = pumps.update(pump.id, pump)

  def listTimers(): Seq[Timer] = timers.values.to(Seq)

  def addTimer(timer: Timer): Timer =
    val newTimer = timer.copy(id = timers.size + 1)
    timers.addOne(newTimer.id, newTimer)
    newTimer

  def updateTimer(timer: Timer): Unit = timers.update(timer.id, timer)

  def listTimerSettings(): Seq[TimerSetting] = timerSettings.values.to(Seq)

  def addTimerSetting(timerSetting: TimerSetting): TimerSetting =
    val newTimerSetting = timerSetting.copy(id = timerSettings.size + 1)
    timerSettings.addOne(newTimerSetting.id, newTimerSetting)
    newTimerSetting

  def updateTimerSetting(timerSetting: TimerSetting): Unit = timerSettings.update(timerSetting.id, timerSetting)

  def listHeaters(): Seq[Heater] = heaters.values.to(Seq)

  def addHeater(heater: Heater): Heater =
    val newHeater = heater.copy(id = heaters.size + 1)
    heaters.addOne(newHeater.id, newHeater)
    newHeater

  def updateHeater(heater: Heater): Unit = heaters.update(heater.id, heater)

  def listHeaterSettings(): Seq[HeaterSetting] = heaterSettings.values.to(Seq)

  def addHeaterSetting(heaterSetting: HeaterSetting): HeaterSetting =
    val newHeaterSetting = heaterSetting.copy(id = heaterSettings.size + 1)
    heaterSettings.addOne(newHeaterSetting.id, newHeaterSetting)
    newHeaterSetting

  def updateHeaterSetting(heaterSetting: HeaterSetting): Unit = heaterSettings.update(heaterSetting.id, heaterSetting)

  def listMeasurements(): Seq[Measurement] = measurements.values.to(Seq)

  def addMeasurement(measurement: Measurement): Measurement =
    val newMeasurement = measurement.copy(id = measurements.size + 1)
    measurements.addOne(newMeasurement.id, newMeasurement)
    newMeasurement

  def updateMeasurement(measurement: Measurement): Unit = measurements.update(measurement.id, measurement)

  def listCleanings(): Seq[Cleaning] = cleanings.values.to(Seq)

  def addCleaning(cleaning: Cleaning): Cleaning =
    val newCleaning = cleaning.copy(id = cleanings.size + 1)
    cleanings.addOne(newCleaning.id, newCleaning)
    newCleaning

  def updateCleaning(cleaning: Cleaning): Unit = cleanings.update(cleaning.id, cleaning)

  def listChemicals(): Seq[Chemical] = chemicals.values.to(Seq)

  def addChemical(chemical: Chemical): Chemical =
    val newChemical = chemical.copy(id = chemicals.size + 1)
    chemicals.addOne(newChemical.id, newChemical)
    newChemical

  def updateChemical(chemical: Chemical): Unit = chemicals.update(chemical.id, chemical)

  def listSupplies(): Seq[Supply] = supplies.values.to(Seq)

  def addSupply(supply: Supply): Supply =
    val newSupply = supply.copy(id = supplies.size + 1)
    supplies.addOne(newSupply.id, newSupply)
    newSupply

  def updateSupply(supply: Supply): Unit = supplies.update(supply.id, supply)

  def listRepairs(): Seq[Repair] = repairs.values.to(Seq)

  def addRepair(repair: Repair): Repair =
    val newRepair = repair.copy(id = repairs.size + 1)
    repairs.addOne(newRepair.id, newRepair)
    newRepair

  def updateRepair(repair: Repair): Unit = repairs.update(repair.id, repair)

  def listEmails: Seq[Email] = emails.values.filter(_.processed == false).to(Seq)

  def addEmail(email: Email): Unit = emails.addOne(email.id, email)

  def updateEmail(email: Email): Unit = emails.update(email.id, email)

  def listFaults: Seq[Fault] = faults.values.to(Seq)
  
  def addFault(fault: Fault): Unit = faults.addOne((fault.dateOf, fault.timeOf, fault.nanoOf), fault)