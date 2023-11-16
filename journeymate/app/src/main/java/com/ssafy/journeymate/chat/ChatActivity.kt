package com.ssafy.journeymate.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.ChatApi
import com.ssafy.journeymate.api.ChatMessage
import com.ssafy.journeymate.api.ResponseDto
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Scanner
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    private var stomp: StompClient? = null
    var url = "http://k9a204.p.ssafy.io:8000/"
    private var mateId : Long = 8;
    private var userName : String = "김민범"
    var stompUrl = "ws://k9a204.p.ssafy.io:8000/chat-service"
    var pub = "/pub/chat-service"
    var sub = "/sub/chat-service/mate"
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
                        adapter.addMessage(ChatMessage(chatMessage.sender, chatMessage.message))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                Log.e("NetworkError", "요청 실패: ${t.message}")
            }
        })

        stompMain()
        val sendButton: ImageButton = findViewById(R.id.messageActivity_ImageButton)
        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    fun stompMain() {
        var stompConnection: Disposable
        var topic: Disposable
        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
        stomp = StompClient(client,1000L).apply { url = stompUrl }
        stompConnection = stomp!!.connect().subscribe {
            when(it.type) {
                Event.Type.OPENED -> {
                    topic = stomp!!.join("${sub}/${mateId}")
                        .subscribe()
//                    topic.dispose()
                    Log.i("연결","성공적")

                }
                Event.Type.CLOSED -> {

                }
                Event.Type.ERROR -> {

                }

                else -> {}
            }
        }

    }

    private fun sendMessage() {
        val messageText = findViewById<EditText>(R.id.messageActivity_editText).text.toString()
        if (messageText.isNotEmpty()) {
            var data = JSONObject()
            data.put("type","TALK")
            data.put("mateId",8)
            data.put("sender","김민범")
            data.put("message",messageText)
            data.put("userCount",8)
            stomp?.send("${pub}", data.toString())?.subscribe()
            Log.i("성공", data.toString())
            // EditText 초기화
            findViewById<EditText>(R.id.messageActivity_editText).text.clear()
        }
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


        override fun getItemCount(): Int = messages.size

        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewMessage: TextView = view.findViewById(R.id.messageItem_textView_message)
            val textViewSender: TextView = view.findViewById(R.id.messageItem_textview_name)
            // 기타 필요한 뷰 요소들...
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = messages[position]
            holder.textViewMessage.text = message.message
            holder.textViewSender.text = message.sender
            // Set other message properties...
        }
    }
}