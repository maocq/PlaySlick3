package controllers

import cats.implicits._
import dominio.ErrorAplicacion.ErrorTecnico
import dominio.TransformadorErrores
import dominio.comandos.Comando
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class ControladorDeComandos(cc: ControllerComponents) (implicit ec: ExecutionContext)
  extends AbstractController(cc) with TransformadorErrores {

  val logger: Logger = Logger(this.getClass)

  def ejecutar(comando: Comando, jsValue:  JsValue): Future[Result] = {
    logger.info(s"Comando ${comando.getClass.getName} Json: $jsValue")
    comando.ejecutar(jsValue).map(consecuencia => {
      consecuencia.respuesta.fold(
        {
          case error@ErrorTecnico(_, _, exc) => {
            logger.error(s"Error aplicación ${Json.toJson(error)}", exc.orNull)
            InternalServerError(Json.toJson(error))
          }
          case error => {
            logger.error(s"BadRequest ${Json.toJson(error)}", error.error.orNull)
            BadRequest(Json.toJson(error))
          }
        },
        json => {
          logger.info(s"Respuesta ${comando.getClass.getName} Json: $json")
          Ok(json)
        }
      )
    }) recover { case e => {
      logger.error(s"Internal server error ${e.getMessage}", e)
      InternalServerError(Json.toJson(ErrorTecnico("Internal server error", "500", e.some)))
    }}
  }
}
