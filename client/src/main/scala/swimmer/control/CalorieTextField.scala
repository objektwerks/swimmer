package swimmer.control

import scalafx.scene.layout.HBox

class CalorieTextField(calories: String) extends HBox:
  val caloriesTextField = new IntTextField:
    text = calories