package controllers

import cats.data.EitherT
import cats.implicits._
import dominio.repositorios.UsuarioRepositorio
import dominio.{ErrorAplicacion, TransformadorDominio, Usuario}
import infraestructura.http.{ServicioHTTP, TransformadorDTOs, UserDTO}
import javax.inject._
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import playslick3.appExecutionContext

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents, usuarioRepositorio: UsuarioRepositorio, http: ServicioHTTP[UserDTO])
  extends AbstractController(cc) with TransformadorDominio with TransformadorDTOs {

  implicit val ec: ExecutionContext = appExecutionContext

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listar()= Action.async { implicit request: Request[AnyContent]  =>
    usuarioRepositorio.listar().map{ usuarios => Ok(Json.toJson(usuarios))
    } recover { case e => InternalServerError("Error") }
  }

  def insertar = Action.async(parse.json) { request: Request[JsValue]  =>
    val dto = request.body.validate[Usuario]
    dto.fold(
      error => Future.successful(BadRequest(Json.obj("status" ->"ERROR", "message" -> JsError.toJson(error)))),
      dto =>
        usuarioRepositorio.insertar(dto).map { id =>
          Ok(Json.obj("status" -> "OK", "message" -> ("Usuario '" + dto.email + "' insertado.")))
        } recover { case e => InternalServerError("Error") }
    )
  }

  def consultarPost() = Action.async { implicit request: Request[AnyContent] =>
    http.consultar("https://jsonplaceholder.typicode.com/posts/1").flatMap(resultado =>
      resultado.fold(
        error => Future.successful(BadRequest(Json.obj("status" ->"ERROR", "message" -> error))),
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
    //Future.successful(Left(ErrorDominio("Error", "20")))
    Future.successful(Right(1))
  }

}
