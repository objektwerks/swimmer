package swimmer

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.collection.{FixedLength, MinLength}
import io.github.iltotore.iron.constraint.numeric.{Greater, GreaterEqual}

import java.time.{Instant, LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter
import java.util.UUID

import scalafx.beans.property.ObjectProperty

sealed trait Entity:
  val id: Long

object Entity:
  given JsonValueCodec[Entity] = JsonCodecMaker.make[Entity]

  def format(epochMillis: Long): String = toLocalDateTime(epochMillis).format(DateTimeFormatter.ISO_DATE_TIME)
  def format(localDateTime: LocalDateTime): String = localDateTime.format(DateTimeFormatter.ISO_DATE_TIME)

  def toLocalDateTime(epochMillis: Long): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneOffset.UTC)
  def toEpochMillis(localDateTime: LocalDateTime): Long = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli

final case class Account(id: Long = 0,
                         license: String = UUID.randomUUID.toString,
                         emailAddress: String = "",
                         pin: String = Pin.newInstance,
                         activated: Long = Instant.now.toEpochMilli,
                         deactivated: Long = 0) extends Entity

object Account:
  val empty = Account(license = "", emailAddress = "", pin = "", activated = 0, deactivated = 0)
  given JsonValueCodec[Account] = JsonCodecMaker.make[Account]

final case class Swimmer(id: Long = 0,
                         accountId: Long,
                         name: String) extends Entity:
  val nameProperty = ObjectProperty[String](this, "name", name)
  val swimmer = this

object Swimmer:
  given JsonValueCodec[Swimmer] = JsonCodecMaker.make[Swimmer]
  given swimmerOrdering: Ordering[Swimmer] = Ordering.by[Swimmer, String](s => s.name)

  def validate(id: Long,
               accountId: Long,
               name: String): Either[Invalidations, Swimmer] =
    val invalidations = Invalidations()
    val either = for
      id        <- id.refineEither[GreaterEqual[0]].left.map(error => invalidations.add("id", error))
      accountId <- accountId.refineEither[Greater[0]].left.map(error => invalidations.add("accountId", error))
      name      <- name.refineEither[MinLength[2]].left.map(error => invalidations.add("name", error))
    yield Swimmer(id, accountId, name)
    invalidations.toEither(either)

final case class Session(id: Long = 0,
                         swimmerId: Long,
                         weight: Int = 150,
                         weightUnit: String = WeightUnit.lb.toString,
                         laps: Int = 10,
                         lapDistance: Int = 50,
                         lapUnit: String = LapUnit.yards.toString,
                         style: String = Style.freestyle.toString,
                         kickboard: Boolean = false,
                         fins: Boolean = false,
                         minutes: Int = 15,
                         seconds: Int = 0,
                         calories: Int = 150,
                         datetime: Long = Instant.now.toEpochMilli) extends Entity:
  val weightProperty = ObjectProperty[Double](this, "weight", weight)
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
  val datetimeProperty = ObjectProperty[String](this, "datetime", Entity.format(datetime))
  val session = this

  def roundSecondsToMinute(): Int = if seconds > 29 then 1 else 0

  def caloriesBurned(): Int =
    if weight < 1.0 || minutes < 1 then 0
    else
      val kg = if WeightUnit.lb.toString == weightUnit then WeightUnit.lbsToKgs(weight) else weight.toDouble
      val met = (Session.MET * 3.5 * kg) / 200
      val mins = minutes + roundSecondsToMinute()
      ( mins * met ).round.toInt

  def distance(): Int = laps * lapDistance
  def speed(): Int = distance() / ( minutes + roundSecondsToMinute() )

object Session:
  val MET = 6
  given JsonValueCodec[Session] = JsonCodecMaker.make[Session]
  given sessionOrdering: Ordering[Session] = Ordering.by[Session, Long](dt => dt.datetime).reverse

  def validate(id: Long = 0,
               swimmerId: Long,
               weight: Int = 150,
               weightUnit: String = WeightUnit.lb.toString,
               laps: Int = 10,
               lapDistance: Int = 50,
               lapUnit: String = LapUnit.yards.toString,
               style: String = Style.freestyle.toString,
               kickboard: Boolean = false,
               fins: Boolean = false,
               minutes: Int = 15,
               seconds: Int = 0,
               calories: Int = 150,
               datetime: Long = Instant.now.toEpochMilli): Either[Invalidations, Session] =
    val invalidations = Invalidations()
    val either = for
      id          <- id.refineEither[GreaterEqual[0]].left.map(error => invalidations.add("id", error))
      swimmerId   <- swimmerId.refineEither[Greater[0]].left.map(error => invalidations.add("swimmerId", error))
      weight      <- weight.refineEither[Greater[0]].left.map(error => invalidations.add("weight", error))
      weightUnit  <- weightUnit.refineEither[FixedLength[2]].left.map(error => invalidations.add("weightUnit", error))
      laps        <- laps.refineEither[Greater[0]].left.map(error => invalidations.add("laps", error))
      lapDistance <- laps.refineEither[Greater[0]].left.map(error => invalidations.add("lapDistance", error))
      lapUnit     <- lapUnit.refineEither[MinLength[4]].left.map(error => invalidations.add("lapUnit", error))
      style       <- style.refineEither[MinLength[4]].left.map(error => invalidations.add("style", error))
      minutes     <- minutes.refineEither[Greater[0]].left.map(error => invalidations.add("minutes", error))
      seconds     <- minutes.refineEither[GreaterEqual[0]].left.map(error => invalidations.add("seconds", error))
      calories    <- calories.refineEither[GreaterEqual[0]].left.map(error => invalidations.add("calories", error))
      datetime  <- datetime.refineEither[Greater[0]].left.map(error => invalidations.add("datetime", error))
    yield Session(id, swimmerId, weight, weightUnit, laps, lapDistance, lapUnit, style, kickboard, fins, minutes, seconds, calories, datetime)
    invalidations.toEither(either)

enum WeightUnit:
  case lb, kg

object WeightUnit:
  def lbsToKgs(lbs: Int): Double = lbs * 0.454
  def kgsToLbs(kgs: Int): Double = kgs * 2.205
  def toList: List[String] = WeightUnit.values.map(wu => wu.toString).toList

enum LapUnit:
  case feet, meters, yards

object LapUnit:
  def toList: List[String] = LapUnit.values.map(lu => lu.toString).toList

enum Style:
  case breaststroke, backstroke, butterfly, freestyle, kick

object Style:
  def toList: List[String] = Style.values.map(s => s.toString).toList