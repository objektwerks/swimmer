package swimmer

import ox.supervised
import ox.resilience.retry
import ox.scheduling.Schedule

import scala.concurrent.duration.*
import scala.util.Try
import scala.util.control.NonFatal

import Validator.*

final class Dispatcher(store: Store, emailer: Emailer):
  def dispatch(command: Command): Event =
    command.isValid match
      case false => addFault( Fault(s"Invalid command: $command") )
      case true =>
        isAuthorized(command) match
          case Unauthorized(cause) => addFault( Fault(cause) )
          case Authorized =>
            command match
              case Register(emailAddress)     => register(emailAddress)
              case Login(emailAddress, pin)   => login(emailAddress, pin)
              case Deactivate(license)        => deactivateAccount(license)
              case Reactivate(license)        => reactivateAccount(license)
              case ListSwimmers(_, accountId) => listSwimmers(accountId)
              case SaveSwimmer(_, swimmer)    => saveSwimmer(swimmer)
              case ListSessions(_, swimmerId) => listSessions(swimmerId)
              case SaveSession(_, session)    => saveSession(session)
              case AddFault(_, fault)         => addFault(fault)

  private def isAuthorized(command: Command): Security =
    command match
      case license: License =>
        try
          supervised:
            retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )(
              if store.isAuthorized(license.license) then Authorized
              else Unauthorized(s"Unauthorized: $command")
            )
        catch
          case NonFatal(error) => Unauthorized(s"Unauthorized: $command, cause: $error")
      case Register(_) | Login(_, _) => Authorized

  private def sendEmail(emailAddress: String, message: String): Boolean =
    val recipients = List(emailAddress)
    emailer.send(recipients, message)

  private def register(emailAddress: String): Event =
    try
      supervised:
        val account = Account(emailAddress = emailAddress)
        val message = s"Your new pin is: ${account.pin}\n\nWelcome aboard!"
        val result = retry( Schedule.fixedInterval(600.millis).maxRepeats(1) )( sendEmail(account.emailAddress, message) )
        if result then
          Registered( store.register(account) )
        else
          throw IllegalArgumentException("Invalid email address.")
    catch
      case NonFatal(error) => Fault(s"Registration failed for: $emailAddress, because: ${error.getMessage}")

  private def login(emailAddress: String, pin: String): Event =
    Try:
      supervised:
        retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.login(emailAddress, pin) )
    .fold(
      error => Fault("Login failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then LoggedIn(optionalAccount.get)
        else Fault(s"Login failed for email address: $emailAddress and pin: $pin")
    )

  private def deactivateAccount(license: String): Event =
    Try:
      supervised:
        retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.deactivateAccount(license) )
    .fold(
      error => Fault("Deactivate account failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then Deactivated(optionalAccount.get)
        else Fault(s"Deactivate account failed for license: $license")
    )

  private def reactivateAccount(license: String): Event =
    Try:
      supervised:
        retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.reactivateAccount(license) )
    .fold(
      error => Fault("Reactivate account failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then Reactivated(optionalAccount.get)
        else Fault(s"Reactivate account failed for license: $license")
    )

  private def listSwimmers(accountId: Long): Event =
    try
      SwimmersListed(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.listSwimmers(accountId) )
      )
    catch
      case NonFatal(error) => Fault("List swimmers failed:", error)

  private def saveSwimmer(swimmer: Swimmer): Event =
    try
      SwimmerSaved(
        supervised:
          if swimmer.id == 0 then retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addSwimmer(swimmer) )
          else retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.updateSwimmer(swimmer) )
      )
    catch
      case NonFatal(error) => Fault("Save swimmer failed:", error)

  private def listSessions(swimmerId: Long): Event =
    try
      SessionsListed(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.listSessions(swimmerId) )
      )
    catch
      case NonFatal(error) => Fault("List sessions failed:", error)

  private def saveSession(session: Session): Event =
    try
      SessionSaved(
        if session.id == 0 then retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addSession(session) )
        else retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.updateSession(session) )
      )
    catch
      case NonFatal(error) => Fault("Save session failed:", error)

  private def addFault(fault: Fault): Event =
    try
      supervised:
        retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addFault(fault) )
        FaultAdded()
    catch
      case NonFatal(error) => Fault("Add fault failed:", error)