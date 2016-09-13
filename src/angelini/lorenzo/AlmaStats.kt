package angelini.lorenzo

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.*

object AlmaStats {
    data class Performance(val subject: String, val cfus: Int, val grade: Int, val praise: Boolean)

    fun calcWeightedAvg(performances: List<Performance>): Float {
        val weightedSum = performances.map { it.grade * it.cfus }.fold(0, { sum, x -> sum + x })
        val totalCfus = performances.map { it.cfus }.fold(0, { sum, x -> sum + x })
        return weightedSum / totalCfus.toFloat()
    }

    fun parsePerformances(username: String, password: String): List<Performance> {
        val examsPage = fetchExamsPage(username, password)
        examsPage ?: return emptyList()

        val examsTable: HtmlTable = examsPage.getFirstByXPath("//table[@class='iceDataTblOutline']")
        return examsTable.rows.map { parsePerformanceTr(it) }.filterNotNull()
    }

    private fun parsePerformanceTr(tr: HtmlTableRow) =
            performanceFromRawData(rawSubject = tr.cells[2].asText(),
                                   rawCfus = tr.cells[4].asText(),
                                   rawGrade = tr.cells[5].asText())

    private fun performanceFromRawData(rawSubject: String, rawCfus: String, rawGrade: String): Performance? {
        if (rawSubject.isNullOrBlank() || rawCfus.isNullOrBlank() || rawGrade.isNullOrBlank()) {
            return null
        }

        try {
            val subject = rawSubject.trim().toLowerCase()
            val cfus = rawCfus.trim().toInt()
            val praise = rawGrade.contains(Regex("\\d+L")) || rawGrade.contains("lode", true)
            val grade = rawGrade.trim().replace(Regex("\\D"), "").toInt()

            return Performance(subject, cfus, grade, praise)
        } catch(e: Throwable) {
            return null
        }
    }

    private fun fetchExamsPage(username: String, password: String): HtmlPage? {
        val webClient = WebClient()
        val loginPage: HtmlPage = webClient.getPage("https://almaesami.unibo.it/almaesami/studenti/home.htm")

        val loginForm: HtmlForm = loginPage.getHtmlElementById("fm1")

        val usernameInput: HtmlTextInput = loginForm.getInputByName("username")
        usernameInput.setValueAttribute(username)

        val passwordInput: HtmlPasswordInput = loginForm.getInputByName("password")
        passwordInput.setValueAttribute(password)

        val loginButton = loginPage.createElement("button")
        loginButton.setAttribute("type", "submit")
        loginForm.appendChild(loginButton)

        val examsPage: HtmlPage = loginButton.click()
        return if(examsPage.webResponse.contentAsString.contains("errorText")) null else examsPage
    }
}