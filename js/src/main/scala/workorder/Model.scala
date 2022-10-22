package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.File

final case class ImageFile(number: Int, file: File, filename: String, url: String)

object Model:
  val userVar = Var(User.empty)
  val serviceProvidersVar = Var(Array.empty[User])
  val workOrdersVar = Var(Array.empty[WorkOrder])
  val workOrderVar = Var(WorkOrder.empty)
  val imageFile: Option[ImageFile] = None