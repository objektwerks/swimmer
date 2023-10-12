package swimmer.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, ComboBox, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import swimmer.{Client, Context, Session}
import swimmer.control.IntTextField
import swimmer.WeightUnit

final class PoolDialog(context: Context, session: Session) extends Dialog[Session]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogSession

  val weightTextField = new IntTextField:
    text = session.weight.toString
  
  val unitComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( WeightUnit.values )
  	value = session.weightUnit.toString
  unitComboBox.prefWidth = 200

  val controls = List[(String, Region)](
    context.labelName -> nameTextField,
    context.labelVolume -> volumeTextField,
    context.labelUnit -> unitComboBox
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton => {
    if dialogButton == saveButtonType then
      pool.copy(
        name = nameTextField.text.value,
        volume = volumeTextField.text.value.toIntOption.getOrElse(pool.volume),
        unit = unitComboBox.value.value
      )
    else null
  }