package controllers

import dominio.Usuario
import infraestructura.TransformadorDTO
import javax.inject._
import persistencia.usuario.UsuarioDAO
import play.api._
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import playslick3.appExecutionContext

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents, usuarioDAO: UsuarioDAO)
  extends AbstractController(cc) with TransformadorDTO {

  implicit val ec: ExecutionContext = appExecutionContext

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listar()= Action.async { implicit request: Request[AnyContent]  =>
    usuarioDAO.listar().map{ usuarios => Ok(Json.toJson(usuarios))
    } recover { case e => InternalServerError("Error") }
  }

  def insertar = Action.async(parse.json) { request: Request[JsValue]  =>
    val dto = request.body.validate[Usuario]
    dto.fold(
      error => Future.successful(BadRequest(Json.obj("status" ->"ERROR", "message" -> JsError.toJson(error)))),
      dto =>
        usuarioDAO.insertar(dto).map { id =>
          Ok(Json.obj("status" -> "OK", "message" -> ("Usuario '" + dto.email + "' insertado.")))
        } recover { case e => InternalServerError("Error") }
    )
  }

}
