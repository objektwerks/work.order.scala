package workorder

import com.raquo.laminar.api.L._

import org.scalajs.dom
import org.scalajs.dom.Headers
import org.scalajs.dom.HttpMethod
import org.scalajs.dom.RequestInit
import org.scalajs.dom.console.log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.Thenable.Implicits.*

import Serializer.given

import upickle.default.{read, write}

object Fetcher:
	val post = "post"
	val jsonHeaders = new Headers {
		js.Array(
		  js.Array("Content-Type", "application/json; charset=UTF-8"),
		  js.Array("Accept", "application/json")
		)
	}
	val formDataHeaders = new Headers {
		js.Array()
	}
	val jsonParameters = new RequestInit {
    method = HttpMethod.POST
    headers = jsonHeaders
  }
	val formDataParameters = new RequestInit {
	  method = HttpMethod.POST
	  headers = formDataHeaders
	}