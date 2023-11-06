package swimmer

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

import java.time.{Instant, LocalDate}

import scalafx.beans.property.ObjectProperty

sealed trait Event

object Event:
  given JsonValueCodec[Event] = JsonCodecMaker.make[Event]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Registered] = JsonCodecMaker.make[Registered]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[LoggedIn] = JsonCodecMaker.make[LoggedIn]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Deactivated] = JsonCodecMaker.make[Deactivated]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Reactivated] = JsonCodecMaker.make[Reactivated]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[SwimmersListed] = JsonCodecMaker.make[SwimmersListed]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[SwimmerSaved] = JsonCodecMaker.make[SwimmerSaved]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[SessionsListed] = JsonCodecMaker.make[SessionsListed]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[SessionSaved] = JsonCodecMaker.make[SessionSaved]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Fault] = JsonCodecMaker.make[Fault]( CodecMakerConfig.withDiscriminatorFieldName(None) )  

final case class Authorized(isAuthorized: Boolean) extends Event

final case class Registered(account: Account) extends Event
final case class LoggedIn(account: Account) extends Event

final case class Deactivated(account: Account) extends Event
final case class Reactivated(account: Account) extends Event

final case class SwimmersListed(swimmers: List[Swimmer]) extends Event
final case class SwimmerSaved(id: Long) extends Event

final case class SessionsListed(sessions: List[Session]) extends Event
final case class SessionSaved(id: Long) extends Event

object Fault:
  def apply(message: String, throwable: Throwable): Fault = Fault(s"$message ${throwable.getMessage}")

  given faultOrdering: Ordering[Fault] = Ordering.by[Fault, Long](f => LocalDate.parse(f.occurred).toEpochDay()).reverse

final case class Fault(cause: String, occurred: String = Instant.now.toString) extends Event:
  val causeProperty = ObjectProperty[String](this, "cause", cause)
  val occurredProperty = ObjectProperty[String](this, "occurred", occurred)

final case class FaultAdded() extends Event