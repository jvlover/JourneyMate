package com.ssafy.journeymate

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ssafy.journeymate.databinding.FragmentMateRegistBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MateRegistFragment : Fragment() {

    private lateinit var startDateEditText: EditText

    private lateinit var endDateEditText: EditText

    private var calendar = Calendar.getInstance()

    private var _binding: FragmentMateRegistBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMateRegistBinding.inflate(inflater, container, false)

        startDateEditText = _binding!!.startDateEditText as EditText
        endDateEditText = _binding!!.endDateEditText as EditText
        val startDateIcon = _binding!!.startDateIcon
        val endDateIcon = _binding!!.endDateIcon


        startDateIcon.setOnClickListener {
            showDatePickerDialog(startDateEditText)
        }

        endDateIcon.setOnClickListener {
            showDatePickerDialog(endDateEditText)
        }


        val selectedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(calendar.time)

        return _binding!!.root
    }

    private fun showDatePickerDialog(editText: EditText) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                editText.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
            },
            year,
            month,
            day

        )
        datePickerDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}