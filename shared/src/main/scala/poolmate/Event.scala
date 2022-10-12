package poolmate

sealed trait Event

final case class Registered(pin: String,
                            success: boolean = true,
                            error: string = "") extends Event

  static success(pin: string): Registered {
    return new Registered(pin)
  }
  
  static fail(error: string): Registered {
    return new Registered('', false, error)
  }
}

/*
export class Registered {
  constructor(public pin: string,
              public success: boolean = true,
              public error: string = '') {}

  static success(pin: string): Registered {
    return new Registered(pin)
  }
  
  static fail(error: string): Registered {
    return new Registered('', false, error)
  }
}

export class LoggedIn {
  constructor(public user: User, 
              public serviceProviders: User[], 
              public workOrders: WorkOrder[], 
              public success: boolean = true, 
              public error: string = '') {}

  static success(user: User, serviceProviders: User[], workOrders: WorkOrder[]): LoggedIn {
    return new LoggedIn(user, serviceProviders, workOrders)  
  }

  static fail(error: string): LoggedIn {
    return new LoggedIn(User.empty(), [], [], false, error)
  }
}

export class WorkOrderSaved {
  constructor(public number: number,
              public success: boolean = true,
              public error: string = '') {}

  static success(number: number): WorkOrderSaved {
    return new WorkOrderSaved(number)
  }
  
  static fail(number: number, error: string): WorkOrderSaved {
    return new WorkOrderSaved(number, false, error)
  }
}

export class WorkOrdersListed {
  constructor(public userId: number, 
              public workOrders: WorkOrder[], 
              public success: boolean = true, 
              public error: string = '') {}

  static success(userId: number, workOrders: WorkOrder[]): WorkOrdersListed {
    return new WorkOrdersListed(userId, workOrders)
  }

  static fail(userId: number, error: string): WorkOrdersListed {
    return new WorkOrdersListed(userId, [WorkOrder.empty()], false, error)
  }
}

export class UserSaved {
  constructor(public id: number, 
              public success: boolean = true, 
              public error: string = '') {}

  static success(id: number): UserSaved {
    return new UserSaved(id)
  }

  static fail(id: number, error: string): UserSaved {
    return new UserSaved(id, false, error)
  }
}
*/