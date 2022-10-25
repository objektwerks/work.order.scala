package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.File

final case class ImageFile(number: Int, file: File, filename: String, url: String)

object Model:
  val userVar = Var(User.empty)
  val usersVar = Var(List.empty[User])
  val workOrdersVar = Var(List.empty[WorkOrder])
  val workOrderVar = Var(WorkOrder.empty)
  val imageFile: Option[ImageFile] = None

  def imageFileUrl: String = imageFile.fold("")(i => i.url)

  def homeownersVar: Var[List[User]] = Var(usersVar.now().filter(user => user.role == Roles.serviceProvider))

  def serviceProvidersVar: Var[List[User]] = Var(usersVar.now().filter(user => user.role == Roles.serviceProvider))

  def homeownerName(id: Int): String = usersVar.now().find(_.id == id).fold("")(_.name)

  def serviceProviderName(id: Int): String = usersVar.now().find(_.id == id).fold("")(_.name)

  def openWorkOrders: Var[List[WorkOrder]] = Var(workOrdersVar.now().filter(workOrder => workOrder.closed.isEmpty))

  def closedWorkOrders: Var[List[WorkOrder]] = Var(workOrdersVar.now().filter(workOrder => workOrder.closed.nonEmpty))