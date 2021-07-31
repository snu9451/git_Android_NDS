package com.example.android_nds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.android_nds.databinding.FragmentOneErrandBinding

class OneErrandFragment : Fragment() {

    private var oneErrandBinding: FragmentOneErrandBinding? = null
    private val binding get()= oneErrandBinding!!
    // 심부름의 정보를 담을 자료구조 맵 선언 - mainActivity에서 객체주입할 예정, 콜백을 제공하는 싱글턴임
    private var ndsFirebaseSync: NDSFirebaseSync? = null
    private var errandKey: String? = null

    companion object {
        private const val TAG = "mymymy"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        oneErrandBinding = FragmentOneErrandBinding.inflate(layoutInflater)
        val view = binding.root

        // 닫기 버튼 클릭 시 이벤트 구현
        view.findViewById<Button>(R.id.one_errand_back).setOnClickListener {
            Log.i(TAG,"닫기 버튼 클릭!!")
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().remove(this@OneErrandFragment).commit()
            fragmentManager.popBackStack()
            (activity as MainActivity).currentFragment = null
        }

        // 채팅하기 버튼 클릭 시 이벤트 구현
        view.findViewById<Button>(R.id.one_errand_chat).setOnClickListener {
            Log.i(TAG,"채팅하기 버튼 클릭!!")
//            (activity as MainActivity).apply {
//                this.removeFragment()
//                this.currentFragment = ChatFragment()
//                this.replaceFragment(this.currentFragment as ChatFragment)
//            }
            var destEmail = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("mem_email")
            Log.i(TAG,"$destEmail")
            (activity as MainActivity).startChatActivity(destEmail.toString())
        }



        // ndsFirebaseSync가 null이 아니라면 해당 키(errandKey)를 갖는 심부름 정보를 가져온다.
        if(ndsFirebaseSync!=null){
            Log.i(TAG, "클릭된 마커의 심부름2: ${ndsFirebaseSync?.allErrandMap?.get(errandKey)}")
            val errandItem = view.findViewById<TextView>(R.id.one_errand_item)
            val errandContent = view.findViewById<TextView>(R.id.one_errand_content)
            val errandItemPrice = view.findViewById<TextView>(R.id.one_errand_item_price)
            val errandErrandPrice = view.findViewById<TextView>(R.id.one_errand_errand_price)
            val errandTotalPrice = view.findViewById<TextView>(R.id.one_errand_total_price)
            val errandJuso = view.findViewById<TextView>(R.id.one_errand_position)
//            val errandLat = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("errand_lat")
//            val errandLng = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("errand_lng")
//            var errandKorJuso = "$errandLat, $errandLng"
            var errandKorJuso = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("juso")
            errandItem?.text = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("errand_item")
            errandContent?.text = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("errand_content")
            errandItemPrice?.text = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("errand_item_price_req")
            errandErrandPrice?.text = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("errand_price")
            errandTotalPrice?.text = ndsFirebaseSync?.allErrandMap?.get(errandKey)?.get("errand_total_price")
            errandJuso.text = errandKorJuso
        }
        return view
    }

    // 닫기 버튼 클릭 시
//    fun onCloseClick(view: View){
//        Log.i(TAG, "닫기버튼 클릭!!!")
//    }


    // S E T T E R
    fun setNdsFirebaseSync(ndsFirebaseSync: NDSFirebaseSync){
        this.ndsFirebaseSync = ndsFirebaseSync
    }
    fun setErrandKey(errandKey: String){
        this.errandKey = errandKey
    }
}