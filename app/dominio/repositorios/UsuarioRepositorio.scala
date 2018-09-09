package dominio.repositorios

import dominio.Usuario
import javax.inject.Inject
import persistencia.usuario.{UsuarioDAO, UsuarioDAOAdaptador}

import scala.concurrent.{ExecutionContext, Future}

class UsuarioRepositorio @Inject()(usuarioDAO: UsuarioDAO)(implicit ec: ExecutionContext) extends UsuarioDAOAdaptador {

  def listar(): Future[Seq[Usuario]] = {
    usuarioDAO.listar().map( _.map(transformar) )
  }

  def insertar(usuario: Usuario): Future[Usuario] = {
    val record = transformar(usuario)
    usuarioDAO.insertar(record).map(transformar)
  }

}
