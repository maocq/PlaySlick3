package persistencia.usuario

import dominio.Usuario

trait UsuarioDAOAdaptador {

  def transformar(record: UsuarioRecord): Usuario = {
    Usuario(record.id, record.nombre, record.apellido, record.email)
  }

  def transformar(usuario: Usuario): UsuarioRecord = {
    UsuarioRecord(usuario.id, usuario.nombre, usuario.apellido, usuario.email)
  }

}
