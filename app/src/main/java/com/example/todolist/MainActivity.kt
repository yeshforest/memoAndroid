package com.example.todolist

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.todolist.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var memoItems:ArrayList<Memo>
    private lateinit var myDao:MemoDao
    private fun setInit(){
        memoItems= ArrayList<Memo>()
        binding.btnWrite.setOnClickListener {

            //팝업창 띄우기
            var dialog: Dialog = Dialog(applicationContext,android.R.style.Theme_Material_Light_Dialog)
            dialog.setContentView(R.layout.dialog_edit)
            var et_title:EditText=dialog.findViewById(R.id.et_title)
            var et_content:EditText=dialog.findViewById(R.id.et_content)
            var btn_save:Button=dialog.findViewById(R.id.btn_save)
            btn_save.setOnClickListener{
                //Insert Database
                var currentTime:String=SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                runBlocking {
                    myDao.insertMemo(Memo(null,currentTime,et_title.getText().toString(),et_content.getText().toString()))

                }

                //Insert UI
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInit()
        myDao=MyDatabase.getDatabase(this).getMemoDao()
        runBlocking {
            myDao.insertMemo(Memo(1,"today","김밥","맛있다. 두개먹자."))
            myDao.insertMemo(Memo(2,"tomorrow","아몰라","잠이나 잘까"))

        }
        val allMemos=myDao.getAllMemos()
        allMemos.observe(this){
            val str=StringBuilder().apply{
                for((id,title,content) in it){
                    append(id)
                    append("-")
                    append(title)
                    append("\n")
                    append(content)
                    append("\n")
                }
            }.toString()
            Log.d("result",str)
        }

    }

}