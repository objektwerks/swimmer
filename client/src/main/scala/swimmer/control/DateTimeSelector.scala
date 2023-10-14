package swimmer.control

import java.time.LocalDateTime

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, Spinner}
import scalafx.scene.layout.{HBox, Priority}

final class DateTimeSelector(localDateTime: LocalDateTime) extends HBox:
  spacing = 3
  padding = Insets(3)

  val labelHour = new Label:
    alignment = Pos.CENTER_LEFT
    text = "H:"

  val labelMinute = new Label:
    alignment = Pos.CENTER_LEFT
    text = "M:"
  
  val hourSpinner = Spinner[Int](min = 0, max = 23, initialValue = localDateTime.getHour, amountToStepBy = 1)
  val minuteSpinner = Spinner[Int](min = 0, max = 59, initialValue = localDateTime.getMinute, amountToStepBy = 1)

  children = List(labelHour, hourSpinner, labelMinute, minuteSpinner)
  HBox.setHgrow(this, Priority.Always)

  def value = localDateTime.withHour( hourSpinner.value.value ).withMinute( minuteSpinner.value.value )