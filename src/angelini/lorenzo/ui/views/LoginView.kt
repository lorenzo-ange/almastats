package angelini.lorenzo.ui.views

import angelini.lorenzo.ui.controllers.LoginController
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.util.Duration
import tornadofx.*

class LoginView : View() {
    override val root = GridPane()

    val controller = LoginController(this)

    var username: TextField by singleAssign()
    var password: PasswordField by singleAssign()
    var loginButton: Button by singleAssign()

    init {
        title = "Please log in"

        with (root) {
            padding = Insets(15.0)
            vgap = 7.0
            hgap = 10.0

            row("Username") {
                username = textfield()
            }

            row("Password") {
                password = passwordfield()
            }

            row {
                loginButton = button("Login") {
                    isDefaultButton = true

                    setOnAction {
                        controller.login(username.text, password.text)
                    }
                }
            }

        }
    }

    fun startLoading() {
        title = "Fetching data..."
        username.isDisable = true
        password.isDisable = true
        loginButton.isDisable = true
        loginButton.text = "⌛"
    }

    fun stopLoading() {
        title = "Please log in"
        username.isDisable = false
        password.isDisable = false
        loginButton.isDisable = false
        loginButton.text = "Login"
    }

    fun shakeStage() {
        var x = 0
        var y = 0
        val cycleCount = 10
        val move = 10
        val keyframeDuration = Duration.seconds(0.04)

        val stage = FX.primaryStage

        val timelineX = Timeline(KeyFrame(keyframeDuration, EventHandler {
            if (x == 0) {
                stage.x = stage.x + move
                x = 1
            } else {
                stage.x = stage.x - move
                x = 0
            }
        }))

        timelineX.cycleCount = cycleCount
        timelineX.isAutoReverse = false

        val timelineY = Timeline(KeyFrame(keyframeDuration, EventHandler {
            if (y == 0) {
                stage.y = stage.y + move
                y = 1;
            } else {
                stage.y = stage.y - move
                y = 0;
            }
        }))

        timelineY.cycleCount = cycleCount;
        timelineY.isAutoReverse = false;

        timelineX.play()
        timelineY.play();
    }
}