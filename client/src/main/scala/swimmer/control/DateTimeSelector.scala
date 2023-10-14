package swimmer.control

import java.time.LocalDateTime

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, Spinner}
import scalafx.scene.layout.VBox

final class DateTimeSelector(localDateTime: LocalDateTime) extends VBox:
  spacing = 3
  padding = Insets(3)

  val labelYears = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Year:"

  val labelMonths = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Month:"

  val labelDays = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Day:"

  val labelHours = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Hour:"

  val labelMinutes = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Minutes:"

  val labelSeconds = new Label:
    alignment = Pos.CENTER_LEFT
    text = "Seconds:"
  
  val hoursSpinner = Spinner[Int](min = 0, max = 23, initialValue = localDateTime.getHour, amountToStepBy = 1)
  val minutesSpinner = Spinner[Int](min = 0, max = 59, initialValue = localDateTime.getMinute, amountToStepBy = 1)
  val secondsSpinner = Spinner[Int](min = 0, max = 59, initialValue = localDateTime.getMinute, amountToStepBy = 1)

  children = List()