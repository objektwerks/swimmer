package swimmer.chart

import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, XYChart}
import scalafx.scene.control.{Tab, TabPane}

import swimmer.{Context, Entity, Model}

final case class CalorieXY(xDate: String, yCount: Int)

final class CaloriesChart(context: Context, model: Model) extends TabPane:
  val expendables = model.observableSessions.reverse
  val dateFormat = DateTimeFormatter.ofPattern("M.dd")
  val minDate = Entity.toLocalDateTime( expendables.map(e => e.datetime).min ).format(dateFormat)
  val maxDate = Entity.toLocalDateTime( expendables.map(e => e.datetime).max ).format(dateFormat)

  val tab = new Tab:
    closable = false
    text = context.tabExpendables
    content = buildChart()

  padding = Insets(6)
  tabs = List(tab)

  def buildChart(): LineChart[String, Number] =
    val filtered = expendables.map(e => CalorieXY( Entity.toLocalDateTime(e.datetime).format(dateFormat), e.calories) )
    val (chart, series) = LineChartBuilder.build(context = context,
                                                 xLabel = context.chartMonthDay,
                                                 xMinDate = minDate,
                                                 xMaxDate = maxDate,
                                                 yLabel = context.headerCalories,
                                                 yLowerBound = 1,
                                                 yUpperBound = 1000,
                                                 yTickUnit = 100,
                                                 yValues = filtered.map(exy => exy.yCount))
    filtered foreach { exy =>
      series.data() += XYChart.Data[String, Number](exy.xDate.format(dateFormat), exy.yCount)
    }
    chart.data = series
    LineChartBuilder.addTooltip(chart)
    chart