package com.ssafy.journeymate.checklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ssafy.journeymate.PopupBarActivity
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.ChecklistApi
import com.ssafy.journeymate.api.FindMateData
import com.ssafy.journeymate.api.LoadAllChecklistInfoResponse
import com.ssafy.journeymate.api.LoadChecklistInfoData
import com.ssafy.journeymate.databinding.ActivityChecklistListBinding
import com.ssafy.journeymate.global.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChecklistListActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var checklistApi: ChecklistApi

    private val binding by lazy { ActivityChecklistListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val checklistLayout = findViewById<LinearLayout>(R.id.checklist_layout)
        val mateData = intent.getSerializableExtra("mateData") as FindMateData
//        val userId = "11ee86e2ade87e5593af792b5a90af12"
        val userId = App.INSTANCE.id

        retrofit = Retrofit.Builder()
            .baseUrl("http://k9a204.p.ssafy.io:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        checklistApi = retrofit.create(ChecklistApi::class.java)

        val callChecklitListLoad =
            checklistApi.loadAllChecklistInfoByMateId(userId, mateData.mateId)

        callChecklitListLoad.enqueue(object : Callback<LoadAllChecklistInfoResponse> {
            override fun onResponse(
                call: Call<LoadAllChecklistInfoResponse>,
                response: Response<LoadAllChecklistInfoResponse>
            ) {
                if (response.isSuccessful) {
                    val loadAllChecklistInfoResponse = response.body()

                    loadAllChecklistInfoResponse?.data?.forEachIndexed { _, checklistData ->
                        val checklistView = createView(checklistData)
                        checklistLayout.addView(checklistView)
                    }
                    Log.d("API 성공", response.toString())
                } else {
                    Log.e("API 오류", "Checklist 불러올 때 오류 발생 ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoadAllChecklistInfoResponse>, t: Throwable) {
                Log.e("API 오류", "Checklist 불러올 때 오류 발생 ${t.localizedMessage}")
            }
        })

        val toolbarInclude = findViewById<View>(R.id.checklist_list_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        toolbarTitleTextView.text = "체크리스트 목록"

        val txtMateDate = findViewById<TextView>(R.id.txtMateDate)
        txtMateDate.text =
            "${mateData.startDate?.substringBefore("T")} ~ ${mateData.endDate?.substringBefore("T")}"

        val txtDestination = findViewById<TextView>(R.id.txtDestination)
        txtDestination.text = mateData.destination

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // 뒤로 가기 버튼 클릭 시 수행할 액션
            onBackPressed()
        }
        val menuButton: ImageButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            // 메뉴 버튼 클릭 시 수행할 액션
            startActivity(Intent(this, PopupBarActivity::class.java))
        }
    }

    private fun createView(checklistData: LoadChecklistInfoData): View {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.layout_checklist_list_data, null)

        val checkButton = view.findViewById<CheckBox>(R.id.checkbtnItem)
        checkButton.isChecked = checklistData.isChecked
        checkButton.setOnClickListener {
            checklistData.isChecked = checkButton.isChecked
        }

        val itemName = view.findViewById<TextView>(R.id.txtItemName)
        itemName.text = checklistData.name

        val itemNum = view.findViewById<TextView>(R.id.txtItemNum)
        itemNum.text = checklistData.num.toString()

        return view
    }
}