package kr.hs.emirim.mirimhee.quizlocker

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_quiz_locker.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class QuizLockerActivity : AppCompatActivity() {

    var quiz:JSONObject? = null

    val worngAnswerPref by lazy { getSharedPreferences("wrongAnswer",Context.MODE_PRIVATE) }
    val correctAnswerPref by lazy{getSharedPreferences("correctAnswer",Context.MODE_PRIVATE)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            // 잠금화면에서 보여지도록 설정
            setShowWhenLocked(true)
            // 잠금 해제
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            // 잠금화면에서 보여지도록 설정
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            // 기본 잠금화면을 해제
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
        // 화면을 켜진 상태로 유지
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_quiz_locker)

        val json = assets.open("capital.json").reader().readText()
        val quizArray = JSONArray(json)
        // 퀴즈를 선택한다.
        quiz = quizArray.getJSONObject(Random().nextInt(quizArray.length()))
        // 퀴즈를 보여준다.
        quizLabel.text = quiz?.getString("question")
        choice1.text = quiz?.getString("choice1")
        choice2.text = quiz?.getString("choice2")

        val id = quiz?.getInt("id").toString() ?: ""
        correctCountLabel.text = "정답횟수: ${correctAnswerPref.getInt(id,0)}"
        wrongCountLabel.text = "오답횟수: ${worngAnswerPref.getInt(id,0)}"



        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progresss: Int, fromUser: Boolean) {
                when {
                progresss > 95 -> {
                    leftImageView.setImageResource(R.drawable.padlock)
                    rightImageView.setImageResource(R.drawable.unlock)
                }
                    progresss < 5 -> {
                        leftImageView.setImageResource(R.drawable.unlock)
                        rightImageView.setImageResource(R.drawable.padlock)
                    }
                   else -> {
                        leftImageView.setImageResource(R.drawable.padlock)
                        rightImageView.setImageResource(R.drawable.padlock)
                    }
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress =  seekBar?.progress ?:50
                when{
                    progress > 95 -> checkChoice(quiz?.getString("choice2") ?: "")
                    progress < 5 -> checkChoice(quiz?.getString("choice1") ?: "")
                    else -> seekBar?.progress =50
                }
            }
        })
    }
    fun checkChoice(choice:String){
        quiz?.let{
            when{
                choice == it.getString("answer") ->{
                    val id = it.getInt("id").toString()
                    var count = correctAnswerPref.getInt(id,0)
                    count++
                    correctAnswerPref.edit().putInt(id,count).apply()
                    correctCountLabel.text="정답 횟수 ${count}"
                }
                else -> {
                    val id = it.getInt("id").toString()
                    var count = worngAnswerPref.getInt(id,0)
                    count++
                    worngAnswerPref.edit().putInt(id,count).apply()
                    wrongCountLabel.text="오답 횟수 ${count}"

                    leftImageView.setImageResource(R.drawable.padlock)
                    rightImageView.setImageResource(R.drawable.padlock)
                    seekBar?.progress =50

                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if(Build.VERSION.SDK_INT >= 26){
                        vibrator.vibrate(VibrationEffect.createOneShot(1000,100))
                    }else{
                        vibrator.vibrate(1000)
                    }
                }
            }
        }
    }

}

