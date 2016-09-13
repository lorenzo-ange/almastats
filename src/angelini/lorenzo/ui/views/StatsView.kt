package angelini.lorenzo.ui.views

import angelini.lorenzo.AlmaStats
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.paint.Color
import tornadofx.View
import tornadofx.data
import tornadofx.linechart
import tornadofx.series

class StatsView(val performances: List<AlmaStats.Performance>, val weightedAvg: Float) : View() {

    class ShowValueNode(value: Int) : StackPane() {
        init {
            setPrefSize(15.0, 15.0)
            val label = createDataThresholdLabel(value)
            children.setAll(label)
            toFront()
        }

        private fun createDataThresholdLabel(value: Int): Label {
            val label = Label("$value")
            label.styleClass.addAll("default-color0", "chart-line-symbol", "chart-series-line")
            label.style = "-fx-font-size: 20; -fx-font-weight: bold;"
            label.textFill = Color.DARKGRAY
            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE)
            return label
        }
    }

    override val root = GridPane()

    init {
        with (root) {
            val columnConstraints = ColumnConstraints()
            columnConstraints.isFillWidth = true
            columnConstraints.hgrow = Priority.ALWAYS
            getColumnConstraints().add(columnConstraints)

            val rowConstraints = RowConstraints()
            rowConstraints.isFillHeight = true
            rowConstraints.vgrow = Priority.ALWAYS
            getRowConstraints().add(rowConstraints)

            fun Float.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

            linechart("Almaesami grades stats\nWeighted AVG: ${weightedAvg.format(2)}", CategoryAxis(), NumberAxis(16.0, 32.0, 1.0)) {
                series("Grades") {
                    performances.forEach {
                        data("${it.subject} (${it.cfus} CFU)", it.grade){
                            node = ShowValueNode(it.grade)
                        }
                    }
                }
            }
        }
    }
}
