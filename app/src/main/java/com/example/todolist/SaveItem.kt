package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.databinding.ActivityDetailedItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SaveItem : AppCompatActivity() {
    private lateinit var binding:ActivityDetailedItemBinding
    private lateinit var myDao:MemoDao
    private var madapter:CustomAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityDetailedItemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




//main(write버튼 클릭) -> saveItem 저장버튼 클릭(값 main으로 옮기기) -> main에서 recyclerview에 저장, db io
        binding.btnClose.setOnClickListener {
            //화면 닫는 로직(main화면으로)
            var intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSave.setOnClickListener {

                var et_title=binding.etTitle.text.toString()
                var et_content=binding.etContent.text.toString()
                var currentTime= SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())


            var intent=Intent(this@SaveItem,MainActivity::class.java)
            intent.putExtra("et_title",et_title)
            intent.putExtra("et_content",et_content)
            intent.putExtra("currentTime",currentTime)
            setResult(RESULT_OK,intent)
            finish()



        }





    }
}