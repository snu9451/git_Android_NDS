package com.example.android_nds.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_nds.databinding.ListitemReqerrandBinding
import com.example.android_nds.model.ReqErrand

class ReqErrandMapAdapter : ListAdapter<ReqErrand, ReqErrandMapAdapter.ReqErrandViewHolder>(diffUtil) {
    // 내부 클래스로 가져야 하는 ViewHolder
    inner class ReqErrandViewHolder(private val binding: ListitemReqerrandBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(reqErrandModel: ReqErrand){
            // 이미지는 Glide 이용하면 될 듯
            binding.tvReqErrandItem.text = reqErrandModel.errand_item
            binding.tvReqErrandLoc.text = "${reqErrandModel.errand_lat}, ${reqErrandModel.errand_lng}"  //★★★★★★★★★★★★한글주소로 수정해야 합니다.
            binding.tvReqErrandErrandPrice.text = reqErrandModel.errand_item_price_req
            binding.tvReqErrandItemPrice.text = reqErrandModel.errand_price
            binding.tvReqErrandTotalPrice.text = "${ reqErrandModel.errand_total_price }"
        }
    }/////////////////////////////////end of inner class: ReqErrandViewHolder

    /* ================================== [[ 미리 만들어진 뷰홀더ViewHolder가 없을 때 실행될 함수 ]] ================================== */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReqErrandViewHolder {
        Log.i(TAG, "onCreateViewHolder 호출 성공")
        return ReqErrandViewHolder(ListitemReqerrandBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /* ============================= [[ 뷰홀더ViewHolder가 바인딩binding되었을 때 실제 데이터를 그려주기 ]] ============================= */
    override fun  onBindViewHolder(holder: ReqErrandViewHolder, position: Int){
        holder.bind(currentList[position])
        Log.i(TAG, "onBindViewHolder 호출 성공")
    }

    /* ======== [[ 리사이클러Recycler의 뷰 포지션의 값이 변경되었지만 똑같은 값일 때 또 가져올 필요가 없으므로 효율적 처리를 위해... ]] ======== */
    companion object{
        const val TAG = "mymymy"
        val diffUtil = object: DiffUtil.ItemCallback<ReqErrand>(){
            // 아이템이 같은지 여부를 체크
            override fun areItemsTheSame(oldItem: ReqErrand, newItem: ReqErrand): Boolean {
                Log.i(TAG, "아이템이 같은지 여부를 체크")
                return oldItem == newItem
            }
            // 내용이 같은지 여부를 체크 - id가 같으면 내용이 같을 것이므로 id로 체크
            override fun areContentsTheSame(oldItem: ReqErrand, newItem: ReqErrand): Boolean {
                // 임시로 내용으로 비교하도록 했음...
                Log.i(TAG, "내용이 같은지 여부를 체크")
                Log.i(TAG, "${oldItem.errandKey}, ${newItem.errandKey}")
                return oldItem.errandKey == newItem.errandKey
            }

        }
    }

}