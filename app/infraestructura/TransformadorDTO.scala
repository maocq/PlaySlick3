package infraestructura

import dominio.Usuario
import play.api.libs.json.Json

trait TransformadorDTO {

  implicit val usersWrites = Json.writes[Usuario]
  implicit val usersReads = Json.reads[Usuario]

}
