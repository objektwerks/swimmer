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
  val buttonConverter = config.getString("button.converter")
  val buttonRegister = config.getString("button.register")
  val buttonLogin = config.getString("button.login")
  val buttonAccount = config.getString("button.account")
  val buttonActivate = config.getString("button.activate")
  val buttonDeactivate = config.getString("button.deactivate")

  val chartCleanings = config.getString("chart.cleanings")
  val chartChemicals = config.getString("chart.chemicals")
  val chartMeasurements = config.getString("chart.measurements")
  val chartMonthDay = config.getString("chart.monthDay")
  val chartMin = config.getString("chart.min")
  val chartMax = config.getString("chart.max")
  val chartAvg = config.getString("chart.avg")
  val chartYCleanings = config.getString("chart.yCleanings")
  val chartTotalChlorine = config.getString("chart.totalChlorine")
  val chartFreeChlorine = config.getString("chart.freeChlorine")
  val chartCombinedChlorine = config.getString("chart.combinedChlorine")
  val chartPh = config.getString("chart.ph")
  val chartCalciumHardness = config.getString("chart.calciumHardness")
  val chartTotalAlkalinity = config.getString("chart.totalAlkalinity")
  val chartCyanuricAcid = config.getString("chart.cyanuricAcid")
  val chartTotalBromine = config.getString("chart.totalBromine")
  val chartSalt = config.getString("chart.salt")
  val chartLiquidChlorine = config.getString("chart.liquidChlorine")
  val chartTrichlor = config.getString("chart.trichlor")
  val chartDichlor = config.getString("chart.dichlor")
  val chartCalciumHypochlorite = config.getString("chart.calciumHypochlorite")
  val chartStabilizer = config.getString("chart.stabilizer")
  val chartAlgaecide = config.getString("chart.algaecide")
  val chartMuriaticAcid = config.getString("chart.muriaticAcid")
  val chartGranularSalt = config.getString("chart.granularSalt")

  val columnYes = config.getString("column.yes")
  val columnNo = config.getString("column.no")

  val converterGallons = config.getString("converter.gallons")
  val converterLiters = config.getString("converter.liters")
  val converterPounds = config.getString("converter.pounds")
  val converterKilograms = config.getString("converter.kilograms")

  val dialogRegisterLogin = config.getString("dialog.registerLogin")
  val dialogAccount = config.getString("dialog.account")
  val dialogSwimmer = config.getString("dialog.swimmer")
  val dialogSession = config.getString("dialog.session")
  val dialogFaults = config.getString("dialog.faults")

  val headerOccurred = config.getString("header.occurred")
  val headerFault = config.getString("header.fault")

  val labelLicense = config.getString("label.license")
  val labelEmailAddress = config.getString("label.emailAddress")
  val labelPin = config.getString("label.pin")
  val labelActivated = config.getString("label.activated")
  val labelDeactivated = config.getString("label.deactivated")
  val labelSwimmers = config.getString("label.swimmers")
  val labelSessions = config.getString("label.sessions")

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