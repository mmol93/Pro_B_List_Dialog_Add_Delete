package com.example.pro_b_listanddialog

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
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

            titleData = data1
            memoData = data2

            // 받은 데이터를 토대로 리스트뷰 만들기
            listData1.add(data1)

            for (i in listData1.indices){
                val map =HashMap<String, Any?>()
                map["data1"] = listData1[i]

                listTotal.add(map)
            }
            val keys = arrayOf("data1")
            val viewPosition = intArrayOf(R.id.list_textView)

            // 리스트뷰를 위한 어댑터 생성
            val listAdapter = SimpleAdapter(this, listTotal, R.layout.list_layout1, keys, viewPosition)
            MainBinding.listView.adapter = listAdapter
            listData1.clear()

            // 리스트뷰의 항목을 길게 눌렀을 때
            MainBinding.listView.setOnItemLongClickListener(listener1)
        }
    }
    // setOnItemLongClickListener 에 대한 리스너 정의
    val listener1 = object : AdapterView.OnItemLongClickListener{
        override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
            when(parent?.id){
                R.id.listView -> {
                    val dialogBuilder = AlertDialog.Builder(this@MainActivity)

                    dialogBuilder.setTitle("$titleData")
                    dialogBuilder.setNegativeButton("취소", null)

                    // 다이얼로그에 리스트뷰를 세팅하는 동시에 리스너 정의(고차함수)
                    dialogBuilder.setItems(dialogArray){ dialogInterface: DialogInterface, i: Int ->
                        // i 인덱스는 0부터 시작함
                        if (i == 1){
                            listTotal.removeAt(position)
                        }
                    }
                    dialogBuilder.show()
                }
            }
            return true
        }
    }
}