package com.example.keywordnewsreader

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.keywordnewsreader.databinding.ActivityKeywordResultBinding
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.Locale


class KeywordResultActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var btn_back: ImageButton? = null
    private var btn_play: Button? = null
    private var btn_stop: Button? = null
    private var resultList: ListView? = null
    private var scrollView: ScrollView? = null
    private val params = Bundle()
    private var playState : String = "stop"

    var textResult: String = ""
    val articleTitleList: MutableList<String> = ArrayList() // 기사 제목 리스트
    val articleUrlList: MutableList<String> = ArrayList() // 기사 링크 리스트
    val articlePhotoUrlList: MutableList<String> = ArrayList() // 기사 이미지 리스트
    val binding by lazy { ActivityKeywordResultBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var keyword: String? = intent.getStringExtra("utteranceResult") // 메인액티비티에서 받은 발화결과가 키워드가 됨.
        // 크롤링 시작
        if (keyword != null) { //키워드 발화 작동의 경우
            KeywordCrawling(keyword)
        }
        setContentView(binding.root)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar()!!.hide();
        }//가로 모드일 때 타이틀 바 가지기 용도
        btn_back = findViewById<ImageButton>(R.id.btn_back) // 각 레이아웃 변수들 선언
        btn_play = findViewById<Button>(R.id.btn_play)
        btn_stop = findViewById<Button>(R.id.btn_stop)
        scrollView = findViewById<ScrollView>(R.id.scrollView)

        btn_play!!.isEnabled = false //재생 버튼 플래그 false로 초기화
        tts = TextToSpeech(this, this)
        btn_stop!!.setOnClickListener { onDestroy() } // 정지 버튼 클릭시 시나리오 구현
        btn_back!!.setOnClickListener { // Back 버튼 클릭시 시나리오 구현 : 메인액티비티로 돌아가기
            tts!!.stop()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    @SuppressLint("CheckResult", "SuspiciousIndentation")
    fun KeywordCrawling(name: String) { //크롤링을 통해 기사제목, 기사링크 , 기사이미지 리스트 만들기

        val url =
            "https://search.naver.com/search.naver?where=news&ie=utf8&sm=nws_hty&query=$name&photo=1" //사진이 첨부된 뉴스 기사만 검색

        var doc: Document? = null



        Single.fromCallable { //싱글 클래스 스레드 사용
            try {
                doc = Jsoup.connect(url).get()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val element = doc!!.getElementsByAttributeValue("class", "news_tit") // 기사 제목 태그 get
            val element2 = doc!!.getElementsByAttributeValue("class", "news_tit")// 기사 url get
            val element3 = doc!!.getElementsByAttributeValue("class", "dsc_thumb")// 기사 이미지 get

            for (elem1 in element) { // element 형식의 내용에서 특정 attribute를 추출하여 각 리스트들에 삽입 작업
                articleTitleList.add(elem1.select("a").attr("title"))
            }
            for (elem2 in element2) {
                articleUrlList.add(elem2.select("a").attr("href"))
            }
            for (elem3 in element3) {
                articlePhotoUrlList.add(elem3.select("img").attr("data-lazysrc"))
            }
            println("제목 검사")
            println(articleTitleList)
            println("url 검사")
            println(articleUrlList)
            println("이미지 검사")
            println(articlePhotoUrlList)


        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(

                { text ->
                    val searchResultTextView = findViewById<TextView>(R.id.searchResultTextView)
                    searchResultTextView.setText(
                        "▼ " + name + "  : 네이버 뉴스 검색 결과  ▼"
                    )
                    for (i in 0..articleTitleList.size - 1) { // tts를 하기 위해 리스트 내용들을 추합한 스트링을 저장
                        textResult += "${articleTitleList.get(i)}" + "\t\t\t"
                    }

                    println(textResult)
                    //tts를 리스트로 읽는게 가능하다면 위의 3줄 및 textResult변수는 지워도 됨


                    // 결과를 나타내는 리스트뷰를 어댑터에 연결하기 위해 Totallist에 기사사진과 기사타이틀추합(리스트내에서 for문 구현 방법 몰라 일단 하드코딩으로)
                    var TotalList = arrayListOf(
                        Data(articlePhotoUrlList.get(0), articleTitleList.get(0)),
                        Data(articlePhotoUrlList.get(1), articleTitleList.get(1)),
                        Data(articlePhotoUrlList.get(2), articleTitleList.get(2)),
                        Data(articlePhotoUrlList.get(3), articleTitleList.get(3)),
                        Data(articlePhotoUrlList.get(4), articleTitleList.get(4)),
                        Data(articlePhotoUrlList.get(5), articleTitleList.get(5)),
                        Data(articlePhotoUrlList.get(6), articleTitleList.get(6)),
                        Data(articlePhotoUrlList.get(7), articleTitleList.get(7)),
                        Data(articlePhotoUrlList.get(8), articleTitleList.get(8)),
                        Data(articlePhotoUrlList.get(9), articleTitleList.get(9))
                    )

                    println("리스트 확인 시작") // 리스트 잘나오는지 확인하기 위한 logcat 확인용 부분
                    println(TotalList)
                    println("리스트 확인 종료")
                    btn_play!!.setOnClickListener { speakOut(articleTitleList) } // 재생 버튼 클릭시 시나리오 구현

                    binding.resultListView.adapter =
                        CustomAdapter(this, TotalList) // 사진,타이틀이 담긴 변수를 resultListView에 띄우기 위한 작업
                    binding.resultListView
                        ?.setOnItemClickListener { adapterView, view, i, l ->
                            Toast.makeText(this, articleUrlList[i], Toast.LENGTH_SHORT).show()
                            tts!!.stop()
                            Handler().postDelayed({
                                showWebViewDialog(articleUrlList[i]) // 특정 클릭된 링크를 웹뷰로 보여주기 function
                                super.onResume() // 결과 내용의 상태 죽지 않기 위해 유지
                            }, 1000)
                        }
                },
                // documentTitle 응답 오류 시
                { it.printStackTrace() }
            )
    }


    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.KOREAN)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                btn_play!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }


    }

    private fun speakOut(articleTitleList: MutableList<String>) {


        scrollView!!.fullScroll(ScrollView.FOCUS_UP)
        for (i in 0..articleTitleList.size - 1) {
            if (i == 0) {
                tts!!.speak("첫번째 뉴스", TextToSpeech.QUEUE_ADD,params,"첫번째 뉴스")
                Handler().postDelayed({}, 500)
                tts!!.speak(

                    articleTitleList.get(0),
                    TextToSpeech.QUEUE_ADD,
                    params,
                    articleTitleList.get(0)
                )


            } else {


                Handler().postDelayed({}, 500)

                    //scrollView!!.smoothScrollBy(0, 600);



                    tts!!.speak("${i+1} 번째 뉴스", TextToSpeech.QUEUE_ADD,params,"${i+1} 번째 뉴스")
                Handler().postDelayed({
                    scrollView!!.smoothScrollBy(0,100)
                }, 30000) //대충 6번째 뉴스 발화 때 스크롤 시간 때려 맞춤

                Handler().postDelayed({}, 500)
                    tts!!.speak(
                        articleTitleList.get(i),
                        TextToSpeech.QUEUE_ADD,
                        params,
                        articleTitleList.get(i)
                    )



            }
        }
        Handler().postDelayed({}, 1000)
        tts!!.speak("이상 뉴스 끝!!", TextToSpeech.QUEUE_ADD,params,"이상 뉴스 끝!!")


    }

    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()

        }
        super.onDestroy()
    }

    private fun showWebViewDialog(url: String) { //웹 뷰 띄우기 function

        val webView = WebView(this).apply {
            settings.useWideViewPort=true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false




            loadUrl(url)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        view!!.loadUrl(url)
                    }
                    return true
                }
            }
        }

        val builderSetting = AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background)

            .setView(webView)
            .setNegativeButton("이전 화면으로 돌아가기") { dialog, _ ->
                dialog.dismiss()

            }

            .show()
        builderSetting.window?.setLayout(2200,1000)

    }
}