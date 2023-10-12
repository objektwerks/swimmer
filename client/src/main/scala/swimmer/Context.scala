package swimmer

import com.typesafe.config.Config

import scala.jdk.CollectionConverters.*
import scalafx.scene.image.{Image, ImageView}

final class Context(config: Config):
  val windowTitle = config.getString("window.title")
  val windowWidth = config.getDouble("window.width")
  val windowHeight = config.getDouble("window.height")

  val url = config.getString("url")

  val errorServer = config.getString("error.server")
  val errorRegister = config.getString("error.register")
  val errorLogin = config.getString("error.login")

  val buttonAdd = config.getString("button.add")
  val buttonEdit = config.getString("button.edit")
  val buttonSave = config.getString("button.save")
  val buttonChart = config.getString("button.chart")
  val buttonFaults = config.getString("button.faults")
  val buttonRegister = config.getString("button.register")
  val buttonLogin = config.getString("button.login")
  val buttonAccount = config.getString("button.account")
  val buttonActivate = config.getString("button.activate")
  val buttonDeactivate = config.getString("button.deactivate")

  val dialogRegisterLogin = config.getString("dialog.registerLogin")
  val dialogAccount = config.getString("dialog.account")
  val dialogSwimmer = config.getString("dialog.swimmer")
  val dialogSession = config.getString("dialog.session")
  val dialogFaults = config.getString("dialog.faults")

  val headerOccurred = config.getString("header.occurred")
  val headerFault = config.getString("header.fault")

  val labelEmailAddress = config.getString("label.emailAddress")
  val labelPin = config.getString("label.pin")
  val labelActivated = config.getString("label.activated")
  val labelDeactivated = config.getString("label.deactivated")
  val labelSwimmers = config.getString("label.swimmers")
  val labelSessions = config.getString("label.sessions")
  val labelLicense = config.getString("label.license")
  val labelName = config.getString("label.name")
  val labelWeight = config.getString("label.weight")
  val labelWeightUnit = config.getString("label.weightUnit")
  val labelLaps = config.getString("label.laps")
  val labelLapDistance = config.getString("label.lapDistance")
  val labelLapUnit = config.getString("label.lapUnit")
  val labelStyle = config.getString("label.style")
  val labelKickboard = config.getString("label.kickboard")
  val labelFins = config.getString("label.fins")
  val labelMinutes = config.getString("label.minutes")
  val labelSeconds = config.getString("label.seconds")

  val tabSwimmers = config.getString("tab.swimmers")
  val tabSessions = config.getString("tab.sessions")

  def logoImage = loadImageView("/image/logo.png")
  def addImage = loadImageView("/image/add.png")
  def editImage = loadImageView("/image/edit.png")
  def chartImage = loadImageView("/image/chart.png")
  def faultsImage = loadImageView("/image/faults.png")
  def accountImage = loadImageView("/image/account.png")

  def logo = new Image(Image.getClass.getResourceAsStream("/image/logo.png"))

  private def loadImageView(path: String): ImageView = new ImageView:
    image = new Image(Image.getClass.getResourceAsStream(path))
    fitHeight = 25
    fitWidth = 25
    preserveRatio = true
    smooth = true