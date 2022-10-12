package poolmate

sealed trait Event

final case class Registered(pin: String,
                            success: Boolean = true,
                            error: String = "") extends Event:
  def success(pin: String): Registered = Registered(pin)
  def fail(error: String): Registered = Registered("", false, error)

final case class LoggedIn(user: User, 
                          serviceProviders: Array[User], 
                          workOrders: Array[WorkOrder], 
                          success: Boolean = true, 
                          error: String = "") extends Event:

  def success(user: User, serviceProviders: Array[User], workOrders: Array[WorkOrder]): LoggedIn = LoggedIn(user, serviceProviders, workOrders)
  def fail(error: String): LoggedIn = LoggedIn(User.empty, Array.empty[User], Array.empty[WorkOrder], false, error)

final case class UserSaved(id: Int, 
                           success: Boolean = true, 
                           error: String = "") extends Event:

  def success(id: Int): UserSaved = UserSaved(id)
  def fail(id: Int, error: String): UserSaved = UserSaved(id, false, error)

/*
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
*/