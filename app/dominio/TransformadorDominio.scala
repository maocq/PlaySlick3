package dominio

import play.api.libs.json.Json

trait TransformadorDominio {

  implicit val usuarioFormat = Json.format[Usuario]

}
