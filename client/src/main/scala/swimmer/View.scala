package swimmer

import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{BorderPane, HBox, Priority, VBox}

final class View(context: Context, model: Model):
  val borderPane = new BorderPane:
    prefWidth = context.windowWidth
    prefHeight = context.windowHeight
    padding = Insets(6)

  val scene = new Scene:
    root = borderPane
    stylesheets = List("/style.css")