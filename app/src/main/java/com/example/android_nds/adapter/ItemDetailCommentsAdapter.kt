package com.example.android_nds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_nds.databinding.CommentsitemBinding
import com.example.android_nds.model.ItemComments

//이건 댓글 가져오기
class ItemDetailCommentsAdapter(private val commentsDeleteListener: (ItemComments) -> Unit) : ListAdapter<ItemComments, ItemDetailCommentsAdapter.ItemViewHolder>(diffUtil) {

    //2번
    inner class ItemViewHolder(private val binding: CommentsitemBinding): RecyclerView.ViewHolder(binding.root){
        //Items를 itemsModel이라는 이름으로 받아온다.
        fun bind(itemsModel: ItemComments){
            //댓글 삭제
            binding.commentDeleteButton.setOnClickListener{
                commentsDeleteListener(itemsModel)
            }
            if(itemsModel.comment_pos==1) {
                //서버에서 댓글 가져와서 댓글 처리
                binding.commentNickname.text = "      "+itemsModel.mem_nickname
                binding.commentDate.text = "       "+itemsModel.comment_date
                //pos가 1이면 대댓글 , 0이면 댓글
                binding.content.text = "     "+itemsModel.comment_msg
            }else{
                //서버에서 댓글 가져와서 댓글 처리
                binding.commentNickname.text = itemsModel.mem_nickname
                binding.commentDate.text = itemsModel.comment_date
                //pos가 1이면 대댓글 , 0이면 댓글
                binding.content.text = itemsModel.comment_msg
            }
            if(itemsModel.comment_me==1){
                binding.commentDeleteButton.isVisible=true
            }
        }

    }


    //1번
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //이너클래스인 얘를 생성시켜준다. => 이 안에 listitem을 바인딩하고, infalte(객체로 만든다)를 한다
        return ItemViewHolder(CommentsitemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    //3번 : onBindViewHolder함수는 생성된 뷰홀더에 데이터를 바인딩 해주는 함수
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ItemComments>(){
            override fun areItemsTheSame(oldItem: ItemComments, newItem: ItemComments): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: ItemComments, newItem: ItemComments): Boolean {
                return oldItem.comment_step==newItem.comment_step
            }

        }
    }
}