package com.ssafy.journeymate.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.kakao.sdk.user.UserApiClient
import com.ssafy.journeymate.R
import com.ssafy.journeymate.databinding.ActivityMyPageBinding

class MyPageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyPageBinding.inflate(layoutInflater) }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        // Todo : S3 로 이미지 받아와야 합니다.
//        var imgProfile = findViewById<ImageView>(R.id.imgProfile)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("userInformation", "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d("userInformation", "사용자 정보 요청 성공 : $user")

                binding.txtName.text = user.kakaoAccount?.profile?.nickname
            }
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
            //Todo : 회원탈퇴 api
        }

    }
}