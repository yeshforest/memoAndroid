package com.example.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

    inner class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {

        init {


            binding.item.setOnClickListener {
                var curPos: Int = adapterPosition
                var memoItem: Memo = memoItems.get(curPos)

                var strChoiceItems = arrayOf("수정하기", "삭제하기")
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("원하는 작업을 선택해주세요")
                builder.setItems(strChoiceItems, DialogInterface.OnClickListener { dialogInterface, pos ->


                    if (pos == 0) {
                        // 수정하기

                        //팝업창 띄우기
                        val dialog = Dialog(context,android.R.style.Theme_Material_Light_Dialog)
                        dialog.setContentView(R.layout.dialog_edit)

                        dialog.setCancelable(false)

                        val et_title: EditText =dialog.findViewById(R.id.et_title)
                        val et_content: EditText =dialog.findViewById(R.id.et_content)
                        val btn_save: Button =dialog.findViewById(R.id.btn_save)
                        et_title.setText(memoItem.title)
                        et_content.setText(memoItem.content)
                        btn_save.setOnClickListener{

                            //Update table
                            val title:String=et_title.text.toString()
                            val content:String=et_content.text.toString()
                            val currentTime:String=
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
                            val beforeTime=memoItem.writeDate
                            memoItem.title=title
                            memoItem.content=content
                            memoItem.writeDate=currentTime
                            memoItem.beforeTime=beforeTime

                            CoroutineScope(Dispatchers.IO).launch {

                                myDao.updateMemo(memoItem)
                                withContext(Dispatchers.Main) {
                                    Log.d("memoitem", memoItem.content)
                                    notifyItemChanged(curPos, memoItem)
                                }
                            }

                            dialog.dismiss()
                         //   dialogParentView.removeView(dialogbinding.root)
                            Toast.makeText(context,"수정 완료!",Toast.LENGTH_SHORT).show()
                        }
                        dialog.show()
                    } else if (pos == 1) {
                        //delete
                        CoroutineScope(Dispatchers.IO).launch {
                            myDao.deleteMemo(memoItem)

                            withContext(Dispatchers.Main) {
                                //delete UI
                               memoItems.removeAt(curPos)
                                // customAdapter.notifyItemChanged(curPos)
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
        return ViewHolder(binding)
    }

    // 생성된 아이템 레이아읏에 값 입력 후 목록에 출력
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvTitle.text= memoItems[position].title
        holder.binding.tvContent.text= memoItems[position].content
        holder.binding.tvDate.text= memoItems[position].writeDate
    }


    //override fun getItemCount() = memoItems.size
    override fun getItemCount(): Int {//삭제한 뒤엔 size가 안뜬다.
        Log.d("size?????",memoItems.size.toString())
        return memoItems.size

    }
}