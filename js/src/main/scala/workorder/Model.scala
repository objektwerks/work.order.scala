package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.File

final case class ImageFile(number: Int, file: File, filename: String, url: String)

object Model:
  val userVar = Var(User.empty)
  val homeownersVar = Var(List.empty[User])
  val serviceProvidersVar = Var(List.empty[User])
  val workOrdersVar = Var(List.empty[WorkOrder])
  val workOrderVar = Var(WorkOrder.empty)
  val imageFile: Option[ImageFile] = None

  def imageFileUrl: String = imageFile.fold("")(i => i.url)

  def homeownerName(id: Int): String = homeownersVar.now().find(_.id == id).fold("")(_.name)

  def serviceProviderName(id: Int): String = serviceProvidersVar.now().find(_.id == id).fold("")(_.name)

  def openWorkOrders: List[WorkOrder] = workOrdersVar.now().filter(workOrder => workOrder.closed.isEmpty)

  def closedWorkOrders: List[WorkOrder] = workOrdersVar.now().filter(workOrder => workOrder.closed.nonEmpty)