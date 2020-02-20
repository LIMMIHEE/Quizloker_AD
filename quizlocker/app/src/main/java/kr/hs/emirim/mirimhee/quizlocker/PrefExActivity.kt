package kr.hs.emirim.mirimhee.quizlocker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceFragment
import kotlinx.android.synthetic.main.activity_file_ex.*
import kotlinx.android.synthetic.main.activity_file_ex.saveButton
import kotlinx.android.synthetic.main.activity_pref_ex.*
import kotlinx.android.synthetic.main.activity_pref_ex.loadButton as loadButton1

class PrefExActivity : AppCompatActivity() {

    val nameFieldKey = "nameField"
    val pushCheckBoxKey = "pushCheckBox"
    val preference by lazy { getSharedPreferences("PrefExActivity", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref_ex)

        saveButton.setOnClickListener {
            preference.edit().putString(nameFieldKey, nameField.text.toString()).apply()
            preference.edit().putBoolean(pushCheckBoxKey, pushCheckBox.isChecked).apply()
        }

        loadButton.setOnClickListener {
            nameField.setText(preference.getString(nameFieldKey, ""))
            pushCheckBox.isChecked = preference.getBoolean(pushCheckBoxKey, false)
        }
    }

}

