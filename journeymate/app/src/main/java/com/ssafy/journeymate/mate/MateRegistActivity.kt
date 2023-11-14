package com.ssafy.journeymate.mate

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.FindUserResponse
import com.ssafy.journeymate.api.MateApi
import com.ssafy.journeymate.api.RegistMateRequest
import com.ssafy.journeymate.api.RegistMateResponse
import com.ssafy.journeymate.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.*
import java.util.Calendar
import java.util.Locale

class MateRegistActivity : AppCompatActivity() {

    private lateinit var startDateEditText: TextView
    private lateinit var endDateEditText: TextView
    private lateinit var mateEditText: AutoCompleteTextView
    private lateinit var mateLinearLayout: LinearLayout

    // id 담는 배열
    private var userList = mutableListOf<String>()

    // nickname 담는 배열
    private var nicknameList = mutableListOf<String>()

    private lateinit var mateApi: MateApi

    private lateinit var userApi: UserApi

    // 선택된 시작 날짜 저장
    private var selectedStartDate: String? = null

    // 드롭다운
    private lateinit var mateEditTextAdapter: ArrayAdapter<String>

    val loggedInUserId = "11ee7ebf112c1927aa4b85e38939408d"
    val loggedInUserNickname = "coffee"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate_regist)

        val toolbarInclude = findViewById<View>(R.id.mate_regsit_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "여행 그룹 생성"





        // 시작 날짜, 끝 날짜
        startDateEditText = findViewById(R.id.start_date_text_view)
        endDateEditText = findViewById(R.id.end_date_text_view)

        val startDateIcon: ImageView? = findViewById(R.id.start_date_icon)
        startDateIcon?.setOnClickListener {
            showDatePickerDialog(startDateEditText, isStartDate = true)
        }

        val endDateIcon: ImageView? = findViewById(R.id.end_date_icon)
        endDateIcon?.setOnClickListener {
            // endDate 클릭시 startDate 에 값 존재
            if (startDateEditText.text.isNotEmpty()) {
                showDatePickerDialog(endDateEditText, isStartDate = false)
            } else {

                AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("시작 날짜를 먼저 선택해주세요.")
                    .setPositiveButton("확인", null)
                    .show()
            }
        }


        // 메이트 찾기
        mateEditText = findViewById(R.id.mate_edit_text)
        mateLinearLayout = findViewById(R.id.mates_linearlayout)

        // creatorId -> userList 에 포함 시키기
        // 서버에는 ID 를 보내고 front 에서는 nickname 을 보여줘야 한다.
        // nicknameList.add(App.INSTANCE.nickname)


        // 지금 로그인된 회원은 무조건 추가
        addTextToLayout(loggedInUserNickname, loggedInUserId )

        mateEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val input = s.toString()

                searchUser(input)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        mateEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val input = mateEditText.text.toString()
                // API 호출
                searchUser(input)
                true
            } else {
                false
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/mate-service/regist/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mateApi = retrofit.create(MateApi::class.java)

        // 그룹 생성하기 버튼 클릭
        val mateRegistButton: Button = findViewById(R.id.mate_regist_button)
        mateRegistButton.setOnClickListener {
            if (validateInput()) {
                callMateRegisterApi(userList)
            }
        }
    }


    // 시작 날짜, 끝 날짜 선택
    private fun showDatePickerDialog(textView: TextView, isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear/${selectedMonth + 1}/$selectedDay"

                // String to Date
                val originalFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
                val date = originalFormat.parse(selectedDate)

                if (isStartDate) {
                    selectedStartDate = originalFormat.format(date)
                }

                textView.setText(originalFormat.format(date))
            },
            year, month, day
        )

        if (!isStartDate) {
            val startDate = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA).parse(selectedStartDate)

            datePickerDialog.datePicker.minDate = startDate?.time ?: System.currentTimeMillis()
        } else {
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        }

        datePickerDialog.show()
    }

    // 메이트 검색

    private fun searchUser(input: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userApi = retrofit.create(UserApi::class.java)

        userApi.findUserByNickname(input).enqueue(object : Callback<FindUserResponse> {
            override fun onResponse(
                call: Call<FindUserResponse>,
                response: Response<FindUserResponse>
            ) {
                if (response.isSuccessful) {
                    val userData = response.body()?.data
                    if (userData != null) {
                        // 닉네임을 ArrayAdapter에 설정
                        val nicknames = listOf(userData.nickname)
                        mateEditTextAdapter = ArrayAdapter(
                            this@MateRegistActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            nicknames
                        )
                        mateEditText.setAdapter(mateEditTextAdapter)
                        mateEditText.showDropDown()

                        // 클릭시 userList와 nicknameList에 추가
                        mateEditText.setOnItemClickListener { _, _, position, _ ->
                            val selectedNickname = mateEditTextAdapter.getItem(position) ?: ""
                            addTextToLayout(selectedNickname, userData.id)
                        }
                    } else {
                        // 응답이 성공적이지만, body가 null인 경우
                        Log.e("에러", "응답은 성공했지만 데이터가 없습니다.")
                    }
                } else {
                    // 응답에 실패한 경우 (4xx 또는 5xx 응답)
                    val errorMessage = response.errorBody()?.string()
                    Log.e("에러", "user 검색 에러 발생: $errorMessage")
                }
            }

            override fun onFailure(call: Call<FindUserResponse>, t: Throwable) {
                Log.e("에러", "user 검색 에러 발생: ${t.message}")
            }
        })
    }

    private fun addTextToLayout(nickname: String, id: String) {
        val textView = TextView(this).apply {
            text = nickname
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.START
                setMargins(10, 10, 10, 10)
            }
        }

        val imageView = ImageView(this).apply {
            setImageResource(android.R.drawable.ic_delete)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END
            }
            setOnClickListener {
                // 첫 번째 요소는 삭제 불가능하게 설정
                if (nickname != nicknameList.firstOrNull()) {
                    mateLinearLayout.removeView(textView.parent as View)
                    userList.remove(id)
                    nicknameList.remove(nickname)
                }
            }
        }

        // Horizontal LinearLayout을 만들어 TextView와 ImageView를 포함시킵니다.
        val userLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL // 가운데 정렬을 위해 gravity 설정
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // 너비를 MATCH_PARENT로 설정
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            addView(textView)
            addView(imageView)
        }

        mateLinearLayout.addView(userLayout)

        // 첫 번째 요소는 삭제 불가능하게 설정
        if (userList.isEmpty()) {
            imageView.visibility = View.INVISIBLE
        }

        userList.add(id)
        nicknameList.add(nickname)
    }

    private fun validateInput(): Boolean {
        val destination = findViewById<EditText>(R.id.destination_text_edit).text.toString()
        val name = findViewById<EditText>(R.id.name_edit_text).text.toString()

        if (destination.isBlank() || name.isBlank() || startDateEditText.text.isBlank() || endDateEditText.text.isBlank() || userList.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("모든 사항을 입력해주세요.")
                .setPositiveButton("확인", null)
                .show()
            return false
        }

        return true
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun callMateRegisterApi(userList: List<String>) {

        val inputTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val sDateTime = LocalDate.parse(startDateEditText.text.toString(), inputTimeFormatter)
        val startDate = sDateTime.atStartOfDay()
        val eDateTime = LocalDate.parse(endDateEditText.text.toString(), inputTimeFormatter)
        val endDate = eDateTime.atStartOfDay()

        val registMateRequest = RegistMateRequest(
            destination = findViewById<EditText>(R.id.destination_text_edit).text.toString(),
            name = findViewById<EditText>(R.id.name_edit_text).text.toString(),
            startDate = startDate,
            endDate = endDate,
            users = userList.toMutableList(),
            creator = loggedInUserId
        )

        Log.i("MateRegistActivity", "등록될 user id: ${userList.joinToString()}")

        val call = mateApi.registMate(registMateRequest)
        call.enqueue(object : Callback<RegistMateResponse> {
            override fun onResponse(
                call: Call<RegistMateResponse>,
                response: Response<RegistMateResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("sucess log", "api 응답 성공")
                    val intent = Intent(this@MateRegistActivity, MateListActivity::class.java)
                    intent.putExtra("response", response.body())  // 응답을 새 액티비티로 전달
                    startActivity(intent)

                } else {
                    Log.i("Api Response Unsuccessful", "api 응답 문제")

                }
            }

            override fun onFailure(call: Call<RegistMateResponse>, t: Throwable) {
                Log.e("error log", "실패했습니다. ")
            }
        })
    }


}