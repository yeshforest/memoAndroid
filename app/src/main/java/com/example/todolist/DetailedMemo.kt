package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todolist.databinding.ActivityDetailedItemBinding
import com.example.todolist.databinding.ActivityDetailedMemoBinding

class DetailedMemo : AppCompatActivity() {
    private lateinit var binding:ActivityDetailedMemoBinding
    private lateinit var getResultText:ActivityResultLauncher<Intent>
    private lateinit var memoItem:Memo

    private fun setMemoItem(){
        getResultText=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result->
            if(result.resultCode== RESULT_OK){
                var temp=Memo(null,"실패","실패","실패","실패")
                memoItem= (result.data?.getSerializableExtra("memoItem")?:temp) as Memo
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailedMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

     //   setMemoItem()
        var temp=Memo(null,"실패","실패","실패","실패")
        memoItem= (intent.getSerializableExtra("memoItem")?:temp) as Memo
        binding.etTitle.setText(memoItem.title)
        binding.etContent.setText(memoItem.content)

        binding.btnDelete.setOnClickListener {
         //   var intent=Intent(this@DetailedMemo,MainActivity::class.java)
         //   intent.putExtra("isDelete",true)
         //   startActivity(intent)
        //    finish()
        }

    }


}