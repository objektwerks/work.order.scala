package objektwerks.service

import objektwerks.datetime.DateTime
import objektwerks.entity.*

trait Store:
  def register(email: String): Option[Account]
  def login(email: String, pin: String): Option[Account]

  def isAuthorized(license: String): Boolean

  def deactivate(license: String): Option[Account]
  def reactivate(license: String): Option[Account]

  def listPools(): Seq[Pool]
  def addPool(pool: Pool): Pool
  def updatePool(pool: Pool): Unit

  def listSurfaces(): Seq[Surface]
  def addSurface(surface: Surface): Surface
  def updateSurface(surface: Surface): Unit

  def listPumps(): Seq[Pump]
  def addPump(pump: Pump): Pump
  def updatePump(pump: Pump): Unit

  def listTimers(): Seq[Timer]
  def addTimer(timer: Timer): Timer
  def updateTimer(timer: Timer): Unit

  def listTimerSettings(): Seq[TimerSetting]
  def addTimerSetting(timerSetting: TimerSetting): TimerSetting
  def updateTimerSetting(timerSetting: TimerSetting): Unit

  def listHeaters(): Seq[Heater]
  def addHeater(heater: Heater): Heater
  def updateHeater(heater: Heater): Unit

  def listHeaterSettings(): Seq[HeaterSetting]
  def addHeaterSetting(heaterSetting: HeaterSetting): HeaterSetting
  def updateHeaterSetting(heaterSetting: HeaterSetting): Unit

  def listMeasurements(): Seq[Measurement]
  def addMeasurement(measurement: Measurement): Measurement
  def updateMeasurement(measurement: Measurement): Unit

  def listCleanings(): Seq[Cleaning]
  def addCleaning(cleaning: Cleaning): Cleaning
  def updateCleaning(cleaning: Cleaning): Unit

  def listChemicals(): Seq[Chemical]
  def addChemical(chemical: Chemical): Chemical
  def updateChemical(chemical: Chemical): Unit

  def listSupplies(): Seq[Supply]
  def addSupply(supply: Supply): Supply
  def updateSupply(supply: Supply): Unit

  def listRepairs(): Seq[Repair]
  def addRepair(repair: Repair): Repair
  def updateRepair(repair: Repair): Unit

  def listEmails: Seq[Email]
  def addEmail(email: Email): Unit
  def updateEmail(email: Email): Unit

  def listFaults: Seq[Fault]
  def addFault(fault: Fault): Unit