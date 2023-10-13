package swimmer

import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.{BorderPane, Priority, VBox}

import swimmer.pane.SwimmersPane

final class View(context: Context, model: Model):
  val borderPane = new BorderPane:
    prefWidth = context.windowWidth
    prefHeight = context.windowHeight
    padding = Insets(6)

  val swimmersPane = SwimmersPane(context, model)
  VBox.setVgrow(swimmersPane, Priority.Always)

  val scene = new Scene:
    root = borderPane
    stylesheets = List("/style.css")