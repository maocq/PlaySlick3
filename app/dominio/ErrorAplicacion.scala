package dominio

trait ErrorAplicacion extends Product with Serializable {
  val mensaje: String
  val codigoError: String
  val error: Option[Throwable]
}

object ErrorAplicacion {

  case class ErrorTecnico(mensaje: String, codigoError:String, error: Option[Throwable] = None) extends ErrorAplicacion
  case class ErrorDominio(mensaje: String, codigoError:String, error: Option[Throwable] = None) extends ErrorAplicacion
  case class ErrorValidacion(mensaje: String, campos: Seq[String], codigoError:String, error: Option[Throwable] = None) extends ErrorAplicacion

}