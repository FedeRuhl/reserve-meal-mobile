package com.example.reservemeal.ui.session

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.reservemeal.R
import com.example.reservemeal.utility.PreferenceHelper
import com.example.reservemeal.utility.PreferenceHelper.set
import com.example.reservemeal.utility.getPayloadValue
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.*


class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val jwt = preferences.getString("jwt", "")
        val role = getPayloadValue(jwt ?: "", "role")

        if (role != "admin")
            hideOptions(navView)

        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.nav_home,
                    R.id.nav_create_meal,
                    R.id.nav_add_funds,
                    R.id.nav_my_reserves
                ), drawerLayout
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun hideOptions(navView: NavigationView) {
        val navMenu: Menu = navView.menu
        navMenu.findItem(R.id.nav_create_meal).isVisible = false
        navMenu.findItem(R.id.nav_add_funds).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_log_out) {
            preferences.all.clear()
            preferences["jwt"] = ""

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            true
        } else
            super.onOptionsItemSelected(item);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity2, menu)
        changeNavHeader()

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun changeNavHeader() {
        val jwt = preferences.getString("jwt", "")
        header_user_name.text = getPayloadValue(jwt ?: "", "name")
        header_user_email.text = getPayloadValue(jwt ?: "", "email")
    }
}