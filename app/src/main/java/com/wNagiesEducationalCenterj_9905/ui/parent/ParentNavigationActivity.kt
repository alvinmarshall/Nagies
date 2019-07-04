package com.wNagiesEducationalCenterj_9905.ui.parent

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.wNagiesEducationalCenterj_9905.R
import com.wNagiesEducationalCenterj_9905.base.BaseActivity
import com.wNagiesEducationalCenterj_9905.common.GlideApp
import com.wNagiesEducationalCenterj_9905.common.USER_INFO
import com.wNagiesEducationalCenterj_9905.ui.auth.RoleActivity
import kotlinx.android.synthetic.main.content_parent_navigation.*
import kotlinx.android.synthetic.main.nav_header_parent_navigation.view.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class ParentNavigationActivity : BaseActivity() {
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var snackBar:Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        setupNavigation()
        if (intent.hasExtra(USER_INFO)) {
            setUserInfo(intent)
        }
    }

    private fun setUserInfo(intent: Intent?) = launch {
        val bundle = intent?.extras?.getStringArrayList(USER_INFO)
        val photo = bundle?.get(1)
        val index = bundle?.get(0)
        val title = "index: $index"
        snackBar = Snackbar.make(root,"welcome back $index",Snackbar.LENGTH_LONG)
        snackBar?.show()
        navView.getHeaderView(0).nav_header_title.text = title
        GlideApp.with(applicationContext).load(photo)
            .placeholder(R.drawable.default_user_avatar)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(navView.getHeaderView(0).img_sidebar)

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
            R.id.action_settings -> true
            R.id.action_logout -> {
                preferenceProvider.setUserLogin(false, null)
                startActivity(intentFor<RoleActivity>().newTask().clearTask())
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
