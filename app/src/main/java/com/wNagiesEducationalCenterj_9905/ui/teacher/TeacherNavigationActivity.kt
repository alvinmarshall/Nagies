package com.wNagiesEducationalCenterj_9905.ui.teacher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseActivity
import com.wNagiesEducationalCenterj_9905.common.*
import com.wNagiesEducationalCenterj_9905.common.utils.NotificationUtils
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import com.wNagiesEducationalCenterj_9905.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.content_teacher_navigation.*
import kotlinx.android.synthetic.main.nav_header_teacher_navigation.view.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity
import timber.log.Timber

class TeacherNavigationActivity : BaseActivity() {
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    private var alertDialog: AlertDialog.Builder? = null
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        alertDialog = AlertDialog.Builder(this)
        snackBar =Snackbar.make(root,"",Snackbar.LENGTH_SHORT)
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        val usr = preferenceProvider.getUserSessionData().name?.split(" ")?.get(0)
        snackBar?.setText("welcome back $usr")?.show()
        setupNavigation()

        firebaseMessageSubscription()


        navigateFromNotificationCenter()
        registerPushNotificationReceiver()
    }

    private fun firebaseMessageSubscription() {
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.fcm_topic_teacher)).addOnCompleteListener {
            if (!it.isSuccessful) {
                Timber.i("Task Failed")
                return@addOnCompleteListener
            }
            Timber.i("incoming teachers topic")
        }
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.fcm_topic_parent))
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

    private fun registerPushNotificationReceiver() {
        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Timber.i("${intent?.action}")
                when (intent?.action) {
                    FOREGROUND_PUSH_NOTIFICATION -> {
                        setFetchData(intent.getStringExtra(FOREGROUND_PUSH_NOTIFICATION_EXTRA))
                    }

                }
            }
        }
    }

    private fun setFetchData(type: String?) {
        val extra = Bundle()
        when (type) {
            NAVIGATION_TO_COMPLAINT -> {
                extra.putBoolean(COMPLAINT_RECEIVE_EXTRA, true)
                showNewMessageNavigationPrompt(
                    R.id.parentComplaintFragment, COMPLAINT_RECEIVE_EXTRA, extra,
                    NAVIGATION_TO_COMPLAINT
                )
            }
            NAVIGATE_TO_DIALOG_RESET_PASSWORD -> {
                showPasswordResetDialog(alertDialog)
            }
            NAVIGATE_TO_ANNOUNCEMENT -> {
                extra.putBoolean(ANNOUNCEMENT_RECEIVE_EXTRA, true)
                showNewMessageNavigationPrompt(
                    R.id.teacherAnnouncementFragment, ANNOUNCEMENT_RECEIVE_EXTRA, extra,
                    NAVIGATE_TO_ANNOUNCEMENT
                )
            }
        }
    }

    private fun showNewMessageNavigationPrompt(location: Int, key: String, extra: Bundle?, navigation: String? = null) {
        val currentFragment = navController.currentDestination?.label
        // don't show dialog if user is in the same location
        currentFragment?.let {
            if (it == navigation) {
                Timber.i("same location so pref set")
                snackBar?.setText("new message received")?.show()
                preferenceProvider.setNotificationCallback(key, true)
                return
            }
        }

        alertDialog?.setTitle("New Message Alert")
        alertDialog?.setMessage("Do want to view message")
        alertDialog?.setPositiveButton("yes") { dialog, _ ->
            dialog.dismiss()
            setNavigation(location, extra)
        }
        alertDialog?.setNegativeButton("cancel") { dialog, _ ->
            preferenceProvider.setNotificationCallback(key, true)
            dialog.dismiss()
        }
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }

    private fun navigateFromNotificationCenter() {
        if (intent.hasExtra(NOTIFICATION_MESSAGE_EXTRAS)) {
            when (intent.getStringExtra(NOTIFICATION_MESSAGE_EXTRAS)) {
                NAVIGATION_TO_COMPLAINT -> {
                    navigateUserToMessagePage(NAVIGATION_TO_COMPLAINT)
                }
                NAVIGATE_TO_DIALOG_RESET_PASSWORD -> {
                    showPasswordResetDialog(alertDialog)
                }
                NAVIGATE_TO_ANNOUNCEMENT -> {
                    navigateUserToMessagePage(NAVIGATE_TO_ANNOUNCEMENT)
                }
            }

        }
    }

    private fun navigateUserToMessagePage(location: String?) {
        Timber.i("navigation $location")
        val extra = Bundle()
        when (location) {
            NAVIGATION_TO_COMPLAINT -> {
                extra.putBoolean(COMPLAINT_RECEIVE_EXTRA, true)
                setNavigation(R.id.parentComplaintFragment, extra)
            }
            NAVIGATE_TO_ANNOUNCEMENT -> {
                extra.putBoolean(ANNOUNCEMENT_RECEIVE_EXTRA, true)
                setNavigation(R.id.teacherAnnouncementFragment, extra)
            }
        }
    }

    private fun setNavigation(location: Int, extra: Bundle?) {
        Navigation.findNavController(this, R.id.fragment_socket).navigate(location, extra)
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

    private fun setUserInfo() = launch {
        val photo = preferenceProvider.getUserSessionData().imageUrl
        val username = "user: ${preferenceProvider.getUserSessionData().name?.split(" ")?.get(0)}"
        navView.getHeaderView(0).nav_header_title.text = username
        GlideApp.with(applicationContext).load(photo)
            .placeholder(R.drawable.parent)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(navView.getHeaderView(0).img_sidebar)
    }

    override fun onResume() {
        super.onResume()
        setUserInfo()
        mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                it,
                IntentFilter(FOREGROUND_PUSH_NOTIFICATION)
            )
        }
    }

    override fun onPause() {
        super.onPause()
        mRegistrationBroadcastReceiver?.let { LocalBroadcastManager.getInstance(this).unregisterReceiver(it) }
    }

}
