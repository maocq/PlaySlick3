package dominio

import dominio.ErrorAplicacion.{ErrorDominio, ErrorTecnico, ErrorValidacion}
import play.api.libs.json._
import play.api.libs.functional.syntax._


trait TransformadorErrores {

  implicit val errorTecnicoRds: Reads[ErrorTecnico] = (
    ( JsPath \ "mensaje" ).read[String] and
      ( JsPath \ "codigoError" ).read[String]
    ).apply( ( mj, cd) => ErrorTecnico( mj, cd, None) )

  implicit val errorValidacionRds: Reads[ErrorValidacion] = (
    ( JsPath \ "mensaje" ).read[String] and
      ( JsPath \ "codigoError" ).read[String]
    ).apply( ( mj, cd) => ErrorValidacion( mj, List.empty ,cd) )

  implicit val errorDominioRds: Reads[ErrorDominio] = (
    ( JsPath \ "mensaje" ).read[String] and
      ( JsPath \ "codigoError" ).read[String]
    ).apply( ( mj, cd) => ErrorDominio( mj, cd, None) )


  implicit val errorAplicacionWts: Writes[ErrorAplicacion] = Writes {
    case validacion@ErrorValidacion(_, _, _, _) => writesErrorValidacion(validacion)
    case error => writesErrorAplicacion(error)
  }

  def writesErrorValidacion(error: ErrorValidacion) = {
    JsObject( Seq(
      "mensaje" -> JsString( error.mensaje ),
      "codigoError" -> JsString( error.codigoError ),
      "campos" -> Json.toJson(error.campos)
    ))
  }

  def writesErrorAplicacion(error: ErrorAplicacion) = {
    JsObject( Seq(
      "mensaje" -> JsString( error.mensaje ),
      "codigoError" -> JsString( error.codigoError )
    ))
  }

}
