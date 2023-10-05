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
  private val random = new Random

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
                           height: Int = 0,
                           weight: Int = 0) extends Entity

  final case class Session(id: Long = 0,
                           swimmerId: Long,
                           lap: Int,
                           laps: Int,
                           duration: Long,
                           stroke: String = Stroke.free.toString,
                           aide: String = Aid.none.toString,
                           to: Long = Instant.now.toEpochMilli,
                           from: Long = Instant.now.toEpochMilli + 1) extends Entity

  enum Stroke:
    case free, breast, back

  enum Aid:
    case kickboard, fins, none