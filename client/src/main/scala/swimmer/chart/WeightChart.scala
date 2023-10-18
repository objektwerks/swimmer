package swimmer.chart

import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.scene.chart.{LineChart, XYChart}
import scalafx.scene.control.Tab

import swimmer.{Context, Entity, Model}

final case class WeightXY(xDate: String, yCount: Int)

final class WeightChart(context: Context, model: Model) extends Tab:
  val weights = model.observableSessions.reverse
  val dateFormat = DateTimeFormatter.ofPattern("M.dd")
  val minDate = Entity.toLocalDateTime( weights.map(e => e.datetime).min ).format(dateFormat)
  val maxDate = Entity.toLocalDateTime( weights.map(e => e.datetime).max ).format(dateFormat)

  closable = false
  text = context.tabWeight
  content = buildChart()

  def buildChart(): LineChart[String, Number] =
    val filtered = weights.map(e => WeightXY( Entity.toLocalDateTime(e.datetime).format(dateFormat), e.weight) )
    val (chart, series) = LineChartBuilder.build(context = context,
                                                 xLabel = context.chartMonthDay,
                                                 xMinDate = minDate,
                                                 xMaxDate = maxDate,
                                                 yLabel = context.tabWeight,
                                                 yLowerBound = 50,
                                                 yUpperBound = 400,
                                                 yTickUnit = 50,
                                                 yValues = filtered.map(exy => exy.yCount))
    filtered foreach { exy =>
      series.data() += XYChart.Data[String, Number](exy.xDate.format(dateFormat), exy.yCount)
    }
    chart.data = series
    LineChartBuilder.addTooltip(chart)
    chart