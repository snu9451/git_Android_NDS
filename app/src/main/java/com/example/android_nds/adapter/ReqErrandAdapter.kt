package com.example.android_nds.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_nds.databinding.ListitemReqerrandBinding
import com.example.android_nds.model.ReqErrand

class ReqErrandAdapter {}
//
//class ReqErrandAdapter : ListAdapter<ReqErrand, ReqErrandAdapter.ReqErrandViewHolder>(diffUtil) {
//    // 내부 클래스로 가져야 하는 ViewHolder
//    inner class ReqErrandViewHolder(private val binding: ListitemReqerrandBinding): RecyclerView.ViewHolder(binding.root){
//        fun bind(reqErrandModel: ReqErrand){
//            // 이미지는 Glide 이용하면 될 듯
//            binding.chatNickname.text = chatListModel.chat_nickname
//            binding.chatContent.text = chatListModel.chat_content
//            /*
//            // 서버로부터 이미지 정보를 가져와서 이미지 로딩을 처리하기
//            Glide
//                .with(binding.coverImageView.context)  // binding이 가리키는 것은 xml 즉, ViewGroup
//                .load(bookModel.coverSmallUrl)
////                .centerCrop()
////                .placeholder(R.drawable.loading_spinner)
//                .into(binding.coverImageView)
//
//             */
//        }
//    }/////////////////////////////////end of inner: ChatListViewHolder
//
//}