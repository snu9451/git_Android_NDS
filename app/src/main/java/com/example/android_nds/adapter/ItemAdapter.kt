package com.example.android_nds.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_nds.AllListItemFragment
import com.example.android_nds.UrlSave
import com.example.android_nds.databinding.ListitemBinding
import com.example.android_nds.model.Items

class ItemAdapter(private val itemClickListener: (Items) -> Unit): ListAdapter<Items, ItemAdapter.ItemViewHolder>(diffUtil) {

    //2번 : 이너 클래스로 만들어줌 => 얘는 지금  RecyclerView.ViewHolder를 상속받고 있다. 그래서 onCreateViewHolder에서 ItemViewHolder를 리턴해서 가져올 수 있다.
    inner class ItemViewHolder(private val binding: ListitemBinding): RecyclerView.ViewHolder(binding.root){
        //Items를 itemsModel이라는 이름으로 받아온다.
        fun bind(itemsModel: Items){
            binding.root.setOnClickListener{
                itemClickListener(itemsModel)
            }
            binding.bmTitle.text = itemsModel.bm_title
            //서버에서 이미지 정보를 가져와서 이미지 로딩 처리하기
            Glide
                .with(binding.biFile.context)
                .load(UrlSave().img_where+itemsModel.bi_file)
                .into(binding.biFile)
            binding.bmDate.text  = itemsModel.bm_date
            binding.bmPrice.text = itemsModel.bm_price.toString()
        }
    }


    //1번
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //이너클래스인 얘를 생성시켜준다. => 이 안에 listitem을 바인딩하고, infalte(객체로 만든다)를 한다
        return ItemViewHolder(ListitemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    //3번 : onBindViewHolder함수는 생성된 뷰홀더에 데이터를 바인딩 해주는 함수
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Items>(){
            override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean {
                 return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean {
                return oldItem.bm_no==newItem.bm_no
            }

        }
    }
}