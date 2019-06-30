package com.wNagiesEducationalCenterj_9905.common.utils

import com.wNagiesEducationalCenterj_9905.common.SERVER_URL
import org.junit.Test

import org.junit.Assert.*

class ImagePathUtilTest {

    @Test
    fun setImageUrlPath() {
        val imagePath = "../students/uploads/filename.pdf"
        val actual = ImagePathUtil.setCorrectPath(imagePath)
        val expect = "$SERVER_URL/students/uploads/filename.pdf"
        assertEquals(expect,actual)
    }
}