package com.wNagiesEducationalCenterj_9905.common.utils

import android.app.Activity
import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.wNagiesEducationalCenterj_9905.R
import javax.inject.Inject

class InputValidationProvider @Inject constructor(
    private val context: Context
) {
    fun isEditTextFilled
                (editText: EditText,message:String? = null,isEmail:Boolean = false):Boolean{
        val value = editText.text.toString().trim()
        if (value.isEmpty()){
            editText.error = message?:context.getString(R.string.prompt_empty_field)
            hideKeyboardFrom(editText)
            return false
        }
        if (isEmail){
             return if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
                editText.error = message?:context.getString(R.string.prompt_empty_field)
                 hideKeyboardFrom(editText)
                false
            }else true
        }
        return true
    }

    fun isEditTextFilledMatches
                (editText0:EditText,editText1:EditText,message: String? = null):Boolean{
        val value0 = editText0.text.toString().trim()
        val value1 = editText1.text.toString().trim()
        if (!value0.contentEquals(value1)){
            editText1.error = message?:context.getString(R.string.prompt_password_mismatch)
            hideKeyboardFrom(editText1)
            return false
        }
        return true
    }

    private fun hideKeyboardFrom(view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}