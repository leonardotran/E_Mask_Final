package com.vietnamese.maskregion

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(getString(R.string.app_name))
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout)
        navigation_view.setupWithNavController(navController)

        bottom_nav_view.setupWithNavController(navController)
        val user = FirebaseAuth.getInstance().currentUser
        window.setNavigationBarColor(Color.BLACK)

//        val headerView = navigation_view.getHeaderView(0)
//        avatarImage = headerView.findViewById(R.id.image_avatar) as ImageView
//        textDisplayName = headerView.findViewById(R.id.textDisplayName) as TextView
//        textRanking = headerView.findViewById(R.id.textRanking) as TextView
//
//        val db = FirebaseFirestore.getInstance()
//        profileRef = db.collection("Profile").document(user?.uid.toString())


//        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
//
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//        this.title = navController.currentDestination?.label
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            this.title = navController.currentDestination?.label
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    private val TIME_INTERVAL =
        2000 // # milliseconds, desired time passed between two back presses.
    private var mBackPressed: Long = 0

    override fun onBackPressed() {
        if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.navigation_home) {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                finish()
                return
            } else {
                Toast.makeText(getBaseContext(), "Tap back twice to exit", Toast.LENGTH_SHORT)
                    .show();
            }
            mBackPressed = System.currentTimeMillis();
        } else if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
        }
    }

}