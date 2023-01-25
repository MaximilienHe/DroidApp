package com.redgunner.droidsoft.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.redgunner.droidsoft.R
import com.redgunner.droidsoft.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainIntent = Intent(this, SplashScreen::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainIntent)
        /*
        val ll = findViewById<View>(R.id.main) as ConstraintLayout
        ll.setVisibility(View.GONE)

        Handler().postDelayed({
            ll.setVisibility(View.VISIBLE)
        }, 3000)
        */

    }
}