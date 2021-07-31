package com.example.android_nds

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_nds.adapter.ItemDetailAdapter
import com.example.android_nds.adapter.ItemDetailCommentsAdapter
import com.example.android_nds.api.ItemService
import com.example.android_nds.databinding.ActivityItemDetailBinding
import com.example.android_nds.interceptor.LoginHandleInterceptor
import com.example.android_nds.model.ItemComments
import com.example.android_nds.model.ItemDetailDto
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//아이템 하나를 자세히 보는 액티비티
class ItemDetailActivity:AppCompatActivity() {
    //메인 UI
    private lateinit var  binding: ActivityItemDetailBinding
    //get,post url주소
    private lateinit var itemService : ItemService
    //어댑터 아이템 디테일
    private lateinit var itemDetailadapter: ItemDetailAdapter
    //어댑터 아이템 디테일
    private lateinit var itemDetailCommentsAdapter: ItemDetailCommentsAdapter
    //글 번호 저장 => 아이템 삭제나 글 등록 후 다시 상품을 가져와야하서
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
        //인터셉터 달아주기
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LoginHandleInterceptor(applicationContext)).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.ip_num))
            .client(okHttpClient)
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

    fun startChatActivity(destEmail: String){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("dest_email", destEmail)
        startActivity(intent)
    }


    ////처음에 아이템 정보를 가져올 때
    private fun initItemDetailRecyclerView(){
        //아답터를 선언해준다. => 아답터는 데이터를 중간에서 가져와주는 역할을 하는 아이니까!
        itemDetailadapter = ItemDetailAdapter()
        //우리는 데이터를 담을때 linearlayout에 담을 것이기때문에 LinearLayoutManager를 넣어준다.
        binding.fileRecyclerView.layoutManager = LinearLayoutManager(this)
        //데이터플 ActivityAllListItem의 id에 담아준다
        binding.fileRecyclerView.adapter = itemDetailadapter
    }
    ////처음에 아이템 댓글들을 가져올 때
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
                        //내용 받아오기
                        binding.bmTitle.text = it.bm_title
                        binding.bmDate.text = it.bm_date
                        binding.bmPrice.text = it.bm_price.toString()
                        binding.bmContent.text = it.bm_content
                        binding.sellerNickname.text = it.seller_nickname
                        binding.bmHit.text = "조회수 "+it.bm_hit.toString()
                        item_no = it.bm_no //번호 저장 -> 그래야 댓글 삭제, 등록할 때 사용 가능
                        // 채팅 연결하기
                        val destEmail: String = it.mem_email
                        // 채팅하기 버튼 클릭 시 이벤트 구현
                        findViewById<Button>(R.id.messageButton).apply {
                            val preferences: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                            val memEmail: String? = preferences.getString("mem_email", "")
                            if(destEmail == memEmail){
                                this.text = "삭제하기"
                                this.isVisible = true
                                this.setOnClickListener {
                                    itemDeleteConnect("sel")
                                    AllListItemFragment.isToFromInsertActivity = true   // 새로고침을 위해
                                }
                            }
                            else {
                                this.text = "판매자와 대화하기"
                                this.isVisible = true
                                this.setOnClickListener {
                                    startChatActivity(destEmail)
                                }
                            }

                            //어댑터 - 댓글 다 가져오기
                            itemDetailCommentsAdapter.submitList(it.itemComments)


//                        var ab :ActionBar?
//                        ab = supportActionBar
                            var ab : Toolbar? = findViewById(R.id.includedToolbar)
                            //만약 나의 판매 물품이면 삭제가 가능
                            ab?.isVisible = it.seller_me==1
                        }

                    }
                }
                //api 요청 실패시
                override fun onFailure(call: Call<ItemDetailDto>, t: Throwable) {
                    Log.e("main_fail",t.toString())
                }

            })
    }

    //메뉴바 만들기 영역 시작//////////////////////////////////////////////////////////////////////////////////////
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //메뉴바에 대한 이벤트 : 아이템 삭제
    override fun onOptionsItemSelected(menuBt: MenuItem): Boolean {
        when(menuBt.itemId){
            R.id.delete_myItem->
                itemDeleteConnect("sel")
        }
        return super.onOptionsItemSelected(menuBt)
    }

    //메뉴바의 이벤트 메소드 => 내 물건 삭제
    private fun itemDeleteConnect(br_sel_buy : String){
        itemService.getItemDeleteByName(item_no,br_sel_buy).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful.not()){
                    Log.e("main_fail","Not Success!!")
                    return
                }
                Toast.makeText(applicationContext,"상품 삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show()
                //finish를 하면 현재 화면이 아예 종료된다.
                finish()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("main_fail",t.toString())
            }

        })
    }
    //메뉴바 만들기 영역 끝///////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////////여기부터는 이벤트 메소드들////////////////////////////////////////////////////
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