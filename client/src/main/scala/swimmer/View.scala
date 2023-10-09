package swimmer

import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{BorderPane, HBox, Priority, VBox}

import pool.pane.{PoolsPane, TabbedPane}
import pool.dashboard.DashboardPane

final class View(context: Context, model: Model):
  val borderPane = new BorderPane:
    prefWidth = context.windowWidth
    prefHeight = context.windowHeight
    padding = Insets(6)

  val dashboardPane = DashboardPane(context, model)
  HBox.setHgrow(dashboardPane, Priority.Always)

  val poolsPane = PoolsPane(context, model)
  VBox.setVgrow(poolsPane, Priority.Always)

  val tabbedPane = TabbedPane(context, model)
  VBox.setVgrow(tabbedPane, Priority.Always)

  val splitPane = new SplitPane {
    orientation = Orientation.Horizontal
    items.addAll(poolsPane, tabbedPane)
  }
  splitPane.setDividerPositions(0.30, 0.70)
  VBox.setVgrow(splitPane, Priority.Always)

  borderPane.top = dashboardPane
  borderPane.center = splitPane

  val scene = new Scene:
    root = borderPane
    stylesheets = List("/style.css")