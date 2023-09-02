package id.gdev.regist

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun removePrefix(){
        val ticketNumber = "GOOGA23757216"
        val scanResult = "57623:757216"
        val resultTicket = ticketNumber.removePrefix("GOOGA23")
        val resultScan = scanResult.split(":").last()
        println(resultTicket)
        println(resultScan)
        println("Is Equal = ${resultTicket == resultScan}")
    }
}