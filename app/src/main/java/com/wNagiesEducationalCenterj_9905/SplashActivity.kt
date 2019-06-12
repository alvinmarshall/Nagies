package com.wNagiesEducationalCenterj_9905

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity<RoleActivity>()
        finish()
    }
}
