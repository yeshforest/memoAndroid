package com.example.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.DialogEditBinding
import com.example.todolist.databinding.ItemListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(val mI:ArrayList<Memo>,val c:Context ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>()
{
   private val myDao:MemoDao
   private var memoItems= ArrayList<Memo>()
   private var context: Context = c

    init{
        myDao=MyDatabase.getDatabase(context).getMemoDao()
        memoItems.addAll(mI)
    }

    inner class ViewHolder(val binding: ItemListBinding, val dialogbinding:DialogEditBinding) : RecyclerView.ViewHolder(binding.root) {

        init {


            binding.item.setOnClickListener {
                var curPos: Int = adapterPosition
                var memoItem: Memo = memoItems.get(curPos)

                var strChoiceItems = arrayOf("삭제하기")
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("삭제하시겠습니까?")
                builder.setItems(strChoiceItems, DialogInterface.OnClickListener { _, pos ->


                    if (pos == 0) {
                        //delete
                        CoroutineScope(Dispatchers.IO).launch {
                            myDao.deleteMemo(memoItem)

                            withContext(Dispatchers.Main) {
                                //delete UI
                                memoItems.removeAt(curPos)
                                notifyItemRemoved(curPos)


                            }



                        }

                        Toast.makeText(context,"삭제완료!",Toast.LENGTH_SHORT).show()


                    }
                })
                builder.show()
            }

        }
    }
    //액티비티에서 호출되는 함수이며 현재 어댑터에 새로운 게시글 아이템을 전달받아 추가하는 목적이다.
    fun addItem(item:Memo){
        memoItems.add(0,item)//0번째에 add
        notifyItemInserted(0)//반영

    }


  //한 화면에 그려지는 아이템 개수만큼 레이아웃 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val dialogbinding=DialogEditBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding,dialogbinding)
    }

    // 생성된 아이템 레이아읏에 값 입력 후 목록에 출력
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvTitle.text= memoItems[position].title
        holder.binding.tvContent.text= memoItems[position].content
        holder.binding.tvDate.text= memoItems[position].writeDate
    }


    override fun getItemCount() = memoItems.size

}