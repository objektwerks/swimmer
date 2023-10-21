package swimmer.dialog

import scalafx.scene.control.{Dialog, TabPane}

import swimmer.{Client, Context, Model}
import swimmer.chart.{CaloriesChart, DistanceChart, WeightChart}

class ChartDialog(context: Context, model: Model) extends Dialog:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogCharts

  val tabPane = new TabPane:
    tabs = List(
      DistanceChart(context, model),
      CaloriesChart(context, model),
      WeightChart(context, model)
    )