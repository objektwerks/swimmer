package swimmer

sealed trait Command

sealed trait License:
  val license: String

final case class Register(emailAddress: String) extends Command
final case class Login(emailAddress: String, pin: String) extends Command

final case class Deactivate(license: String) extends Command with License
final case class Reactivate(license: String) extends Command with License

final case class ListSwimmers(license: String) extends Command with License
final case class SaveSwimmer(license: String, swimmer: Swimmer) extends Command with License

final case class ListSessions(license: String, swimmerId: Long) extends Command with License
final case class SaveSession(license: String, session: Session) extends Command with License

final case class AddFault(license: String, fault: Fault) extends Command with License