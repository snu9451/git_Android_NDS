package com.example.android_nds.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.android_nds.databinding.ImgsitemuploadBinding


class PhotoListAdapter (private val removePhotoListener: (Uri) -> Unit) : RecyclerView.Adapter<PhotoListAdapter.ProductItemViewHolder>() {

    private var imageUriList: List<Uri> = listOf()
    //2번
    inner class ProductItemViewHolder(private val binding: ImgsitemuploadBinding): RecyclerView.ViewHolder(binding.root){

        fun bindData(data: Uri) = with(binding){
            Imagefile.setImageURI(data)
        }

        fun bindViews(data: Uri) = with(binding){
            closeButton.setOnClickListener{
                removePhotoListener(data)
            }
        }

    }

    //1번
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = ImgsitemuploadBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductItemViewHolder(view)
    }

    //3번 : onBindViewHolder함수는 생성된 뷰홀더에 데이터를 바인딩 해주는 함수
    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        holder.bindData(imageUriList[position])
        holder.bindViews(imageUriList[position])
    }

    override fun getItemCount(): Int = imageUriList.size

    fun setPhotoList(imageUriList: List<Uri>){
        this.imageUriList = imageUriList
        notifyDataSetChanged()
    }
}