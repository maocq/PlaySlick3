package dominio.comandos
import cats.implicits._
import dominio.TransformadorDominio
import dominio.repositorios.UsuarioRepositorio
import infraestructura.http.TransformadorDTOs
import javax.inject.Inject
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.{ExecutionContext, Future}

class ListarUsuarios @Inject()(usuarioRepositorio: UsuarioRepositorio)(implicit ec: ExecutionContext)
  extends Comando with TransformadorDominio with TransformadorDTOs {

  def ejecutar(jsValue: JsValue): Future[Consecuencia] = {
    usuarioRepositorio.listar().map{usuarios =>
      Consecuencia(List.empty, Json.toJson(usuarios).asRight)
    }
  }

}
