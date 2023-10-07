package swimmer

import java.time.Instant
import java.util.UUID

import scala.util.Random

object Pin:
  private val specialChars = "~!@#$%^&*-+=<>?/:;".toList
  private val random = Random

  private def newSpecialChar: Char = specialChars( random.nextInt(specialChars.length) )

  def newInstance: String =
    Random.shuffle(
      Random
        .alphanumeric
        .take(5)
        .mkString
        .prepended(newSpecialChar)
        .appended(newSpecialChar)
    ).mkString

sealed trait Entity:
  val id: Long

final case class Account(id: Long = 0,
                         license: String = UUID.randomUUID.toString,
                         emailAddress: String = "",
                         pin: String = Pin.newInstance,
                         activated: Long = Instant.now.toEpochMilli,
                         deactivated: Long = 0) extends Entity

object Account:
  val empty = Account(
    license = "",
    emailAddress = "",
    pin = "",
    activated = 0,
    deactivated = 0
  )

final case class Swimmer(id: Long = 0,
                         license: String,
                         name: String) extends Entity

final case class Session(id: Long = 0,
                         swimmerId: Long,
                         weight: Int,
                         weightUnit: String = Unit.lb.toString,
                         laps: Int,
                         lapUnit: String = Unit.yards.toString,
                         style: String = Style.freestyle.toString,
                         kickboard: Boolean = false,
                         fins: Boolean = false,
                         minutes: Int,
                         seconds: Int,
                         calories: Int,
                         datetime: Long = Instant.now.toEpochMilli) extends Entity

enum Unit:
  case lb, kg, feet, meters, yards
  def lbsToKgs(lbs: Double): Double = lbs * 0.454
  def kgsToLbs(kgs: Double): Double = kgs * 2.205

enum Style:
  case breaststroke, backstroke, butterfly, freestyle, kick