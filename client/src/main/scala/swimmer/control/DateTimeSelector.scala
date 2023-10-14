package swimmer.control

import java.time.LocalDateTime

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, Spinner}
import scalafx.scene.layout.VBox

final class DateTimeSelector(localDateTime: LocalDateTime) extends VBox:
  spacing = 3
  padding = Insets(3)

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

  val labelMinutes = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Minute:"

  val labelSeconds = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Second:"
  
  val yearSpinner = Spinner[Int](min = localDateTime.getYear, max = localDateTime.getYear + 1, initialValue = localDateTime.getYear, amountToStepBy = 1)
  val monthSpinner = Spinner[Int](min = 1, max = 12, initialValue = localDateTime.getMonthValue, amountToStepBy = 1)
  val daySpinner = Spinner[Int](min = 1, max = 31, initialValue = localDateTime.getDayOfMonth, amountToStepBy = 1)
  val hourSpinner = Spinner[Int](min = 0, max = 23, initialValue = localDateTime.getHour, amountToStepBy = 1)
  val minuteSpinner = Spinner[Int](min = 0, max = 59, initialValue = localDateTime.getMinute, amountToStepBy = 1)
  val secondSpinner = Spinner[Int](min = 0, max = 59, initialValue = localDateTime.getSecond, amountToStepBy = 1)

  children = List()