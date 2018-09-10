package controllers

import dominio.ErrorAplicacion.ErrorValidacion
import dominio.comandos.Comando
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class ControladorDeComandos(cc: ControllerComponents) (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def ejecutar(comando: Comando, jsValue:  JsValue): Future[Result] = {
    comando.ejecutar(jsValue).map(consecuencia => {
      consecuencia.respuesta.fold(
        {
          case ErrorValidacion(mensaje, _, _, _) => BadRequest(Json.toJson(mensaje))
          case error => InternalServerError(Json.toJson(error.mensaje))
        },
        json => Ok(json)
      )
    }) recover { case e => InternalServerError(Json.obj("status" ->"ERROR", "message" -> Json.toJson(e.getMessage))) }
  }
}
