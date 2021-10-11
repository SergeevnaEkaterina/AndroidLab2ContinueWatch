package ru.spbstu.icc.kspt.lab2.continuewatch
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var secondsElapsed: Int = 0
    var isOnScreen = true
    lateinit var textSecondsElapsed: TextView
    private lateinit var sharedPref: SharedPreferences

    var backgroundThread = Thread {
        while (true) {
            if (isOnScreen) {
                Thread.sleep(1000)
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.text, secondsElapsed++)
                }
            }
        }
    }

    override fun onStop() {
        isOnScreen = false
        with(sharedPref.edit()) {
            putInt(SECONDS_ELAPSED, secondsElapsed)
            apply()
        }
        super.onStop()
    }

    override fun onStart() {
        isOnScreen = true
        secondsElapsed = sharedPref.getInt(SECONDS_ELAPSED, secondsElapsed)
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        sharedPref = getSharedPreferences(SECONDS_ELAPSED, Context.MODE_PRIVATE)
        backgroundThread.start()
    }

    companion object { const val SECONDS_ELAPSED = "Seconds passed" }

}