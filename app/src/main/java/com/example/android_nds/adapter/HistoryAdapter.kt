package com.example.android_nds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_nds.databinding.ItemHistoryBinding
import com.example.android_nds.model.History

class HistoryAdapter(val historyDeleteClickListener: (String) -> Unit ): ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
    //2번 : 이너 클래스로 만들어줌 => 얘는 지금  RecyclerView.ViewHolder를 상속받고 있다. 그래서 onCreateViewHolder에서 ItemViewHolder를 리턴해서 가져올 수 있다.
    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        //Items를 itemsModel이라는 이름으로 받아온다.
        fun bind(historyModel: History){
            binding.historyKeywordTextView.text = historyModel.keyword

            binding.historyKeywordDeleteButton.setOnClickListener{
                historyDeleteClickListener(historyModel.keyword.orEmpty())
            }
        }
    }


    //1번
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder{
        //이너클래스인 얘를 생성시켜준다. => 이 안에 listitem을 바인딩하고, infalte(객체로 만든다)를 한다
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    //3번 : onBindViewHolder함수는 생성된 뷰홀더에 데이터를 바인딩 해주는 함수
    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<History>(){
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword==newItem.keyword
            }

        }
    }
}