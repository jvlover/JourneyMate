package com.ssafy.journeymate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.ssafy.journeymate.databinding.ActivityToolbarBinding
import com.ssafy.journeymate.global.App
import com.ssafy.journeymate.user.MyPageActivity
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ToolbarActivity : AppCompatActivity() {

    private val binding by lazy { ActivityToolbarBinding.inflate(layoutInflater) }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(binding.root)

        var imageView: ImageView? = binding.imgProfile;
        var bitmap: Bitmap? = null

        binding.txtName.text = App.INSTANCE.nickname

        val uThread: Thread = object : Thread() {
            override fun run() {
                try {
                    val url = URL("${App.INSTANCE.profileImg}")
                    // 이미지 URL 경로

                    // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                    conn.doInput = true // 서버로부터 응답 수신
                    conn.connect() // 연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)

                    val `is`: InputStream = conn.inputStream // inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(`is`) // Bitmap으로 변환

                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        uThread.start() // 작업 Thread 실행
        try {
            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야 한다.
            // join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리도록 한다.
            // join() 메서드는 InterruptedException을 발생시킨다.
            uThread.join()
            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
            imageView?.setImageBitmap(bitmap)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val myGroupButton: AppCompatButton = findViewById(R.id.btnMyGroup)
        val myGroupButton2: AppCompatButton = findViewById(R.id.btnMyGroup2)
        val myPageButton: AppCompatButton = findViewById(R.id.btnMypage)

        myGroupButton.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
        myGroupButton2.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
        myPageButton.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
    }
}