package infraestructura.http

import infraestructura.acl.dto.UsuarioDTO
import play.api.libs.json.Json

trait TransformadorDTOs {

  implicit val usuarioDTOFormat = Json.format[UsuarioDTO]
  implicit val userDTOFormat = Json.format[UserDTO]

}
