package swimmer

import com.typesafe.scalalogging.LazyLogging

import ox.supervised

import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.ObjectProperty

import Fault.given

final class Model(fetcher: Fetcher) extends LazyLogging:
  def assertInFxThread(message: String, suffix: String = " should be in fx thread!"): Unit =
    require(Platform.isFxApplicationThread, message + suffix)
  def assertNotInFxThread(message: String, suffix: String = " should not be in fx thread!"): Unit =
    require(!Platform.isFxApplicationThread, message + suffix)

  val registered = ObjectProperty[Boolean](true)
  val loggedin = ObjectProperty[Boolean](true)

  val selectedSwimmerId = ObjectProperty[Long](0)
  val selectedSessionId = ObjectProperty[Long](0)

  selectedSwimmerId.onChange { (_, _, newSwimmerId) =>
    sessions(newSwimmerId)
  }

  val objectAccount = ObjectProperty[Account](Account.empty)
  val observableSwimmers = ObservableBuffer[Swimmer]()
  val observableSessions = ObservableBuffer[Session]()
  val observableFaults = ObservableBuffer[Fault]()

  def onFetchFault(source: String, fault: Fault): Unit =
    val cause = s"$source - $fault"
    logger.error("*** cause: {}", cause)
    observableFaults += fault.copy(cause = cause)

  def onFetchFault(source: String, entity: Entity, fault: Fault): Unit =
    val cause = s"$source - $entity - $fault"
    logger.error("*** cause: {}", cause)
    observableFaults += fault.copy(cause = cause)

  def add(fault: Fault): Unit =
    supervised:
      fetcher.fetch(
        AddFault(objectAccount.get.license, fault),
        (event: Event) => event match
          case fault @ Fault(cause, _) => onFetchFault("add fault", fault)
          case FaultAdded() =>
            observableFaults += fault
            observableFaults.sort()
          case _ => ()
      )

  def register(register: Register): Unit =
    fetcher.fetch(
      register,
      (event: Event) => event match
        case _ @ Fault(_, _) => registered.set(false)
        case Registered(account) => objectAccount.set(account)
        case _ => ()
    )

  def login(login: Login): Unit =
    fetcher.fetch(
      login,
      (event: Event) => event match
        case _ @ Fault(_, _) => loggedin.set(false)
        case LoggedIn(account) =>
          objectAccount.set(account)
          swimmers()
        case _ => ()
    )

  def deactivate(deactivate: Deactivate): Unit =
    fetcher.fetch(
      deactivate,
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("deactivate", fault)
        case Deactivated(account) => objectAccount.set(account)
        case _ => ()
    )

  def reactivate(reactivate: Reactivate): Unit =
    fetcher.fetch(
      reactivate,
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("reactivate", fault)
        case Reactivated(account) => objectAccount.set(account)
        case _ => ()
    )

  def swimmers(): Unit =
    supervised:
      fetcher.fetch(
        ListSwimmers(objectAccount.get.license, objectAccount.get.id),
        (event: Event) => event match
          case fault @ Fault(_, _) => onFetchFault("swimmers", fault)
          case SwimmersListed(swimmers) =>
            assertNotInFxThread("list swimmers")
            observableSwimmers.clear()
            observableSwimmers ++= swimmers
          case _ => ()
      )

  def add(swimmer: Swimmer)(runLast: => Unit): Unit =
    fetcher.fetch(
      SaveSwimmer(objectAccount.get.license, swimmer),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("add swimmer", swimmer, fault)
        case SwimmerSaved(id) =>
          assertNotInFxThread(s"add swimmer: $swimmer")
          observableSwimmers.insert(0, swimmer.copy(id = id))
          observableSwimmers.sort()
          selectedSwimmerId.set(id)
          logger.info(s"Added swimmer: $swimmer")
          runLast
        case _ => ()
    )

  def update(selectedIndex: Int, swimmer: Swimmer)(runLast: => Unit): Unit =
    fetcher.fetch(
      SaveSwimmer(objectAccount.get.license, swimmer),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("update swimmer", swimmer, fault)
        case SwimmerSaved(id) =>
          assertNotInFxThread(s"update swimmer from: $selectedIndex to: $swimmer")
          if selectedIndex > -1 then
            observableSwimmers.update(selectedIndex, swimmer)      
            logger.info(s"Updated swimmer from: $selectedIndex to: $swimmer")
            runLast
          else
            logger.error(s"Update of swimmer from: $selectedIndex to: $swimmer failed due to invalid index: $selectedIndex")
        case _ => ()
    )

  def sessions(swimmerId: Long): Unit =
    fetcher.fetch(
      ListSessions(objectAccount.get.license, swimmerId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("sessions", fault)
        case SessionsListed(sessions) =>
          assertNotInFxThread("list sessions")
          observableSessions.clear()
          observableSessions ++= sessions
        case _ => ()
    )

  def add(session: Session)(runLast: => Unit): Unit =
    fetcher.fetch(
      SaveSession(objectAccount.get.license, session),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("add session", session, fault)
        case SessionSaved(id) =>
          assertNotInFxThread(s"add session: $session")
          observableSessions.insert(0, session.copy(id = id))
          observableSessions.sort()
          selectedSessionId.set(id)
          logger.info(s"Added session: $session")
          runLast
        case _ => ()
    )

  def update(selectedIndex: Int, session: Session)(runLast: => Unit): Unit =
    fetcher.fetch(
      SaveSession(objectAccount.get.license, session),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("update session", session, fault)
        case SessionSaved(id) =>
          assertNotInFxThread(s"update session from: $selectedIndex to: $session")
          if selectedIndex > -1 then
            observableSessions.update(selectedIndex, session)      
            logger.info(s"Updated session from: $selectedIndex to: $session")
            runLast
          else
            logger.error(s"Update of session from: $selectedIndex to: $session failed due to invalid index: $selectedIndex")
        case _ => ()
    )