package dominio.comandos

import dominio.ErrorAplicacion
import play.api.libs.json.JsValue

case class Consecuencia(evento: List[String], respuesta: Either[ErrorAplicacion, JsValue])
