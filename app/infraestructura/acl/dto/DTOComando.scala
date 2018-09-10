package infraestructura.acl.dto

trait DTOComando {

  def validar: Either[List[String], UsuarioDTO]

}
