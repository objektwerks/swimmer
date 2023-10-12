package swimmer.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, ComboBox, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import swimmer.{Client, Context, Swimmer}

final class SwimmerDialog(context: Context, swimmer: Swimmer) extends Dialog[Swimmer]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogSwimmer

  val licenseTextField = new TextField:
    text = swimmer.license

  val nameTextField = new TextField:
    text = swimmer.name

  val controls = List[(String, Region)](
    context.labelLicense -> licenseTextField,
    context.labelName -> nameTextField
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton =>
    if dialogButton == saveButtonType then
      swimmer.copy(
        license = licenseTextField.text.value,
        name = nameTextField.text.value
      )
    else null