package kr.hs.emirim.mirimhee.quizlocker

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_file_ex.*
import java.io.File
import java.io.FileNotFoundException

class FileExActivity : AppCompatActivity() {

    val filename = "data.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)

        saveButton.setOnClickListener {
            val text = textField.text.toString()
            when{
                TextUtils.isEmpty(text) -> {
                    Toast.makeText(applicationContext,"텍스타가 비어있습니다",Toast.LENGTH_LONG).show()
                }
                else -> {
                    saveToInnerStorage(text,filename)
                }
            }
        }

        loadButton.setOnClickListener {
            try {
            textField.setText(loadFromOnnerStorage(filename))
        }catch (e : FileNotFoundException){
                Toast.makeText(applicationContext,"저장된 텍스트가 없습니다",Toast.LENGTH_LONG).show()
            }

        }
    }

    fun loadFromOnnerStorage(filename: String): String {
        val fileInputStream = openFileInput(filename)
        return fileInputStream.reader().readText()
    }


    fun saveToInnerStorage(text: String, filename: String) {
        val fileOutputStream = openFileOutput(filename,Context.MODE_PRIVATE)
        fileOutputStream.write(text.toByteArray())
        fileOutputStream.close()
    }

}
