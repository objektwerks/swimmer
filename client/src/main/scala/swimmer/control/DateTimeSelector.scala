package swimmer.control

import java.time.LocalDateTime

import scalafx.beans.property.ObjectProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label, Spinner}
import scalafx.scene.layout.{GridPane, HBox, Priority, VBox}
import scalafx.stage.Popup

import swimmer.Client

/**
 * ImageButton onclick show Popup with date time selector
 * Popup onclick | escape hide Popup with date time selector
 */
final class DateTimeSelector(localDateTime: LocalDateTime) extends HBox:
  spacing = 3
  padding = Insets(3)

  var value: ObjectProperty[Option[LocalDateTime]] = ObjectProperty(None)

  val localDateTimeLabel = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Date Time:"

  val localDateTimeButton = new Button:
    text = "..."
    disable = false
    onAction = { _ => showPopup() }

  children = List(localDateTimeLabel, localDateTimeButton)
  HBox.setHgrow(this, Priority.Always)

  val dateTimePopupView = DateTimePopupView(localDateTime, hidePopup)
  val dateTimePopup = Popup()
  dateTimePopup.getContent.addAll(dateTimePopupView)

  def showPopup(): Unit = dateTimePopup.show(Client.stage)

  def hidePopup(popupLocalDateTime: LocalDateTime): Unit =
    dateTimePopup.hide()
    value.value = Some(popupLocalDateTime)

private final class DateTimePopupView(localDateTime: LocalDateTime,
                                      hidePopupView: (LocalDateTime) => Unit) extends VBox:
  val labelYear = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Year:"

  val labelMonth = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Month:"

  val labelDay = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Day:"

  val labelHour = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Hour:"

  val labelMinute = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Minute:"

  val labelSecond = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Second:"
  
  val yearSpinner = Spinner[Int](min = localDateTime.getYear, max = localDateTime.getYear + 1, initialValue = localDateTime.getYear, amountToStepBy = 1)
  val monthSpinner = Spinner[Int](min = 1, max = 12, initialValue = localDateTime.getMonthValue, amountToStepBy = 1)
  val daySpinner = Spinner[Int](min = 1, max = 31, initialValue = localDateTime.getDayOfMonth, amountToStepBy = 1)
  val hourSpinner = Spinner[Int](min = 0, max = 23, initialValue = localDateTime.getHour, amountToStepBy = 1)
  val minuteSpinner = Spinner[Int](min = 0, max = 59, initialValue = localDateTime.getMinute, amountToStepBy = 1)
  val secondSpinner = Spinner[Int](min = 0, max = 59, initialValue = localDateTime.getSecond, amountToStepBy = 1)

  val controls = List[(Label, Spinner[Int])](
    labelYear -> yearSpinner,
    labelMonth -> monthSpinner,
    labelDay -> daySpinner,
    labelHour -> hourSpinner,
    labelMinute -> minuteSpinner,
    labelSecond -> secondSpinner
  )

  val selector = buildGridPane(controls)

  val closeButton = new Button:
    text = "Close"
    disable = false
    onAction = { _ => hidePopupView( value() ) }

  children = List(selector, closeButton)

  def value(): LocalDateTime = localDateTime

  private def buildGridPane(controls: List[(Label, Spinner[Int])]): GridPane =
    val gridPane = new GridPane:
      hgap = 6
      vgap = 6
      padding = Insets(top = 6, right = 6, bottom = 6, left = 6)
    
    var row = 0
    for ((label, spinner) <- controls)
      gridPane.add(label, columnIndex = 0, rowIndex = row)
      gridPane.add(spinner, columnIndex = 1, rowIndex = row)
      row += 1

    gridPane