package dominio.repositorios

import dominio.Usuario
import javax.inject.Inject
import persistencia.usuario.{UsuarioDAO, UsuarioDAOAdaptador}
import playslick3.appExecutionContext

import scala.concurrent.{ExecutionContext, Future}

class UsuarioRepositorio @Inject()(usuarioDAO: UsuarioDAO) extends UsuarioDAOAdaptador {

  implicit val ec: ExecutionContext = appExecutionContext

  def listar(): Future[Seq[Usuario]] = {
    usuarioDAO.listar().map( _.map(transformar) )
  }

  def insertar(usuario: Usuario): Future[Usuario] = {
    val record = transformar(usuario)
    usuarioDAO.insertar(record).map(transformar)
  }

}
