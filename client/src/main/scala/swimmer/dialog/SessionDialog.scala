package swimmer.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, CheckBox, ComboBox, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import swimmer.{Client, Context, Session}
import swimmer.control.IntTextField
import swimmer.{LapUnit, Style, WeightUnit}

final class PoolDialog(context: Context, session: Session) extends Dialog[Session]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogSession

  val weightTextField = new IntTextField:
    text = session.weight.toString
  
  val weightUnitComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( WeightUnit.toList )
  	value = session.weightUnit.toString
  weightUnitComboBox.prefWidth = 200

  val lapsTextField = new IntTextField:
    text = session.laps.toString

  val lapDistanceTextField = new IntTextField:
    text = session.lapDistance.toString

  val lapUnitComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( LapUnit.toList )
  	value = session.lapUnit.toString
  lapUnitComboBox.prefWidth = 200

  val styleComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( Style.toList )
  	value = session.style.toString
  styleComboBox.prefWidth = 300

  val kickboardCheckBox = new CheckBox:
    selected = session.kickboard

  val finsCheckBox = new CheckBox:
    selected = session.fins

  val controls = List[(String, Region)](
    context.labelWeightUnit  -> weightUnitTextField,
    context.labelWeightUnit  -> weightUnitComboBox,
    context.labelLaps        -> lapsTextField,
    context.labelLapDistance -> lapDistanceTextField,
    context.labelLapUnit     -> lapUnitComboBox,
    context.labelStyle       -> styleComboBox,
    context.labelKickboard   -> kickboardCheckBox,
    context.labelFins        -> finsCheckBox,
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton =>
    if dialogButton == saveButtonType then
      session.copy(
        weight = weightTextField.int(session.weight),
        weightUnit = weightUnitComboBox.value.value,
        laps = lapsTextField.int(session.laps),
        lapDistance = lapDistanceTextField.int(session.lapDistance),
        lapUnit = lapUnitComboBox.value.value,
        style = styleComboBox.value.value,
        kickboard = kickboardCheckBox.selected.value,
        fins = finsCheckBox.selected.value,
      )
    else null