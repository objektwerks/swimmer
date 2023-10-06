package swimmer

import java.time.Instant
import java.util.UUID

import scala.util.Random

sealed trait Entity:
  val id: Long

final case class Account(id: Long = 0,
                         license: String = newLicense,
                         emailAddress: String = "",
                         pin: String = newPin,
                         activated: Long = Instant.now.toEpochMilli,
                         deactivated: Long = 0) extends Entity

object Account:
  private val specialChars = "~!@#$%^&*-+=<>?/:;".toList
  private val random = Random

  private def newSpecialChar: Char = specialChars(random.nextInt(specialChars.length))

  private def newPin: String =
    Random.shuffle(
      Random
        .alphanumeric
        .take(5)
        .mkString
        .prepended(newSpecialChar)
        .appended(newSpecialChar)
    ).mkString

  private def newLicense: String = UUID.randomUUID.toString

  val empty = Account(
    license = "",
    emailAddress = "",
    pin = "",
    activated = 0,
    deactivated = 0
  )

final case class Swimmer(id: Long = 0,
                          license: String = "",
                          name: String = "",
                          weight: Int = 150) extends Entity

final case class Session(id: Long = 0,
                          swimmerId: Long,
                          lap: Int,
                          unit: String = Unit.yards.toString,
                          laps: Int,
                          stroke: String = Stroke.freestyle.toString,
                          kickboard: Boolean = false,
                          fins: Boolean = false,
                          minutes: Int = 1,
                          seconds: Int = 0,
                          swam: Long = Instant.now.toEpochMilli) extends Entity

enum Unit:
  case feet, yards, meters

enum Stroke:
  case breast, back, butterfly, freestyle, kick