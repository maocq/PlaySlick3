package persistencia.usuario

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

case class UsuarioRecord(id: Long, nombre: String, apellido: String, email: String)

class UsuarioDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def listar(): Future[Seq[UsuarioRecord]] = db.run {
    usuariosdb.result
  }

  def insertar(usuarioRecord: UsuarioRecord): Future[UsuarioRecord] = {
    val insertar = (usuariosdb returning usuariosdb) += usuarioRecord
    db.run(insertar)
  }

  private class UsuariosRecord(tag: Tag)  extends Table[UsuarioRecord](tag, "usuarios") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def apellido = column[String]("apellido")
    def email = column[String]("email")

    def * = (id, nombre, apellido, email) <> (UsuarioRecord.tupled, UsuarioRecord.unapply)
  }

  private val usuariosdb = TableQuery[UsuariosRecord]

}

