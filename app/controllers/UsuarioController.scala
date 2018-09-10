package controllers

import dominio.TransformadorDominio
import dominio.comandos.ListarUsuarios
import infraestructura.http.TransformadorDTOs
import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.{ControllerComponents, Request}

import scala.concurrent.ExecutionContext

class UsuarioController @Inject()(cc: ControllerComponents, listarUsuarios: ListarUsuarios)(implicit ec: ExecutionContext)
  extends ControladorDeComandos(cc) with TransformadorDominio with TransformadorDTOs {

  def listarComando()= Action.async(parse.json) { implicit request: Request[JsValue]  =>
    ejecutar(listarUsuarios, request.body)
  }

}
