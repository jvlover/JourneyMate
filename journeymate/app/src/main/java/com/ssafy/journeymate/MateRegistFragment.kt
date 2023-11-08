package com.ssafy.journeymate

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.ssafy.journeymate.databinding.FragmentMateRegistBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [MateRegistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        endDateEditText.setOnClickListener {
            showDatePickerDialog()
        }


        



        return _binding!!.root
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                endDateEditText.setText(selectedDate)
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