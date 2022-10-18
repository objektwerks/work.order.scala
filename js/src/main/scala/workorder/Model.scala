package workorder

import com.raquo.laminar.api.L.*

import org.scalajs.dom.File

final case class ImageFile(number: Int, file: File, filename: String, url: String)

object Model:
  val user = Var(User.empty)
  val serviceProviders = Var(Array.empty[User])
  val workOrders = Var(Array.empty[WorkOrder])
  val imageFile: Option[ImageFile] = None