package com.example.android_nds

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_nds.adapter.ItemDetailAdapter
import com.example.android_nds.adapter.ItemDetailCommentsAdapter
import com.example.android_nds.api.ItemService
import com.example.android_nds.databinding.ActivityItemDetailBinding
import com.example.android_nds.model.ItemComments
import com.example.android_nds.model.ItemDetailDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemDetailActivity:AppCompatActivity() {
    //메인 UI
    private lateinit var  binding: ActivityItemDetailBinding
    //get,post url주소
    private lateinit var itemService : ItemService
    //어댑터 아이템 디테일
    private lateinit var itemDetailadapter: ItemDetailAdapter
    //어댑터 아이템 디테일
    private lateinit var itemDetailCommentsAdapter: ItemDetailCommentsAdapter
    //글 번호 저장
    private var item_no : Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ActivityAllListItem을 바인딩 해준다.
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        //R.layout.activity_all_list_item이 아닌 바인딩한 그 view바인딩.root를 해주면 view를 넣는 것과 같다,
        setContentView(binding.root)

        //아이템 디테일 가져오기 - 어댑터
        initItemDetailRecyclerView()
        //아이템 댓글 가져오기 - 어댑터
        initItemCommentRecyclerView()
        //retroifit으로 내가 연결해야할 서버에 접속
        //Gson으로 컨버터 해주는 부분
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.ip_num))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스인 itemService를 가져옴
        itemService = retrofit.create(ItemService::class.java)
        //아이템 번호 넘기기, 실패시 1로 넘겨 받기
        itemDetailConnect(intent.getIntExtra("bm_no",1))

        val commentButton = findViewById<Button>(R.id.commentButton)
        commentButton.setOnClickListener{
            val comment : EditText = findViewById(R.id.commentEditText)
            //단 댓글이 없을 시
            if(comment.text.isEmpty()){
                Toast.makeText(applicationContext,"댓글을 입력해주세요!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //댓글 달기
            itemDetailCommentInsert(comment.text.toString())
            comment.setText("")
            //댓글들 다시 가져오기
            itemDetailCommentsSelect(item_no)
        }
    }

    private fun initItemDetailRecyclerView(){
        //아답터를 선언해준다. => 아답터는 데이터를 중간에서 가져와주는 역할을 하는 아이니까!
        itemDetailadapter = ItemDetailAdapter()
        //우리는 데이터를 담을때 linearlayout에 담을 것이기때문에 LinearLayoutManager를 넣어준다.
        binding.fileRecyclerView.layoutManager = LinearLayoutManager(this)
        //데이터플 ActivityAllListItem의 id에 담아준다
        binding.fileRecyclerView.adapter = itemDetailadapter
    }
    private fun initItemCommentRecyclerView(){
        //아답터를 선언해준다. => 아답터는 데이터를 중간에서 가져와주는 역할을 하는 아이니까!
        itemDetailCommentsAdapter = ItemDetailCommentsAdapter(commentsDeleteListener = {
            commentDelete(it)
        })
        //우리는 데이터를 담을때 linearlayout에 담을 것이기때문에 LinearLayoutManager를 넣어준다.
        binding.itemCommentsRecyclerView.layoutManager = LinearLayoutManager(this)
        //데이터플 ActivityAllListItem의 id에 담아준다
        binding.itemCommentsRecyclerView.adapter = itemDetailCommentsAdapter
    }

    //아이템 디테일 가져오기
    private fun itemDetailConnect(pr_bm_no : Int){
        itemService.getItemDetailByName(pr_bm_no)
            .enqueue(object: Callback<ItemDetailDto> {
                //api 요청 성공시
                override fun onResponse(call: Call<ItemDetailDto>, response: Response<ItemDetailDto>) {
                    if(response.isSuccessful.not()){
                        Log.e("main_fail","Not Success!!")
                        return
                    }

                    response.body()?.let{
                        Log.d("main_success",it.toString())
                        it.itemImgs.forEach { t ->
                            Log.d("titleName",t.toString())
                        }
                        //어답터에 list형태로 담아주기 위해서 필요하다. - 이미지
                        itemDetailadapter.submitList(it.itemImgs)
                        //@SerializedName("BM_STATUS") val bm_status: String,
                        //@SerializedName("I_LIKE")val i_like: Int,
                        //@SerializedName("seller_me")val seller_me:Int,
                        //내용 받아오기
                        binding.bmTitle.text = it.bm_title
                        binding.bmDate.text = it.bm_date
                        binding.bmPrice.text = it.bm_price.toString()
                        binding.bmContent.text = it.bm_content
                        binding.sellerNickname.text = it.seller_nickname
                        binding.bmHit.text = "조회수 "+it.bm_hit.toString()
                        item_no = it.bm_no //번호 저장 -> 그래야 댓글 삭제, 등록할 때 사용 가능
                        //어댑터 - 댓글 다 가져오기
                        itemDetailCommentsAdapter.submitList(it.itemComments)

                    }
                }
                //api 요청 실패시
                override fun onFailure(call: Call<ItemDetailDto>, t: Throwable) {
                    Log.e("main_fail",t.toString())
                }

            })
    }

    //댓글 삭제
    private fun commentDelete(it: ItemComments) {
        //댓글 삭제후
        itemDetailCommentDelete(it.comment_step)
        //댓글들 다시 가져오기
        itemDetailCommentsSelect(item_no)
    }
    //댓글 달기
    private fun itemDetailCommentInsert(comment : String) {
        itemService.getCommentInsertByName(comment,0,0,item_no)
            .enqueue(object: Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful.not()){
                        Log.e("main_fail","Not Success!!")
                        return
                    }
                    Toast.makeText(applicationContext,"댓글이 입력되었습니다.",Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("main_fail",t.toString())
                }

            })
    }
    //댓글 삭제
    private fun itemDetailCommentDelete(comment_step: Int) {
        itemService.getCommentDeleteByName(comment_step).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful.not()){
                    Log.e("main_fail","Not Success!!")
                    return
                }
                Toast.makeText(applicationContext,"삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("main_fail",t.toString())
            }

        })
    }
    //댓글 가져오기
    private fun itemDetailCommentsSelect(pr_bm_no: Int) {
        itemService.getItemDetailByName(pr_bm_no)
            .enqueue(object: Callback<ItemDetailDto> {
                //api 요청 성공시
                override fun onResponse(call: Call<ItemDetailDto>, response: Response<ItemDetailDto>) {
                    if(response.isSuccessful.not()){
                        Log.e("main_fail","Not Success!!")
                        return
                    }
                    response.body()?.let{
                        Log.d("main_success",it.toString())
                        it.itemImgs.forEach { t ->
                            Log.d("titleName",t.toString())
                        }
                        //어댑터 - 댓글 다 가져오기
                        itemDetailCommentsAdapter.submitList(it.itemComments)

                    }
                }
                //api 요청 실패시
                override fun onFailure(call: Call<ItemDetailDto>, t: Throwable) {
                    Log.e("main_fail",t.toString())
                }

            })
    }
}
