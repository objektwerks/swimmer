package swimmer

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

sealed trait Command

object Command:
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

sealed trait License:
  val license: String

final case class Register(emailAddress: String) extends Command
final case class Login(emailAddress: String, pin: String) extends Command

final case class Deactivate(license: String) extends Command with License
final case class Reactivate(license: String) extends Command with License

final case class ListSwimmers(license: String, accountId: Long) extends Command with License
final case class SaveSwimmer(license: String, swimmer: Swimmer) extends Command with License

final case class ListSessions(license: String, swimmerId: Long) extends Command with License
final case class SaveSession(license: String, session: Session) extends Command with License

final case class AddFault(license: String, fault: Fault) extends Command with License