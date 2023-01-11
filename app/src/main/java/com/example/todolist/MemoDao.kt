package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoDao {
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo:Memo)

    @Query("SELECT * FROM memo_table ORDER BY writeDate DESC")
    fun getAllMemos():
            LiveData<List<Memo>>

    @Query("SELECT * FROM memo_table WHERE title= :searchTitle")
    suspend fun getMemoByTitle(searchTitle:String):List<Memo>

    @Delete
    suspend fun deleteMemo(memo:Memo)

    @Update
    fun updateMemo(memo:Memo)
}
