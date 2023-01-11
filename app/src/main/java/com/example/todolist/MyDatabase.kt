package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities= [Memo::class],version=1)
abstract class MyDatabase:RoomDatabase() {
    abstract fun getMemoDao():MemoDao

    //room instance - static singleton으로 구현
    companion object{
        private var INSTANCE:MyDatabase?=null
        fun getDatabase(context: Context):MyDatabase{
            if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(
                context,MyDatabase::class.java,"memoDB"

            ).build()
            }
            return INSTANCE as MyDatabase
        }
    }


}