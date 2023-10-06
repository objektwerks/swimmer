package swimmer

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

object Serializer:
  given JsonValueCodec[Entity] = JsonCodecMaker.make[Entity]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Account] = JsonCodecMaker.make[Account]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Swimmer] = JsonCodecMaker.make[Swimmer]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Session] = JsonCodecMaker.make[Session]( CodecMakerConfig.withDiscriminatorFieldName(None) )

  given JsonValueCodec[Command] = JsonCodecMaker.make[Command]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[License] = JsonCodecMaker.make[License]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Register] = JsonCodecMaker.make[Register]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Login] = JsonCodecMaker.make[Login]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Deactivate] = JsonCodecMaker.make[Deactivate]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Reactivate] = JsonCodecMaker.make[Reactivate]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ListSwimmers] = JsonCodecMaker.make[ListSwimmers]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[SaveSwimmer] = JsonCodecMaker.make[SaveSwimmer]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ListSessions] = JsonCodecMaker.make[ListSessions]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[SaveSession] = JsonCodecMaker.make[SaveSession]( CodecMakerConfig.withDiscriminatorFieldName(None) )

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