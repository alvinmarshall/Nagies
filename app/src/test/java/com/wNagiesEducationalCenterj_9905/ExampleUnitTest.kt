package com.wNagiesEducationalCenterj_9905

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        val myList = mutableListOf<Pair<Profile, String>>()
//        for (student in ProfileLabel.getMultiple()) {
//            myList.add(Pair(Profile(student.first, student.second), "server data"))
//        }
//
//        for (i in myList.indices) {
//            println("index $i")
//        }

        val can = getCurrentDateTime()
        println(can.toString("yyyy/MM/dd"))

        assertEquals(4, 2 + 2)
    }
}
