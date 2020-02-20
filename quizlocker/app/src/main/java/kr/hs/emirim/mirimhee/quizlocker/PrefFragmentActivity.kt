package kr.hs.emirim.mirimhee.quizlocker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceFragment


class PrefFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref_fragment)
        fragmentManager.beginTransaction().replace(android.R.id.content,MyPreFragment()).commit()

    }

    class MyPreFragment(): PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.ex_pref)
        }
    }

}
