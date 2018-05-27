package persistencia.usuario

import dominio.Usuario
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UsuarioDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def listar(): Future[Seq[Usuario]] = db.run {
    usuariosdb.result
  }


  private class Usuarios(tag: Tag)  extends Table[Usuario](tag, "usuarios") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def apellido = column[String]("apellido")
    def email = column[String]("email")

    def * = (id, nombre, apellido, email) <> (Usuario.tupled, Usuario.unapply)
  }

  private val usuariosdb = TableQuery[Usuarios]

}

