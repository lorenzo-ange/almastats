package angelini.lorenzo

import angelini.lorenzo.ui.views.LoginView
import javafx.application.Application
import tornadofx.App

class AlmaStatsApp : App(LoginView::class)

fun main(args: Array<String>) = Application.launch(AlmaStatsApp::class.java)
