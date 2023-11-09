package com.ssafy.journeymate

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.journeymate.databinding.FragmentMateRegistBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MateRegistActivity : AppCompatActivity() {


    private lateinit var startDateEditText: TextView

    private lateinit var endDateEditText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mate_regist)

        // 프래그먼트 매니저를 사용하여 프래그먼트를 추가합니다.
//        if (savedInstanceState == null) { // 액티비티가 재생성되지 않은 경우에만 프래그먼트 추가
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerView, MateRegistFragment())
//                .commit()
//        }

        startDateEditText = findViewById(R.id.start_date_text_view)
        endDateEditText = findViewById(R.id.end_date_text_view)

        val startDateIcon: ImageView? = findViewById(R.id.start_date_icon)

        if (startDateIcon != null) {
            startDateIcon.setOnClickListener {
                showDatePickerDialog(startDateEditText)
            }
        }

        val endDateIcon: ImageView? = findViewById(R.id.end_date_icon)

        if (endDateIcon != null) {
            endDateIcon.setOnClickListener {
                showDatePickerDialog(endDateEditText)
            }
        }

    }

    private fun showDatePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedYear}/${selectedMonth + 1}/${selectedDay}"
                textView.setText(selectedDate)
            }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }
}