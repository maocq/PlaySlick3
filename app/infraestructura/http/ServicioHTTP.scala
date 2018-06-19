package infraestructura.http

import dominio.ErrorAplicacion
import dominio.ErrorAplicacion.ErrorValidacion
import javax.inject.Inject
import play.api.libs.json.Reads
import play.api.libs.ws.WSClient
import playslick3.appExecutionContext

import scala.concurrent.{ExecutionContext, Future}

class ServicioHTTP[A] @Inject() (ws: WSClient)  {

  implicit val ec: ExecutionContext = appExecutionContext

  def consultar(url: String)(implicit m: Reads[A]): Future[Either[ErrorAplicacion, A]] = {

      ws.url(url).get().map( res =>
        res.json.validate[A].fold(
          error => Left(ErrorValidacion("Error JSON", error.map(_.toString()), "101")),
          ok => Right(ok)
        )
      )

  }

}
