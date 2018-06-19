package infraestructura.http

import play.api.libs.json.Json

trait TransformadorDTOs {

  implicit val userDTOReads = Json.reads[UserDTO]
  implicit val userDTOWrites = Json.writes[UserDTO]

}
