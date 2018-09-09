package infraestructura.http

import play.api.libs.json.Json

trait TransformadorDTOs {

  implicit val userDTOFormat = Json.format[UserDTO]

}
