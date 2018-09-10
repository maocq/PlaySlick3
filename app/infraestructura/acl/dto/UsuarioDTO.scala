package infraestructura.acl.dto

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._

case class UsuarioDTO(nombre: String, apellido: String, email: String) extends DTOComando {

  def validar: Either[List[String], UsuarioDTO] = {
    val validacion: ValidatedNel[String, UsuarioDTO] = (
      validarNombre,
      validarApellido,
      validarEmail
    ).mapN(UsuarioDTO)
    validacion.toEither.leftMap(_.toList)
  }

  private def validarNombre: Validated[NonEmptyList[String], String] = {
    if (nombre.length < 2 )"usuario.nombre".invalidNel else nombre.validNel
  }

  private def validarApellido: Validated[NonEmptyList[String], String] = {
    if (apellido.length < 2 )"usuario.apellido".invalidNel else apellido.validNel
  }

  private def validarEmail: Validated[NonEmptyList[String], String] = {
    if (email.length < 2 )"usuario.mail".invalidNel else email.validNel
  }


}
