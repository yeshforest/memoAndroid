package com.example.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.DialogEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var memoItems:ArrayList<Memo>
    private lateinit var myDao:MemoDao
    private var madapter:CustomAdapter?=null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

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
    //registerForActivityResult 등록
    private fun setResultWrite(){
        resultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode== Activity.RESULT_OK){
                val currentTime=result.data?.getStringExtra("currentTime") ?:"시간 저장 실패"
                val et_title=result.data?.getStringExtra("et_title")?:"제목 저장 실패"
                val et_content=result.data?.getStringExtra("et_content")?:"내용 저장 실패"

                //Insert Database
                CoroutineScope(Dispatchers.IO).launch {
                    myDao.insertMemo(Memo(null,currentTime,et_title,et_content,null))

                    //Insert UI
                    var item:Memo=Memo(null,currentTime,et_title,et_content,null)
                    madapter!!.addItem(item)

                }
                binding.rvTodo.smoothScrollToPosition(0)
                Toast.makeText(applicationContext,"추가 완료!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun setInit(){
        myDao=MyDatabase.getDatabase(this).getMemoDao()
        memoItems= ArrayList<Memo>()

        loadRecentDB()
        setResultWrite()

        binding.btnWrite.setOnClickListener {

            var intent= Intent(this,SaveItem::class.java)
            resultLauncher.launch(intent)

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setInit()
        }



}