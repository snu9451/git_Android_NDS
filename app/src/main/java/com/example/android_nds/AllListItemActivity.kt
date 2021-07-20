package com.example.android_nds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.android_nds.adapter.HistoryAdapter
import com.example.android_nds.adapter.ItemAdapter
import com.example.android_nds.api.ItemService
import com.example.android_nds.databinding.ActivityAllListItemBinding
import com.example.android_nds.model.History
import com.example.android_nds.model.Items
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AllListItemActivity : AppCompatActivity() {
    //메인 UI
    private lateinit var  binding: ActivityAllListItemBinding
    //어댑터 아이템 리스트
    private lateinit var adapter: ItemAdapter
    //어댑터 검색어
    private lateinit var historyadapter: HistoryAdapter
    //get,post url주소
    private lateinit var itemService : ItemService
    //Room 데이타베이스
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ActivityAllListItem을 바인딩 해준다.
        binding = ActivityAllListItemBinding.inflate(layoutInflater)
        //R.layout.activity_all_list_item이 아닌 바인딩한 그 view바인딩.root를 해주면 view를 넣는 것과 같다,
        setContentView(binding.root)

        //아이템 전부 가져오는 리사이클러뷰
        initItemListRecyclerView()
        //검색어를 전부 가져오는 리사이클러뷰 + 여기안에 이벤트들이 들어있음
        initHistoryRecyclerView()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "ItemSearchDB"
        ).build()

        //retroifit으로 내가 연결해야할 서버에 접속
        //Gson으로 컨버터 해주는 부분
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.ip_num))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //인터페이스인 itemService를 가져옴
        itemService = retrofit.create(ItemService::class.java)

        //아이템 목록 받아옴.
        itemListConnect("like_rank")

    }

    private fun initItemListRecyclerView(){
        //아답터를 선언해준다. => 아답터는 데이터를 중간에서 가져와주는 역할을 하는 아이니까!
        adapter = ItemAdapter(itemClickListener = {
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("bm_no",it.bm_no)
            startActivity(intent)
        })
        //우리는 데이터를 담을때 linearlayout에 담을 것이기때문에 LinearLayoutManager를 넣어준다.
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(this)
        //데이터플 ActivityAllListItem의 id에 담아준다
        binding.itemRecyclerView.adapter = adapter
    }

    /////////////////////////[검색 영역]////////////////////////////////
    //검색 관련 리사이클러뷰
    private fun initHistoryRecyclerView(){
        //아답터를 선언해준다. => 아답터는 데이터를 중간에서 가져와주는 역할을 하는 아이니까!
        historyadapter = HistoryAdapter(
            historyDeleteClickListener = {
                deleteSearchKeyword(it)
            }
        )
        //우리는 데이터를 담을때 linearlayout에 담을 것이기때문에 LinearLayoutManager를 넣어준다.
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        //데이터플 ActivityAllListItem의 id에 담아준다
        binding.historyRecyclerView.adapter = historyadapter

        initSearchEditText()
    }

    //검색 관련 이벤트
    private fun initSearchEditText(){
        //keyCode는 타자가 될 때에 대한 이벤트를 설정 할 수 있다.
        //keyCode는 KeyEvent에 저장되어있다.
        //그 중에서도 KEYCODE_ENTER가 발생하면 (KEYCODE_ENTER는 엔터를 의미) 이벤트를 발생시킨다.
        //event.action 중에서도 MotionEvent.ACTION_DOWN는
        //화면에서 ACTION_DOWN은 버튼을 눌렀을 때 , ACTION_UP은 손을 땠을때를 의미한다.
        //따라서, 엔터를 손가락으로 누르고 있을때 이벤트가 발생할 것임을 말해준다.
        binding.searchEditText.setOnKeyListener{v,keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN){
                search(binding.searchEditText.text.toString()) //검색어를 가져옴
                return@setOnKeyListener true //이 이벤트가 실행됨을 의미
            }
            return@setOnKeyListener false //처리가 안된, 다른 행동이 실행 됬기에 false처리가 되겠지!
        }

        binding.searchEditText.setOnTouchListener{v, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                showHistoryView()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false //처리가 안된, 다른 행동이 실행 됬기에 false처리가 되겠지!
        }
    }

    //검색하는 메소드
    private fun search(keyword:String){
        itemService.getItemSearchByName(keyword)
            .enqueue(object: Callback<List<Items>>{
                //api 요청 성공시
                override fun onResponse(call: Call<List<Items>>, response: Response<List<Items>>) {
                    if(response.isSuccessful.not()){
                        Log.e("main_fail","Not Success!!")
                        return
                    }
                    //검색어들 안보이게
                    hideHistoryView()
                    //검색어 저장
                    saveSearchKeyword(keyword)
                    //어답터에 list형태로 담아주기 위해서 필요하다.
                    adapter.submitList(response.body()?.orEmpty())
                }
                //api 요청 실패시
                override fun onFailure(call: Call<List<Items>>, t: Throwable) {
                    Log.e("main_fail",t.toString())
                }

            })
    }
    //검색어들 보여주는 sql문
    private fun showHistoryView(){
        Thread {
            val keywords = db.historyDao().getAll().reversed() //최신순부터 보여주기 위해서 reversed()

            //UI작업을 위한 스레드 인듯!
            runOnUiThread{
                //검색어들이 보이게 
                binding.historyRecyclerView.isVisible = true
                //orEmpty()는 검색어가 없을 시
                historyadapter.submitList(keywords.orEmpty())
            }
        }.start()
    }
    //검색어UI 안보이게
    private fun hideHistoryView(){
        binding.historyRecyclerView.isVisible = false
    }
    //검색어 저장하는 sql문 갔다오기
    private fun saveSearchKeyword(keyword: String){
        Thread {
            db.historyDao().insertHistory((History(null,keyword)))
        }.start()
    }
    //검색어 삭제하는 sql문 갔다오기
    private fun deleteSearchKeyword(keyword: String){
        Thread {
            db.historyDao().deleteHistory(keyword)
            //삭제 후 다시 가져와야해서
            showHistoryView()
        }.start()
    }

    /////////////////////////[메뉴 영역]////////////////////////////////
    private fun replaceView(tab: Fragment){
            supportFragmentManager.beginTransaction()
                .replace(R.id.itemRecyclerView,tab)
                .commit()
    }
    //메뉴 옵션 추가
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_type, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.like_rank ->
                itemListConnect("like_rank")
            R.id.new_rank ->
                itemListConnect("new_rank")
            R.id.categori ->
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainView, ItemCategoriFragment())
                    .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    //최신순이나 신규 가져오기
    private fun itemListConnect(pr_choice : String){
        itemService.getItemByName(pr_choice)
            .enqueue(object: Callback<List<Items>>{
                //api 요청 성공시
                override fun onResponse(call: Call<List<Items>>, response: Response<List<Items>>) {
                    if(response.isSuccessful.not()){
                        Log.e("main_fail","Not Success!!")
                        return
                    }

                    response.body()?.let{
                        Log.d("main_success",it.toString())
                        it.forEach { t ->
                            Log.d("titleName",t.toString())
                        }
                        //어답터에 list형태로 담아주기 위해서 필요하다.
                        adapter.submitList(response.body()?.orEmpty())

                    }
                }
                //api 요청 실패시
                override fun onFailure(call: Call<List<Items>>, t: Throwable) {
                    Log.e("main_fail",t.toString())
                }

            })
    }
}