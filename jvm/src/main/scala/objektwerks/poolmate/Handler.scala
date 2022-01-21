package objektwerks.poolmate

class Handler(service: Service):
  def handle(command: Command): Event =
    command match
      case register: Register =>
        service.register(register.emailAddress).fold(throwable => Fault(throwable), account => Registered(account))
      case login: Login =>
        service.login(login.emailAddress, login.pin).fold(throwable => Fault(throwable), account => LoggedIn(account))
      
      case deactivate: Deactivate =>
        service.deactivate(deactivate.license).fold(throwable => Fault(throwable), account => Deactivated(account))
      case reactivate: Reactivate =>
        service.reactivate(reactivate.license).fold(throwable => Fault(throwable), account => Reactivated(account))

      case list: ListPools =>
        service.listPools().fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddPool =>
        service.addPool(add.pool).fold(throwable => Fault(throwable), entity => Added(entity))
      case update: UpdatePool =>
        service.updatePool(update.pool).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListSurfaces =>
        service.listSurfaces(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddSurface =>
        service.addSurface(add.surface).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateSurface =>
          service.updateSurface(update.surface).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListPumps =>
        service.listPumps(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddPump =>
        service.addPump(add.pump).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdatePump =>
        service.updatePump(update.pump).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListTimers =>
        service.listTimers(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddTimer =>
        service.addTimer(add.timer).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateTimer =>
        service.updateTimer(update.timer).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListTimerSettings =>
        service.listTimerSettings(list.timerId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddTimerSetting =>
        service.addTimerSetting(add.timerSetting).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateTimerSetting =>
        service.updateTimerSetting(update.timerSetting).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListHeaters =>
        service.listHeaters(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddHeater =>
        service.addHeater(add.heater).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateHeater =>
        service.updateHeater(update.heater).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListHeaterSettings =>
        service.listHeaterSettings(list.heaterId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddHeaterSetting =>
        service.addHeaterSetting(add.heaterSetting).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateHeaterSetting =>
        service.updateHeaterSetting(update.heaterSetting).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListMeasurements =>
        service.listMeasurements(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddMeasurement =>
        service.addMeasurement(add.measurement).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateMeasurement =>
        service.updateMeasurement(update.measurement).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListCleanings =>
        service.listCleanings(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddCleaning =>
        service.addCleaning(add.cleaning).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateCleaning =>
        service.updateCleaning(update.cleaning).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListChemicals =>
        service.listChemicals(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddChemical =>
        service.addChemical(add.chemical).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateChemical =>
        service.updateChemical(update.chemical).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListSupplies =>
        service.listSupplies(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddSupply =>
        service.addSupply(add.supply).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateSupply =>
        service.updateSupply(update.supply).fold(throwable => Fault(throwable), _ => Updated())

      case list: ListRepairs =>
        service.listRepairs(list.poolId).fold(throwable => Fault(throwable), entities => Listed(entities))
      case add: AddRepair =>
        service.addRepair(add.repair).fold(throwable => Fault(throwable), entity => objektwerks.entity.Added(entity))
      case update: UpdateRepair =>
        service.updateRepair(update.repair).fold(throwable => Fault(throwable), _ => Updated())
