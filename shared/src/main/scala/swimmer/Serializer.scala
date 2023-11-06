package swimmer

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

object Serializer:
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