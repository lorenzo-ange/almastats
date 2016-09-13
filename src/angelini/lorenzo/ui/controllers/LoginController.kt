package angelini.lorenzo.ui.controllers

import angelini.lorenzo.AlmaStats
import angelini.lorenzo.ui.views.LoginView
import angelini.lorenzo.ui.views.StatsView
import javafx.application.Platform
import tornadofx.FX
import kotlin.concurrent.thread

class LoginController(val loginView: LoginView) {
    fun login(username: String, password: String ) {

        loginView.startLoading()

        thread() {
            val performances = AlmaStats.parsePerformances(username, password)
            if (performances.isEmpty()) {
                Platform.runLater {
                    loginView.stopLoading()
                    loginView.shakeStage()
                }
            }
            else {
                val weightedAvg = AlmaStats.calcWeightedAvg(performances)
                val statsView = StatsView(performances, weightedAvg)
                Platform.runLater {
                    loginView.title = "AlmaStats"
                    FX.primaryStage.isMaximized = true
                    FX.primaryStage.scene.root = statsView.root
                }
            }
        }

    }
}