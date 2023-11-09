package com.ssafy.journeymate

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ssafy.journeymate.databinding.FragmentMateRegistBinding
import java.util.Calendar

class MateRegistFragment : Fragment() {

    private lateinit var startDateEditText: TextView

    private lateinit var endDateEditText: TextView

    private var calendar = Calendar.getInstance()

    private var _binding: FragmentMateRegistBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMateRegistBinding.inflate(inflater, container, false)

        startDateEditText = _binding!!.startDateEditText
        endDateEditText = _binding!!.endDateEditText


        val startDateIcon: ImageView? = view?.findViewById(R.id.start_date_icon)

        if (startDateIcon != null) {
            startDateIcon.setOnClickListener {
                showDatePickerDialog(startDateEditText)
            }
        }

        val endDateIcon: ImageView? = view?.findViewById(R.id.end_date_icon)

        if (endDateIcon != null) {
            endDateIcon.setOnClickListener {
                showDatePickerDialog(endDateEditText)
            }
        }


//        val selectedStartDate =
//            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(startDateEditText.text)
//
//        val selectEndDate =
//            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(endDateEditText.text)
//


        return _binding!!.root
    }

    private fun showDatePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val context = requireContext() // requireContext()는 context가 null이 아님을 보장합니다.

        val datePickerDialog =
            DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedYear}/${selectedMonth + 1}/${selectedDay}"
                textView.setText(selectedDate)
            }, year, month, day)

        datePickerDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}