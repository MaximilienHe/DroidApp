package com.redgunner.droidsoft.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.redgunner.droidsoft.R
import com.redgunner.droidsoft.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

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

        val restApiKey = "6977994q54_pq39qr.s2n61q236499n23o0.870695s959891o9104o8qnn4224q"
        val deviceUuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("Device UUID : $deviceUuid")
        var deviceToken = "test"
        val subscription = "test2"

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            deviceToken = it.result
            System.out.println("Device Token : $deviceToken")
        }


        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://test.lucas-chalmandrier.fr/wp-json/fcm/pn/subscribe")

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true

            val postData = "rest_api_key=$restApiKey&device_uuid=$deviceUuid&device_token=$deviceToken&subscription=$subscription"
            val outputStream = connection.outputStream
            outputStream.write(postData.toByteArray(Charsets.UTF_8))
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // API call was successful
                // You can read the response from connection.inputStream
                System.out.println("API call was successful")
            } else {
                // API call failed
                System.out.println("API call failed")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel all coroutines started in the scope of this Activity to avoid leaking memory
        CoroutineScope(Dispatchers.IO).cancel()
    }
}
