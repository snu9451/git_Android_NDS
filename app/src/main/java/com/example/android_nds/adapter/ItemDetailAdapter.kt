package com.example.android_nds.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_nds.UrlSave
import com.example.android_nds.databinding.ImgsitemBinding

//이건 이미지들 가져오기 위해서!
class ItemDetailAdapter: ListAdapter<String, ItemDetailAdapter.ItemViewHolder>(diffUtil) {

    //2번
    inner class ItemViewHolder(private val binding: ImgsitemBinding): RecyclerView.ViewHolder(binding.root){
        //Items를 itemsModel이라는 이름으로 받아온다.
        fun bind(itemsModel: String){
            //서버에서 이미지 정보를 가져와서 이미지 로딩 처리하기
            Glide
                .with(binding.biFile.context)
                .load(UrlSave().img_where+itemsModel)
                .into(binding.biFile)
        }
    }


    //1번
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //이너클래스인 얘를 생성시켜준다. => 이 안에 listitem을 바인딩하고, infalte(객체로 만든다)를 한다
        return ItemViewHolder(ImgsitemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    //3번 : onBindViewHolder함수는 생성된 뷰홀더에 데이터를 바인딩 해주는 함수
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                 return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem==newItem
            }

        }
    }
}