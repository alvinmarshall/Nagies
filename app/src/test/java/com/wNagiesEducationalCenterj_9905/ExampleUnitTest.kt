package com.wNagiesEducationalCenterj_9905

import com.wNagiesEducationalCenterj_9905.common.utils.ProfileLabel
import com.wNagiesEducationalCenterj_9905.vo.Profile
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val myList = mutableListOf<Pair<Profile, String>>()
        for (student in ProfileLabel.getMultiple()) {
            myList.add(Pair(Profile(student.first, student.second), "server data"))
        }

        for (i in myList.indices) {
            println("index $i")
        }

        assertEquals(4, 2 + 2)
    }
}
