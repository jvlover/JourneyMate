package com.ssafy.journeymate.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ssafy.journeymate.R
import com.ssafy.journeymate.api.ChatApi
import com.ssafy.journeymate.api.ChatMessage
import com.ssafy.journeymate.api.ResponseDto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private lateinit var stompClient: StompClient

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://k9a204.p.ssafy.io:8000/") // 여기서 API의 기본 URL을 설정
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
        initializeWebSocketConnection()

    }

    private fun initializeWebSocketConnection() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://k9a204.p.ssafy.io:8000/chat-service")
        stompClient.connect()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // 연결 성공 처리
                sendMessage("초기 메시지 또는 연결 확인 메시지")
            }, {
                // 연결 실패 처리
                Log.e("WebSocket", "연결 실패", it)
            })
    }
    private fun sendMessage(messageContent: String) {
        val message = ChatMessage(mateId = 8, sender = "김민범", message = messageContent, userCount = 8)
        val messageJson = Gson().toJson(message) // 메시지 객체를 JSON 문자열로 변환

        stompClient.send("/pub/chat-service", messageJson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // 메시지 전송 성공 처리
                Log.i("WebSocket", "메시지 전송 성공")
            }, {
                // 메시지 전송 실패 처리
                Log.e("WebSocket", "메시지 전송 실패", it)
            })
    }

    private fun fetchChatRoomDetails() {
        // Retrofit을 사용하여 채팅방 관련 데이터 가져오기
        // 예: 채팅방 메시지 목록, 참여자 정보 등
    }

//    private fun convertJsonToChatMessage(json: String): ChatMessage {
//        // JSON 문자열을 ChatMessage 객체로 변환하는 로직
//    }


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