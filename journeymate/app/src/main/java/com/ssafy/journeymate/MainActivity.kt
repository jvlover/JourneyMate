package com.ssafy.journeymate

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.ssafy.journeymate.databinding.ActivityMainBinding
import com.ssafy.journeymate.mate.MateListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.contensToolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//

//        val intent = Intent(this, DocsWriteActivity::class.java)
//        startActivity(intent)
        val popupBarButton: ImageButton = findViewById(R.id.btnMenu)
        popupBarButton.setOnClickListener {
            startActivity(Intent(this, PopupBarActivity::class.java))
        }
//        val DocsButton: ImageButton = findViewById(R.id.btnDocs)
//        DocsButton.setOnClickListener {
//            startActivity(Intent(this, PopupBarActivity::class.java))
//        }
//        val journeyButton: ImageButton = findViewById(R.id.btnJourney)
//        journeyButton.setOnClickListener {
//            startActivity(Intent(this, PopupBarActivity::class.java))
//        }
        val mateButton: ImageButton = findViewById(R.id.btnMate)
        mateButton.setOnClickListener {
            startActivity(Intent(this, MateListActivity::class.java))
        }

//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}