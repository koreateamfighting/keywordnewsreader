package com.example.keywordnewsreader

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.keywordnewsreader.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var microphoneImage: ImageView? = null

    var selecetedCompany : String? = null

    private val REQUEST_CODE_SPEECH_INPUT = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar()!!.hide();
        }//가로 모드일 때 타이틀 바 가지기 용도
        microphoneImage = findViewById<ImageView>(R.id.microphoneImage)  //이미지 뷰 변수 생성

        microphoneImage!!.setOnClickListener {
            getRecognizerIntent()
        }

        val trendNews_btn = findViewById<Button>(R.id.trendNews_btn)
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.setSelection(0)
        spinner.adapter = ArrayAdapter.createFromResource(this,R.array.newsCompany, android.R.layout.simple_list_item_1)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0->{
                        selecetedCompany = spinner.selectedItem.toString()

                    }
                    1->{
                        selecetedCompany = spinner.selectedItem.toString()

                    }
                    2->{
                        selecetedCompany = spinner.selectedItem.toString()

                    }
                    3->{
                        selecetedCompany = spinner.selectedItem.toString()

                    }
                    else->{
                        selecetedCompany = null
                    }
                }
            }



        }
        trendNews_btn.setOnClickListener {
            trendNews_btn.isEnabled = false
            val layout = findViewById<View>(R.id.MainLayout)
            val snack =
                Snackbar.make(layout, "해당 언론사 사이트로 이동 중입니다. 잠시만 기다려주세요", Snackbar.LENGTH_SHORT) // 스낵바 띄우기
            snack.view.setBackgroundColor(Color.parseColor("#6799FF")) // 스낵바 백그라운드 컬러 세팅
            snack.show()
            Handler().postDelayed({

                val intent = Intent(this, RankingNewsResultActivity::class.java)
                intent.putExtra("selectedCompany", selecetedCompany)
                startActivity(intent)
                trendNews_btn.isEnabled = true
            },2000)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {

            if (resultCode == RESULT_OK && data != null) {
                val
                        utteranceResult: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                println("결과는") // 발화 결과 logcat 확인용
                println(utteranceResult)
                println("입니다.")
                microphoneImage!!.isEnabled = false
                val layout = findViewById<View>(R.id.MainLayout) // 레이아웃 변수 생성
                val infoText = findViewById<TextView>(R.id.infoText) // 초기 안내문 텍스트뷰 생성
                infoText.setText("${utteranceResult}   검색중....") // 발화 이후 안내문텍스트 "검색중..." 으로 변경

                val snack =
                    Snackbar.make(layout, "해당 키워드를 검색중입니다. 잠시만 기다려주세요", Snackbar.LENGTH_SHORT) // 스낵바 띄우기
                snack.view.setBackgroundColor(Color.parseColor("#6799FF")) // 스낵바 백그라운드 컬러 세팅 (근데 안먹힘...)
                snack.show()

                Handler().postDelayed({
                    val intent = Intent(this, KeywordResultActivity::class.java)
                    infoText.setText("마이크 버튼을 클릭하고,\n 원하는 뉴스 키워드를 발화해주세요.") // 안내문 텍스트 다시 원래 문장으로..
                    intent.putExtra("utteranceResult", utteranceResult.get(0)) // 발화 결과를 가지고 ResultActivity로 이동
                    startActivity(intent)
                    microphoneImage!!.isEnabled = true
                }, 2000)

            }


        }

    }

    fun getRecognizerIntent(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH) // 인텐트 기능 구현 부분
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "찾을 키워드를 발화해주세요.")
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast
                .makeText(
                    this@MainActivity, " " + e.message,
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

}