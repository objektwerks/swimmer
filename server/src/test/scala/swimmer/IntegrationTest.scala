package swimmer

import com.typesafe.config.ConfigFactory

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.*
import scala.sys.process.Process

import Validator.*

final class IntegrationTest extends AnyFunSuite with Matchers:
  val exitCode = Process("psql -d swimmer -f ddl.sql").run().exitValue()
  exitCode shouldBe 0

  val config = ConfigFactory.load("test.conf")

  val store = Store(config, Store.cache(minSize = 1, maxSize = 1, expireAfter = 1.hour))
  val emailer = Emailer(config)
  val dispatcher = Dispatcher(store, emailer)

  var testAccount = Account()
  var testSwimmer = Swimmer(license = testAccount.license, name = "Fred")
  var testSession = Session(swimmerId = 0)

  test("integration"):
    register
    login

    deactivate
    reactivate

    addSwimmer
    updateSwimmer
    listSwimmers

    addSession
    updateSession
    listSessions

    fault

  def register: Unit =
    val register = Register(config.getString("email.sender"))
    dispatcher.dispatch(register) match
      case Registered(account) =>
        assert( account.isActivated )
        testAccount = account
      case fault => fail(s"Invalid registered event: $fault")
    
  def login: Unit =
    val login = Login(testAccount.emailAddress, testAccount.pin)
    dispatcher.dispatch(login) match
      case LoggedIn(account) => account shouldBe testAccount
      case fault => fail(s"Invalid loggedin event: $fault")

  def deactivate: Unit =
    val deactivate = Deactivate(testAccount.license)
    dispatcher.dispatch(deactivate) match
      case Deactivated(account) => assert( account.isDeactivated )
      case fault => fail(s"Invalid deactivated event: $fault")

  def reactivate: Unit =
    val reactivate = Reactivate(testAccount.license)
    dispatcher.dispatch(reactivate) match
      case Reactivated(account) => assert( account.isActivated )
      case fault => fail(s"Invalid reactivated event: $fault")

  def addSwimmer: Unit =
    val saveSwimmer = SaveSwimmer(testAccount.license, testSwimmer)
    dispatcher.dispatch(saveSwimmer) match
      case SwimmerSaved(id) =>
        id should not be 0
        testSwimmer = testSwimmer.copy(id = id)
        testSession = testSession.copy(swimmerId = id)
      case fault => fail(s"Invalid swimmer saved event: $fault")

  def updateSwimmer: Unit =
    testSwimmer = testSwimmer.copy(name = "Fred Flintstone")
    val saveSwimmer = SaveSwimmer(testAccount.license, testSwimmer)
    dispatcher.dispatch(saveSwimmer) match
      case SwimmerSaved(id) => id shouldBe testSwimmer.id
      case fault => fail(s"Invalid swimmer saved event: $fault")
    
  def listSwimmers: Unit =
    val listPools = ListPools(testAccount.license)
    dispatcher.dispatch(listPools) match
      case PoolsListed(pools) =>
        pools.length shouldBe 1
        pools.head shouldBe testPool
      case fault => fail(s"Invalid pools listed event: $fault")

  def addSession: Unit =
    val saveCleaning = SaveCleaning(testAccount.license, testCleaning)
    dispatcher.dispatch(saveCleaning) match
      case CleaningSaved(id) =>
        id should not be 0
        testCleaning = testCleaning.copy(id = id)
      case fault => fail(s"Invalid cleaning saved event: $fault")

  def updateSession: Unit =
    testCleaning = testCleaning.copy(vacuum = true)
    val saveCleaning = SaveCleaning(testAccount.license, testCleaning)
    dispatcher.dispatch(saveCleaning) match
      case CleaningSaved(id) =>
        id shouldBe testCleaning.id
      case fault => fail(s"Invalid cleaning saved event: $fault")

  def listSessions: Unit =
    val listCleanings = ListCleanings(testAccount.license, testPool.id)
    dispatcher.dispatch(listCleanings) match
      case CleaningsListed(cleanings) =>
        cleanings.length shouldBe 1
        cleanings.head shouldBe testCleaning
      case fault => fail(s"Invalid cleanings listed event: $fault")

  def fault: Unit =
    val fault = Fault("error message")
    store.addFault(fault)
    store.listFaults().length shouldBe 1