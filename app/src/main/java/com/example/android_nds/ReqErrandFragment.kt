package com.example.android_nds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_nds.adapter.ReqErrandAdapter
import com.example.android_nds.databinding.FragmentReqerrandBinding
import com.example.android_nds.model.ReqErrand
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReqErrandFragment : Fragment(R.layout.fragment_reqerrand) {
    // 파이어베이스 연동에 필요한 선언
    private lateinit var database: FirebaseDatabase
    private lateinit var reqErrandRef: DatabaseReference
    // 데이터 바인딩을 위해 필요한 선언
    private var reqErrandBinding: FragmentReqerrandBinding? = null
    private lateinit var reqErrandAdapter: ReqErrandAdapter

    private val binding get()= reqErrandBinding!!   // 나는 null이 아니야!! 라는 의미에서 느낌표 두개

    // 프래그먼트가 화면에 올라왔을 때
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFirebase()
    }

    // 파이어베이스에서 데이터 가져오기, 이벤트 달기
    private fun initFirebase() {
        database = Firebase.database
        Log.i(TAG, "$database")
        reqErrandRef = database.getReference("area")
//        chatRef = database.getReference("errand") // X
        Log.i(TAG, "$reqErrandRef")
        // ChildEventListener 생성하기
        val reqErrandChildAddListener = object: ChildEventListener {
            // onChildAdded는 최초 1회 데이터를 가져옴. 이후로는 추가될 때 가져옴.
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                /* [ 파이어베이스에서 데이터 가져오기 테스트 ]
                Log.i(TAG, "스냅샷0 ===> ${snapshot.children}")
                Log.i(TAG, "스냅샷1 ===> ${snapshot.children.toList()}")
                Log.i(TAG, "스냅샷2 ===> ${snapshot.child("chat-"+1+"/content").value}")
                Log.i(TAG, "스냅샷3 ===> ${snapshot.getValue()}")
                Log.i(TAG, "스냅샷4 ===> ${snapshot.value}")
                Log.i(TAG, "${snapshot.child("errand").ref}")
                */
                Log.i(TAG, "정보 ===> ${snapshot.child("errand").apply { 
//                    Log.i(TAG, "this가 뭘까"+this.toString())
                    Log.i(TAG, "this.value: "+this.value.toString())
                    if(this.value!=null) this.children.forEach{
//                      it.key는 errandKey,
//                      it.value는 {errand_lng=126.87867, errand_price=123, errand_c... 과 같은 심부름 한 건의 정보 즉, <ReqErrand>
                        Log.i(TAG, "각 심부름의 key: "+it.key.toString())
                        Log.i(TAG, "ReqErrand에 담길 정보: "+it.value.toString())
                        // 파이어베이스의 데이터 정보를 준비해 둔 ReqErrand 객체에 매핑
                        val reqErrandModel: ReqErrand? = it.getValue(ReqErrand::class.java)
                        // errandKey는 키라서 키로 값에 접근할 수 없는 듯하여 직접 대입해주었음
                        reqErrandModel?.errandKey = it.key.toString()
                        /*
                        Log.i(TAG, "reqErrandModel:"+reqErrandModel)
                        Log.i(TAG, "======================= [[ 전체출력해보기 시작 ]] =======================")
                        Log.i(TAG, "errandKey: ${reqErrandModel?.errandKey}")
                        Log.i(TAG, "errand_content: ${reqErrandModel?.errand_content}")
                        Log.i(TAG, "errand_item: ${reqErrandModel?.errand_item}")
                        Log.i(TAG, "errand_item_price_req: ${reqErrandModel?.errand_item_price_req}")
                        Log.i(TAG, "errand_lat: ${reqErrandModel?.errand_lat}")
                        Log.i(TAG, "errand_lng: ${reqErrandModel?.errand_lng}")
                        Log.i(TAG, "errand_price: ${reqErrandModel?.errand_price}")
                        Log.i(TAG, "errand_request_date: ${reqErrandModel?.errand_request_date}")
                        Log.i(TAG, "errand_total_price: ${reqErrandModel?.errand_total_price}")
                        Log.i(TAG, "mem_email: ${reqErrandModel?.mem_email}")
                        Log.i(TAG, "rider: ${reqErrandModel?.rider}")
                        Log.i(TAG, "status: ${reqErrandModel?.status}")
                        Log.i(TAG, "======================= [[ 전체출력해보기 끝 ]] =======================")
                        */

                    }
                }}")
//                Log.i(TAG,"previousChildName: ${previousChildName}")    // previousChildName: 같은 깊이에서 자기 보다 앞에 있는 키의 이름임
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        reqErrandRef.addChildEventListener(reqErrandChildAddListener)

    }
/*
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView 호출 성공")
//        /*  =================================== [[ 테스트용 :: ArrayList ]] =======================================
        // 데이터 담아주기
        val chatOne = ChatList(null, "김은영", "첫번째 테스트 내용")
        val chatTwo = ChatList(null, "이순신도라에몽", "HI!!! HELLO!!!")
        val chatThree = ChatList(null, "강감찬보노보노", "897654565564")
        val chatFour = ChatList(null, "홍길동둘리", "last content")
        chatListData.add(chatOne)
        chatListData.add(chatTwo)
        chatListData.add(chatThree)
        chatListData.add(chatFour)
//        */
        initFirebase()
        chatListBinding = FragmentChatlistBinding.inflate(layoutInflater)
        Log.i(TAG, "chatListBinding: $chatListBinding")
        val view = binding.root
        chatListAdapter = ChatListAdapter()
        binding.rvChatList.layoutManager = LinearLayoutManager(context)
        binding.rvChatList.adapter = chatListAdapter
        chatListAdapter.submitList(chatListData)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView 호출 성공")
        chatListBinding = null
        Log.i(TAG, "chatListBinding: $chatListBinding")
    }
*/
    companion object {
        const val TAG = "mymymy_ReqErrandFrag"
    }
}