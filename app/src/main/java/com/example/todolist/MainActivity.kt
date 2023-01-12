package com.example.todolist

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.DialogEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var memoItems:ArrayList<Memo>
    private lateinit var myDao:MemoDao
    private var madapter:CustomAdapter?=null


    private fun loadRecentDB() {
        CoroutineScope(Dispatchers.IO).launch {
            memoItems = myDao.getAllMemos() as ArrayList<Memo>
            if (madapter == null) {
                madapter= CustomAdapter(memoItems,this@MainActivity)
                binding.rvTodo.setHasFixedSize(true)
                binding.rvTodo.adapter = madapter
            }
        }



    }
    private fun setInit(){
        var dialogbinding= DialogEditBinding.inflate(layoutInflater)
        myDao=MyDatabase.getDatabase(this).getMemoDao()
        memoItems= ArrayList<Memo>()

        loadRecentDB()

        binding.btnWrite.setOnClickListener {


            //팝업창 띄우기
            var dialog: Dialog = Dialog(this,android.R.style.Theme_Material_Light_Dialog)
            dialog.setContentView(dialogbinding.root)
            dialog.setCancelable(false)
            var dialogParentView: ViewGroup =dialogbinding.root.parent as ViewGroup


            dialogbinding.btnSave.setOnClickListener{

                var et_title=dialogbinding.etTitle.text.toString()
                var et_content=dialogbinding.etContent.text.toString()
                var currentTime=SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())




                //Insert Database
                CoroutineScope(Dispatchers.IO).launch {
                    myDao.insertMemo(Memo(null,currentTime,et_title,et_content,null))

                    //Insert UI
                    var item:Memo=Memo(null,currentTime,et_title,et_content,null)
                    madapter!!.addItem(item)

                }
                binding.rvTodo.smoothScrollToPosition(0)
                dialogbinding.etTitle.setText("")
                dialogbinding.etContent.setText("")
                dialog.dismiss()
                dialogParentView.removeView(dialogbinding.root)
                Toast.makeText(applicationContext,"추가 완료!",Toast.LENGTH_SHORT).show()

            }
        dialog.show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setInit()



        }



}