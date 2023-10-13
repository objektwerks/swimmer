package swimmer

import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{BorderPane, Priority, VBox}

import swimmer.pane.{SessionsPane, SwimmersPane}

final class View(context: Context, model: Model):
  val borderPane = new BorderPane:
    prefWidth = context.windowWidth
    prefHeight = context.windowHeight
    padding = Insets(6)

  val swimmersPane = SwimmersPane(context, model)
  VBox.setVgrow(swimmersPane, Priority.Always)

  val sessionsPane = SessionsPane(context, model)
  VBox.setVgrow(sessionsPane, Priority.Always)

  val splitPane = new SplitPane {
    orientation = Orientation.Horizontal
    items.addAll(swimmersPane, sessionsPane)
  }
  splitPane.setDividerPositions(0.20, 0.80)
  VBox.setVgrow(splitPane, Priority.Always)

  val scene = new Scene:
    root = borderPane
    stylesheets = List("/style.css")