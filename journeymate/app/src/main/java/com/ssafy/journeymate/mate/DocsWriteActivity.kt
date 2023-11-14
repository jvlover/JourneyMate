package com.ssafy.journeymate.mate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.ssafy.journeymate.R

class DocsWriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs_write)

        val toolbarInclude = findViewById<View>(R.id.mate_contents_toolbar) as Toolbar

        val toolbarTitleTextView = toolbarInclude.findViewById<TextView>(R.id.toolbarTitle)

        val AddBtn = findViewById<Button>(R.id.AddBtn)

        AddBtn.setOnClickListener {

        }
        toolbarTitleTextView.text = "여행 문서 작성"


    }

}