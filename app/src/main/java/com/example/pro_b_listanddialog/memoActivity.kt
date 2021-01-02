package com.example.pro_b_listanddialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pro_b_listanddialog.databinding.ActivityMainBinding
import com.example.pro_b_listanddialog.databinding.ActivityMemoBinding

class memoActivity : AppCompatActivity() {
    // 메모 액티비티의 바인딩 생성
    lateinit var MemoBinding : ActivityMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        MemoBinding = ActivityMemoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        // cancel 버튼 클릭시
        MemoBinding.memoActCancelButton2.setOnClickListener {
            val toast = Toast.makeText(this, "취소했습니다", Toast.LENGTH_SHORT)
            toast.show()
            finish()
        }

        // save 버튼 클릭시
        MemoBinding.memoActSaveButton.setOnClickListener {
            val memoIntent = Intent()
            // 입력한 제목 저장하여 전달하기
            memoIntent.putExtra("title", MemoBinding.memoActEditText.text.toString())
            // 입력한 내용 저장하여 전달하기
            memoIntent.putExtra("memo", MemoBinding.memoActEditText2.text.toString())
            // 종료하기 전 이 액티비티의 데이터 정보 저장
            setResult(RESULT_OK, memoIntent)
            // 아무것도 입력하지 않고 "save" 버튼을 누를 시 토스트 출력
            if (MemoBinding.memoActEditText.text.toString() == ""){
                val toast = Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT)
                toast.show()
            }
            finish()
        }
        setContentView(MemoBinding.root)
    }
}