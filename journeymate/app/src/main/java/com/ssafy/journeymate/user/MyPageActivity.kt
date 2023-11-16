package com.ssafy.journeymate.user

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.kakao.sdk.user.UserApiClient
import com.ssafy.journeymate.R
import com.ssafy.journeymate.databinding.ActivityMyPageBinding
import com.ssafy.journeymate.global.App
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class MyPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMyPageBinding.inflate(layoutInflater) }

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

        val logoutButton: AppCompatButton = findViewById(R.id.btnLogut)
        logoutButton.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("logout", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i("logout", "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

//        val modifyProfileButton: Button = findViewById(R.id.btnModifyProfile)
//        modifyProfileButton.setOnClickListener {
//            startActivity(Intent(this,ModifyProfileActivity::class.java))
//        }

        val exitButton: Button = findViewById(R.id.btnExit)
        exitButton.setOnClickListener {
            //Todo : 회원탈퇴 api 모달도 띄우기
        }
    }
}
