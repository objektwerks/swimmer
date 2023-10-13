package swimmer.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import swimmer.{Session, Context, Model}
import swimmer.dialog.SessionDialog

final class SessionsPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val tableView = new TableView[Session]():
    columns ++= List(
      new TableColumn[Session, Int]:
        text = context.headerWeight
        cellValueFactory = _.value.weightProperty
      ,
      new TableColumn[Session, String]:
        text = context.headerWeightUnit
        cellValueFactory = _.value.weightUnitProperty
      ,
      new TableColumn[Session, Int]:
        text = context.headerLaps
        cellValueFactory = _.value.lapsProperty
      ,
      new TableColumn[Session, Int]:
        text = context.headerLapDistance
        cellValueFactory = _.value.lapDistanceProperty
      ,
      new TableColumn[Session, String]:
        text = context.headerLapUnit
        cellValueFactory = _.value.lapUnitProperty
      ,
    )
    items = model.observableSessions

  val addButton = new Button:
    graphic = context.addImage
    text = context.buttonAdd
    disable = true
    onAction = { _ => add() }

  val editButton = new Button:
    graphic = context.editImage
    text = context.buttonEdit
    disable = true
    onAction = { _ => update() }

  val chartButton = new Button:
    graphic = context.chartImage
    text = context.buttonChart
    disable = true
    onAction = { _ => chart() }

  val buttonBar = new HBox:
    spacing = 6
    children = List(addButton, editButton, chartButton)

  model.selectedSessionId.onChange { (_, _, _) =>
    addButton.disable = false
    chartButton.disable = false
  }

  children = List(tableView, buttonBar)
  VBox.setVgrow(tableView, Priority.Always)

  tableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 && tableView.selectionModel().getSelectedItem != null) update()
  }

  tableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  tableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedItem) =>
    // model.update executes a remove and add on items. the remove passes a null selectedItem!
    if selectedItem != null then
      model.selectedSessionId.value = selectedItem.id
      editButton.disable = false
  }

  def add(): Unit =
    SessionDialog(context, Session(swimmerId = model.selectedSwimmerId.value)).showAndWait() match
      case Some(session: Session) => model.add(0, session) {
        tableView.selectionModel().select(0)
      }
      case _ =>

  def update(): Unit =
    val selectedIndex = tableView.selectionModel().getSelectedIndex
    val session = tableView.selectionModel().getSelectedItem.session
    SessionDialog(context, session).showAndWait() match
      case Some(session: Session) => model.update(selectedIndex, session){
        tableView.selectionModel().select(selectedIndex)
      }
      case _ =>

  def chart(): Unit = ??? // SessionsChartDialog(context, model).showAndWait()