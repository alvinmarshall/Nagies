package com.wNagiesEducationalCenterj_9905

import org.junit.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.ZonedDateTime
import java.time.OffsetDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //2019-06-30T20:45:05.439Z
        val time = ZonedDateTime.parse("2019-06-30T20:55:00.466Z[UTC]")
        val new  = ZonedDateTime.now()
        println("new $new time $time isfresh ${time > new}")
        assertEquals(4, 2 + 2)
    }
}
