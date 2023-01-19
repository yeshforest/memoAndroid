package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.databinding.ActivityDetailedItemBinding
import com.example.todolist.databinding.ActivityDetailedMemoBinding

class DetailedMemo : AppCompatActivity() {
    private lateinit var binding:ActivityDetailedMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailedMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}