package com.redgunner.droidsoft.view

import android.R.id
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Event
import com.google.firebase.analytics.FirebaseAnalytics.Param
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.redgunner.droidsoft.R
import com.redgunner.droidsoft.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: SharedViewModel by viewModels()

    private val firebaseAnalytics = Firebase.analytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainIntent = Intent(this, SplashScreen::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainIntent)


        //Log.d("firebase", mFirebaseAnalytics!!.sessionId.isSuccessful.toString())
        // Obtain the FirebaseAnalytics instance.
        val bundle = Bundle()
        bundle.putString(Param.ITEM_NAME, "name")
        bundle.putString(Param.CONTENT_TYPE, "image")
        //mFirebaseAnalytics!!.logEvent(Event.SELECT_CONTENT, bundle)

        /*
        val ll = findViewById<View>(R.id.main) as ConstraintLayout
        ll.setVisibility(View.GONE)

        Handler().postDelayed({
            ll.setVisibility(View.VISIBLE)
        }, 3000)
        */

    }
}