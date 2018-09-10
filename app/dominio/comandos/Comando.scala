package dominio.comandos

import play.api.libs.json.JsValue

import scala.concurrent.Future

trait Comando {

  def ejecutar(jsValue:  JsValue) : Future[Consecuencia]

}
