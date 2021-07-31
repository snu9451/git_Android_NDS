package com.example.android_nds

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager

import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_nds.adapter.PhotoListAdapter
import com.example.android_nds.api.ItemService
import com.example.android_nds.databinding.ActivityItemInsertBinding

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import kotlin.collections.HashMap



//아이템 등록하는 액티비티
class ItemInsertActivity : AppCompatActivity() {
    private var imageName: String? = null
    //이미지 등록 url
    private var selectedUri: ArrayList<Uri> = arrayListOf()
    //get,post url주소
    private lateinit var itemService : ItemService
    lateinit var imagepath : String
    //메인 UI
    private lateinit var iteminsertbinding: ActivityItemInsertBinding
    //사진 목록 가져오기
    private val photoListAdapter = PhotoListAdapter{uri -> removePhoto(uri)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iteminsertbinding = ActivityItemInsertBinding.inflate(layoutInflater)
        setContentView(iteminsertbinding.root)

        AllListItemFragment.isToFromInsertActivity = true

        //카테고리 스피너 담기
        val spinner: Spinner = iteminsertbinding.insertItemCategori
        ArrayAdapter.createFromResource(
            this,
            R.array.insertItemCategori,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        //이미지 등록하기 버튼 클릭 시 권한확인
        findViewById<Button>(R.id.insertItemImgButton).setOnClickListener{
            when{
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else ->{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1010)
                }
            }
        }

        //retroifit으로 내가 연결해야할 서버에 접속
        //Gson으로 컨버터 해주는 부분
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.ip_num))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //인터페이스인 itemService를 가져옴
        itemService = retrofit.create(ItemService::class.java)

        //상품을 올리는 버튼
        findViewById<Button>(R.id.insertItemButton).setOnClickListener {
            val title = findViewById<EditText>(R.id.insertItemTitle)
            val price = findViewById<EditText>(R.id.insertItemPrice)
            val spinner = findViewById<Spinner>(R.id.insertItemCategori)
            val categoriChoice = spinner.selectedItem.toString()
            val content = findViewById<EditText>(R.id.insertItemText)

            //값이 비워 있을 시
            if(title.text.isEmpty() || price.text.isEmpty() || categoriChoice.isEmpty() || content.text.isEmpty()){
                Toast.makeText(applicationContext,"모든 값을 채워주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val title1 = RequestBody.create(MediaType.parse("text/plain"), title.text.toString());
            val price1 = RequestBody.create(MediaType.parse("text/plain"), price.text.toString());
            val categoriChoice1 = RequestBody.create(MediaType.parse("text/plain"), categoriChoice);
            val contents1 = RequestBody.create(MediaType.parse("text/plain"), content.text.toString());

            // 브라우저의 쿠키를 안드로이드에서 구현하기 위함.
            val preferences: SharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val memNickname: String? = preferences.getString("mem_nickname", "")

            val nickName1 = RequestBody.create(MediaType.parse("text/plain"), memNickname.toString());

            //중간에 이미지가 있으면 업로드 과정을 추가
            if(selectedUri.isNotEmpty()){
                //이미지 넣는 부분
                //imagepath = getRealPathFromURI(selectedUri).toString()
                // 여러 file들을 담아줄 ArrayList
                var files : HashMap<String,MultipartBody.Part> = hashMapOf()
                for (i in 0..4) {
                    if(selectedUri.size>i) {
                        imagepath = getRealPathFromURI(selectedUri.get(i)).toString()
                        val file = File(imagepath)
                        // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
                        val requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), file)
                        val body =
                            MultipartBody.Part.createFormData("file"+i, file.getName(), requestFile)
                        files.put("file" + i, body)
                    }
                }

                //내용 넣는 부분
                val requestMap = HashMap<String, RequestBody>();
                requestMap.put("pr_BM_TITLE", title1);
                requestMap.put("pr_BM_PRICE", price1);
                requestMap.put("pr_CATEGORY_NAME", categoriChoice1);
                requestMap.put("pr_BM_CONTENT", contents1);
                requestMap.put("pr_SELLER_NICKNAME", nickName1);
                //상품 등록하기
                itemInsert(requestMap,files)
            }
            //finish를 하면 현재 화면이 아예 종료된다.
            finish()

        }


    }

    private fun initViews(){
        //우리는 데이터를 담을때 linearlayout에 담을 것이기때문에 LinearLayoutManager를 넣어준다.
        iteminsertbinding.insertItemImg.layoutManager = LinearLayoutManager(this)
        //데이터플 ActivityAllListItem의 id에 담아준다
        iteminsertbinding.insertItemImg.adapter = photoListAdapter
    }


    //상품 업로드하기
    private fun itemInsert(requestMap : HashMap<String, RequestBody>, files: HashMap<String,MultipartBody.Part>){
        val file1 = files["file0"]
        val file2 = files["file1"]
        val file3 = files["file2"]
        val file4 = files["file3"]
        val file5 = files["file4"]
        itemService.getItemInsertByName(requestMap,file1 ,file2,file3, file4, file5)
            .enqueue(object: Callback<ResponseBody>{
                //api 요청 성공시
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful.not()){
                        Log.e("main_fail","Not Success!!")
                        Toast.makeText(applicationContext,"사진 업로드 실패!!...",Toast.LENGTH_SHORT).show()
                        return
                    }
                    Toast.makeText(applicationContext,"상품이 등록되었습니다.",Toast.LENGTH_SHORT).show()
                }

                //api 요청 실패시
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("main_fail",t.toString())
                    Toast.makeText(applicationContext,"사진 업로드 실패2222!!...",Toast.LENGTH_SHORT).show()
                }

            })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            1010 ->
                if(grantResults.isNotEmpty() && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    startContentProvider()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun startContentProvider() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2020)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            return
        }

        when(requestCode){
            2020 ->{
                val uri = data?.data
                if (uri != null) {
                    if(selectedUri.size>4){
                        Toast.makeText(this,"사진은 5개까지 가능합니다",Toast.LENGTH_SHORT).show()
                    }else{
                        selectedUri.add(uri)
                        photoListAdapter.setPhotoList(selectedUri)
                        //이미지 리사이클러뷰
                        initViews()
                    }
                }
            }
            else ->{
                Toast.makeText(this,"사진을 가져오지 못했습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }



    fun getRealPathFromURI(contentUri: Uri?): String? {
        if (contentUri?.path?.startsWith("/storage") == true) {
            return contentUri.path.toString();
        }
        val id = DocumentsContract.getDocumentId(contentUri).split(":")[1]
        val columns = MediaStore.Files.FileColumns.DATA
        val selection = MediaStore.Files.FileColumns._ID + " = " + id;
        val cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"),
            arrayOf(columns), selection, null, null)
        try {
            val columnIndex = cursor?.getColumnIndex(columns[0].toString())
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    return cursor.getString(0)
                }
            }
        } finally {
            cursor?.close();
        }
        return null
    }


    private fun showPermissionContextPopup(){
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의"){_, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1010)
            }
    }


    //사진 삭제
    private fun removePhoto(uri: Uri){
        selectedUri.remove(uri)
        photoListAdapter.setPhotoList(selectedUri)
    }
}