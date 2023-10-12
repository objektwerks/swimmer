package swimmer

import java.time.Instant
import java.util.UUID

import scala.util.Random

import scalafx.beans.property.ObjectProperty

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

object Entity:
  given swimmerOrdering: Ordering[Swimmer] = Ordering.by[Swimmer, String](s => s.name)
  given sessionOrdering: Ordering[Session] = Ordering.by[Session, Long](dt => dt.datetime).reverse

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
                         name: String) extends Entity:
  val nameProperty = ObjectProperty[String](this, "name", name)
  val swimmer = this

final case class Session(id: Long = 0,
                         swimmerId: Long,
                         weight: Int,
                         weightUnit: String = WeightUnit.lb.toString,
                         laps: Int,
                         lapDistance: Int = 50,
                         lapUnit: String = LapUnit.yards.toString,
                         style: String = Style.freestyle.toString,
                         kickboard: Boolean = false,
                         fins: Boolean = false,
                         minutes: Int,
                         seconds: Int = 0,
                         calories: Int = 0,
                         datetime: Long = Instant.now.toEpochMilli) extends Entity:
  val weightProperty = ObjectProperty[Int](this, "weight", weight)
  val weightUnitProperty = ObjectProperty[String](this, "weightUnit", weightUnit)
  val lapsProperty = ObjectProperty[Int](this, "laps", laps)
  val lapDistanceProperty = ObjectProperty[Int](this, "lapDistance", laps)
  val lapUnitProperty = ObjectProperty[String](this, "lapUnit", lapUnit)
  val styleProperty = ObjectProperty[String](this, "style", style)
  val kickboardProperty = ObjectProperty[Boolean](this, "kickboard", kickboard)
  val finsProperty = ObjectProperty[Boolean](this, "fins", fins)
  val minutesProperty = ObjectProperty[Int](this, "minutes", minutes)
  val secondsProperty = ObjectProperty[Int](this, "seconds", seconds)
  val caloriesProperty = ObjectProperty[Int](this, "calories", calories)
  val datetimeProperty = ObjectProperty[Long](this, "datetime", datetime)
  val session = this

enum WeightUnit:
  case lb, kg
  def toList: List[String] = WeightUnit.values.map(wu => wu.toString).toList

object WeightUnit:
  def lbsToKgs(lbs: Double): Double = lbs * 0.454
  def kgsToLbs(kgs: Double): Double = kgs * 2.205

enum LapUnit:
  case feet, meters, yards

enum Style:
  case breaststroke, backstroke, butterfly, freestyle, kick