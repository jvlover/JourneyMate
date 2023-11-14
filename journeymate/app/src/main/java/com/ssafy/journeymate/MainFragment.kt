package com.ssafy.journeymate

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.ssafy.journeymate.api.ChatApi
import com.ssafy.journeymate.api.LoadCommentResponse
import com.ssafy.journeymate.api.ResponseDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://k9a204.p.ssafy.io:8000/") // 여기서 API의 기본 URL을 설정
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val chatApi: ChatApi = retrofit.create(ChatApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val imgButton1: ImageButton = view.findViewById(R.id.img1)

        // 클릭 리스너 설정
        imgButton1.setOnClickListener {
            // mateId 값을 설정 (예시)
            val mateId: Long = 8

            // Retrofit을 사용하여 데이터 요청
            val call: Call<ResponseDto> = chatApi.loadComment(mateId)
            call.enqueue(object : Callback<ResponseDto> {
                override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                    if (response.isSuccessful) {
                        val responseDto = response.body()
                        Log.i("NetworkResponse", "성공: ${responseDto?.message}")
                        // 데이터 처리, 예: responseDto?.data 를 사용
                    } else {
                        Log.i("NetworkResponse", "실패: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                    Log.e("NetworkError", "요청 실패: ${t.message}")
                }
            })
        }
        return view
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}