package swimmer.chart

import java.time.format.DateTimeFormatter

import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.Includes.*
import scalafx.scene.chart.{LineChart, XYChart}
import scalafx.scene.control.{ComboBox, Tab}
import scalafx.scene.layout.VBox

import swimmer.{Context, Entity, Model, Style}

final case class DistanceXY(xDate: String, yCount: Int)

final class DistanceChart(context: Context, model: Model) extends Tab:
  val distances = model.observableSessions.reverse
  val dateFormat = DateTimeFormatter.ofPattern("M.dd")
  val minDate = Entity.toLocalDateTime( distances.map(e => e.datetime).min ).format(dateFormat)
  val maxDate = Entity.toLocalDateTime( distances.map(e => e.datetime).max ).format(dateFormat)

  val styleComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( Style.toList )
  	value = Style.freestyle.toString
  styleComboBox.prefWidth = 300
  styleComboBox.onAction = { _ => buildChart() }

  closable = false
  text = context.tabDistance
  content = buildChart()
  content = new VBox { // TODO!
    spacing = 6
    padding = Insets(6)
    children = List(styleComboBox, buildChart())
  }

  def buildChart(): LineChart[String, Number] =
    val filtered = distances.map(e => DistanceXY( Entity.toLocalDateTime(e.datetime).format(dateFormat), e.distance()) )
    val (chart, series) = LineChartBuilder.build(context = context,
                                                 xLabel = context.chartMonthDay,
                                                 xMinDate = minDate,
                                                 xMaxDate = maxDate,
                                                 yLabel = context.tabDistance,
                                                 yLowerBound = 100,
                                                 yUpperBound = 10000,
                                                 yTickUnit = 100,
                                                 yValues = filtered.map(exy => exy.yCount))
    filtered foreach { exy =>
      series.data() += XYChart.Data[String, Number](exy.xDate.format(dateFormat), exy.yCount)
    }
    chart.data = series
    LineChartBuilder.addTooltip(chart)
    chart