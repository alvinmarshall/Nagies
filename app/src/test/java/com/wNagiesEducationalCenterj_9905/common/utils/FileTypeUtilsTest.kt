package com.wNagiesEducationalCenterj_9905.common.utils

import org.junit.Test

import org.junit.Assert.*

class FileTypeUtilsTest {

    @Test
    fun getPDFType() {
        val path = "/storage/public/document/filename.pdf"
        val actual = FileTypeUtils.getType(path)
        val expected = "application/pdf"
        assertEquals("get file format", expected, actual)
    }

    @Test
    fun getImageType() {
        val path = "/storage/public/document/filename.jpg"
        val actual = FileTypeUtils.getType(path)
        val expected = "image/jpeg"
        assertEquals("get file format", expected, actual)
    }

    @Test
    fun getOtherType() {
        val path = "/storage/public/document/filename.other"
        val actual = FileTypeUtils.getType(path)
        val expected = "*/*"
        assertEquals("get file format", expected, actual)
    }
}