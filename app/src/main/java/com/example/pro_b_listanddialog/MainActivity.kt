package com.example.pro_b_listanddialog

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pro_b_listanddialog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // 메인뷰 바인딩하기
    lateinit var MainBinding : ActivityMainBinding

    // 메모 액티비티의 반환 값
    val MemoActivityIdx = 100

    // 리스트뷰의 항목 변수
    val listData1 = mutableListOf<String>()
    val listTotal = ArrayList<HashMap<String, Any?>>()

    var titleData = ""
    var memoData = ""

    val dialogArray = arrayOf("수정", "삭제")

    override fun onCreate(savedInstanceState: Bundle?) {
        MainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainBinding.button.setOnClickListener {
            // memoActivitty의 인텐트 생성
            val memoActIntent = Intent(this, memoActivity::class.java)
            startActivityForResult(memoActIntent, MemoActivityIdx)
        }
        setContentView(MainBinding.root)
    }

    // 다른 액티비티에서 돌아왔을 때 행동 정의
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MemoActivityIdx){
            // 직전 액티비티에서 보낸 데이터 받기
            val data1 = data?.getStringExtra("title").toString()
            val data2 = data?.getStringExtra("memo").toString()
            MainBinding.textView.text = data1
            if (data1 == "null" || data1 == ""){
                MainBinding.textView.text = "아무 데이터도 받지 못함"
            }
            else {
                titleData = data1
                memoData = data2

                // 받은 데이터를 토대로 리스트뷰 만들기
                listData1.add(data1)

                for (i in listData1.indices) {
                    val map = HashMap<String, Any?>()
                    map["data1"] = listData1[i]

                    listTotal.add(map)
                }
                val keys = arrayOf("data1")
                val viewPosition = intArrayOf(R.id.list_textView)

                // 리스트뷰를 위한 어댑터 생성
                val listAdapter =
                    SimpleAdapter(this, listTotal, R.layout.list_layout1, keys, viewPosition)
                MainBinding.listView.adapter = listAdapter
                listData1.clear()

                // 리스트뷰의 항목을 길게 눌렀을 때
                // AlertDialog 소환
                MainBinding.listView.setOnItemLongClickListener(listener1)
            }
        }
    }
    // setOnItemLongClickListener 에 대한 리스너 정의
    val listener1 = object : AdapterView.OnItemLongClickListener{
        // position: 0부터 시작함
        override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
            when(parent?.id){
                R.id.listView -> {
                    val dialogBuilder = AlertDialog.Builder(this@MainActivity)
                    var title = MainBinding.listView.getItemAtPosition(position).toString()
                    // getItemAtPosition을 사용해서 listView의 아이템 타이틀을 들고오더라도
                    // Map 형태의 데이터이기 때문에 key값이 같이 들어간 데이터가 들어온다
                    // 예: {data1=1234}
                    // 그래서 replace로 key값 부분을 제거해준다
                    title = title.replace("{data1=", "")
                    title = title.replace("}","")
                    dialogBuilder.setTitle("$title")
                    dialogBuilder.setNegativeButton("취소", null)

                    // 다이얼로그에 리스트뷰를 세팅하는 동시에 리스너 정의(고차함수)
                    dialogBuilder.setItems(dialogArray){ dialogInterface: DialogInterface, i: Int ->
                        // i 인덱스는 0부터 시작함
                        // Dialog의 "삭제" 버튼을 눌렀을 때
                        if (i == 1){
                            listTotal.removeAt(position)
                            // 삭제 후 ListView를 재생성해야 갱싱된 화면이 보인다
                            val keys = arrayOf("data1")
                            val viewPosition = intArrayOf(R.id.list_textView)

                            // 리스트뷰를 위한 어댑터 생성
                            val listAdapter =
                                SimpleAdapter(this@MainActivity, listTotal, R.layout.list_layout1, keys, viewPosition)
                            MainBinding.listView.adapter = listAdapter
                            listData1.clear()
                        }
                        // Dialog의 "수정" 버튼을 눌렀을 때
                        if (i == 0){

                        }
                    }
                    dialogBuilder.show()
                }
            }
            return true
        }
    }
}