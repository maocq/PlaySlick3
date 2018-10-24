package controllers

import cats.data.EitherT
import cats.implicits._
import dominio.repositorios.UsuarioRepositorio
import dominio.{ErrorAplicacion, TransformadorDominio, Usuario}
import infraestructura.acl.dto.UsuarioDTO
import infraestructura.http.{ServicioHTTP, TransformadorDTOs, UserDTO}
import javax.inject._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import monix.execution.Scheduler.Implicits.global
import monix.eval.Task

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(cc: ControllerComponents, usuarioRepositorio: UsuarioRepositorio, http: ServicioHTTP[UserDTO])(implicit ec: ExecutionContext)
  extends ControladorDeComandos(cc) with TransformadorDominio with TransformadorDTOs {


  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def listar()= Action.async { implicit request: Request[AnyContent]  =>
    usuarioRepositorio.listar().map{ usuarios => Ok(Json.toJson(usuarios))
    } recover { case e => InternalServerError("Error") }
  }

  def insertar = Action.async(parse.json) { implicit request: Request[JsValue]  =>
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


  def eithert() = Action { implicit request: Request[AnyContent] =>

    val res: Future[Either[ErrorAplicacion, Int]] = ( for {
      x <- EitherT(test)
      y <- EitherT(test)
      z <- EitherT(test)
    } yield x + y + z ).value

    Ok(views.html.index())
  }


  def eithertImplicit() = Action { implicit request: Request[AnyContent] =>
    /*
    class EitherTWrapper(value: Future[Either[ErrorAplicacion, Int]]) {
      def toEitherT: EitherT[Future, ErrorAplicacion, Int] = EitherT(value)
    }
    implicit def implicitEitherTFuture(value: Future[Either[ErrorAplicacion, Int]]) = new EitherTWrapper(value)
     */

    class EitherTWrapper[A, B](value: Future[Either[A, B]]) {
      def toEitherT: EitherT[Future, A, B] = EitherT(value)
    }
    implicit def implicitEitherTFuture[A, B](value: Future[Either[A, B]]) = new EitherTWrapper(value)

    /*
    class EitherTWrapper[F[_], A, B](value: F[Either[A, B]]) {
      def toEitherT: EitherT[F, A, B] = EitherT(value)
    }
    implicit def implicitEitherTFuture[F[_], A, B](value: F[Either[A, B]]) = new EitherTWrapper(value)
    */

    val toEitherT: Future[Either[ErrorAplicacion, Int]] = ( for {
      x <- test.toEitherT
      y <- test.toEitherT
      z <- test.toEitherT
    } yield x + y + z ).value

    Ok(views.html.index())
  }

  private def test: Future[Either[ErrorAplicacion, Int]] = {
    Future.successful(Right(1))
  }

  def task() = Action.async { implicit request: Request[AnyContent] =>

    val task1 = Task { println("Effect1!"); "Result1" }
    val task2 = Task { println("Effect2!"); "Result2" }

    val res = for {
      x <- task1
      y <- task2
    } yield x + y

    res.map( t => Ok(" =) " + t)).runAsync
  }

  def taskEitherT() = Action.async { implicit request: Request[AnyContent] =>
    EitherT(testTask)
    //EitherT(testTask).flatMap(r => EitherT(testTask))

//    val res = for {
//      x <- EitherT(testTask)
//      y <- EitherT(testTask)
//    } yield x + y

    Future.successful(Ok("=)"))
  }

  private def testTask: Task[Either[ErrorAplicacion, Int]] = {
    Task.now(1.asRight)
  }


}
