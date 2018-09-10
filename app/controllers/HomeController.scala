package controllers

import cats.data.EitherT
import cats.implicits._
import dominio.repositorios.UsuarioRepositorio
import dominio.{ErrorAplicacion, TransformadorDominio, Usuario}
import infraestructura.acl.dto.UsuarioDTO
import infraestructura.http.{ServicioHTTP, TransformadorDTOs, UserDTO}
import javax.inject._
import play.api.libs.json.{ JsValue, Json}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents, usuarioRepositorio: UsuarioRepositorio, http: ServicioHTTP[UserDTO])(implicit ec: ExecutionContext)
  extends AbstractController(cc) with TransformadorDominio with TransformadorDTOs {


  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listar()= Action.async { implicit request: Request[AnyContent]  =>
    usuarioRepositorio.listar().map{ usuarios => Ok(Json.toJson(usuarios))
    } recover { case e => InternalServerError("Error") }
  }

  def insertar = Action.async(parse.json) { request: Request[JsValue]  =>
    val dto = request.body.validate[UsuarioDTO].asEither.leftMap(e => List("Json invalido")).flatMap(_.validar)
    dto.fold(
      error => Future.successful(BadRequest(Json.obj("status" ->"ERROR", "message" -> Json.toJson(error)))),
      dto =>
        usuarioRepositorio.insertar(Usuario(0, dto.nombre, dto.apellido, dto.email)).map { usuario =>
          Ok(Json.toJson(usuario))
        } recover { case e => InternalServerError("Error") }
    )
  }

  def consultarPost() = Action.async { implicit request: Request[AnyContent] =>
    http.consultar("https://jsonplaceholder.typicode.com/posts/1").flatMap(resultado =>
      resultado.fold(
        error => Future.successful(BadRequest(Json.obj("status" ->"ERROR", "message" -> error.mensaje))),
        post => Future.successful(Ok(Json.toJson(post)))
      )
    ) recover { case e => InternalServerError("Error") }
  }


  def ejemplo() = {
    val res: Future[Either[ErrorAplicacion, Int]] = ( for {
      x <- EitherT(test)
      y <- EitherT(test)
      z <- EitherT(test)
    } yield x + y + z ).value
  }

  def test: Future[Either[ErrorAplicacion, Int]] = {
    Future.successful(Right(1))
  }

}
