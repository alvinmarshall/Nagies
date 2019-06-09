package com.wNagiesEducationalCenterj_9905

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wNagiesEducationalCenterj_9905.ui.auth.AuthActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity<AuthActivity>()
    }
}
