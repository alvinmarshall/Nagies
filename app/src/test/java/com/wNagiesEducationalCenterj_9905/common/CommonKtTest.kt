package com.wNagiesEducationalCenterj_9905.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CommonKtTest {

    @Test
    fun getDifferenceInTime_Invalid_Date_IsNull() {
        val minutes = ""
        assertEquals(0, getDifferenceInTime(minutes))
    }

    @Test
    fun getDifferenceInTime_valid_Date_IsNotNull() {
        val minutes = "2019-08-14 11:00:23"
        assertNotEquals(1, getDifferenceInTime(minutes))
    }
}