package com.ssafy.journeymate.checklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.journeymate.R
import com.ssafy.journeymate.databinding.ActivityChecklistListBinding

class ChecklistListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChecklistListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist_list)
    }
}