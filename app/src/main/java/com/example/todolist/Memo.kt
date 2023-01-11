package com.example.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="memo_table")
data class Memo (
    @PrimaryKey @ColumnInfo(name="memo_id") val id:Int?,
    val writeDate:String,
    val title:String,
    val content:String
        )