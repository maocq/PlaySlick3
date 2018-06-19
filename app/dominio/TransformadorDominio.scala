package dominio

import play.api.libs.json.Json

trait TransformadorDominio {

  implicit val usuarioWrites = Json.writes[Usuario]
  implicit val usuarioReads = Json.reads[Usuario]

}
