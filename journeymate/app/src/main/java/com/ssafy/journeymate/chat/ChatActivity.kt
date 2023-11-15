package com.ssafy.journeymate.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.ChatApi
import com.ssafy.journeymate.api.ChatMessage
import com.ssafy.journeymate.api.ResponseDto
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    var url = "http://k9a204.p.ssafy.io:8000/"
    val client = OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()
    private var stompUrl = "ws://k9a204.p.ssafy.io:8000/"
    private var pub = "/pub/chat-service"
    private var sub = "/sub/chat-service/8"
    private var mateId : Long = 8;
    private var userName : String = "김민범"
    val stomp = StompClient(client,1000L).apply { this@apply.url = url }
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url) // 여기서 API의 기본 URL을 설정
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val chatApi: ChatApi = retrofit.create(ChatApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.messageActivity_recyclerview)
        adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter

        val mateId: Long = 8

        // Retrofit을 사용하여 데이터 요청
        val call: Call<ResponseDto> = chatApi.loadComment(mateId)


        call.enqueue(object : Callback<ResponseDto> {
            override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                if (response.isSuccessful) {
                    val responseDto = response.body()
                    responseDto?.data?.forEach { chatMessage ->
                        adapter.addMessage(ChatMessage(chatMessage.sender, chatMessage.mateId, chatMessage.message))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                Log.e("NetworkError", "요청 실패: ${t.message}")
            }
        })

    }


    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MessageViewHolder>() {
        private val messages = ArrayList<ChatMessage>()

        fun addMessage(message: ChatMessage) {
            messages.add(message)
            notifyDataSetChanged()
            recyclerView.scrollToPosition(messages.size - 1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = messages[position]
            holder.textView_message.text = message.message
            // 추가적인 UI 설정...
        }

        override fun getItemCount(): Int = messages.size

        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView_message: TextView = view.findViewById(R.id.messageItem_textView_message)
            // 기타 필요한 뷰 요소들...
        }
    }
}