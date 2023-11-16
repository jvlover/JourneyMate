package com.ssafy.journeymate

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.journeymate.databinding.ActivityPopupBarBinding
import com.ssafy.journeymate.global.App
import com.ssafy.journeymate.user.MyPageActivity
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class PopupBarActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPopupBarBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var imageView: ImageView? = binding.imgProfile;
        var bitmap: Bitmap? = null

        binding.txtName.text = App.INSTANCE.nickname

        val uThread: Thread = object : Thread() {
            override fun run() {
                try {
                    val url = URL("${App.INSTANCE.profileImg}")
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
            uThread.join()
            imageView?.setImageBitmap(bitmap)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val myGroupButton: Button = findViewById(R.id.btnMyGroup)
        myGroupButton.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }

        val myPageButton: Button = findViewById(R.id.btnMypage)
        myPageButton.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }

    }
}