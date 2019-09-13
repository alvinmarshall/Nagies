package com.wNagiesEducationalCenterj_9905.ui.teacher

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseActivity
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import com.wNagiesEducationalCenterj_9905.ui.settings.SettingsActivity
import com.wNagiesEducationalCenterj_9905.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.nav_header_teacher_navigation.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity
import timber.log.Timber

class TeacherNavigationActivity : BaseActivity() {
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var sharedViewModel: SharedViewModel
    private var fetchMessage: Boolean? = false
    private var fetchComplaint: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.fcm_topic_teacher)).addOnCompleteListener {
            if (!it.isSuccessful) {
                Timber.i("Task Failed")
                return@addOnCompleteListener
            }
            Timber.i("incoming teachers topic")
        }
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.fcm_topic_parent))
        setupNavigation()
        if (intent.hasExtra(USER_INFO)) {
            setUserInfo(intent)
        }

        if (intent.hasExtra(NOTIFICATION_EXTRA_COMPLAINT)) {
            fetchComplaint = intent.extras?.getBoolean(NOTIFICATION_EXTRA_COMPLAINT)
        }
        if (intent.hasExtra(NOTIFICATION_EXTRA_MESSAGE)) {
            fetchMessage = intent.extras?.getBoolean(NOTIFICATION_EXTRA_MESSAGE)
        }

        configureSharedViewModel()

    }

    private fun configureSharedViewModel() {
        sharedViewModel = ViewModelProviders.of(this)[SharedViewModel::class.java]
        sharedViewModel.fetchMessage.value = fetchMessage
        sharedViewModel.fetchComplaint.value = fetchComplaint
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.fragment_socket)
        setupActionBarWithNavController(navController, drawerLayout)
        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
        NavigationUI.setupWithNavController(navView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity<SettingsActivity>()
                true
            }
            R.id.action_logout -> {
                preferenceProvider.setUserLogin(false, null)
                startActivity(intentFor<RoleActivity>().newTask().clearTask())
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUserInfo(intent: Intent?) {
        val bundle = intent?.extras?.getStringArrayList(USER_INFO)
        val photo = bundle?.get(1)
        val id = bundle?.get(0)
        Timber.i("user info : $photo")
        val title = "Teacher: $id"
        navView.getHeaderView(0).nav_header_title.text = title

        GlideApp.with(this).load(photo)
            .placeholder(R.drawable.parent)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(navView.getHeaderView(0).img_sidebar)
    }

}
