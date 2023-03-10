package com.example.todolist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.DialogEditBinding
import com.example.todolist.databinding.ItemListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(val mI:ArrayList<Memo>,val c:Context ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>()
{
    val myDao:MemoDao
    var memoItems= ArrayList<Memo>()
    var context: Context = c
    private lateinit var resultLauncher:ActivityResultLauncher<Intent>
    init{
        myDao=MyDatabase.getDatabase(context).getMemoDao()
        memoItems.addAll(mI)

    }
    inner class ViewHolder(val binding: ItemListBinding, val dialogbinding: DialogEditBinding) : RecyclerView.ViewHolder(binding.root) {


        private fun setResult(memoItem:Memo, curPos:Int){
            val activity=context as MainActivity
            resultLauncher=activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result->
                if(result.resultCode== Activity.RESULT_OK){
                    val isDelete=result.data?.getStringExtra("isDelete")?:false
                    if(isDelete==true){
                        CoroutineScope(Dispatchers.IO).launch {
                            myDao.deleteMemo(memoItem)

                            withContext(Dispatchers.Main) {
                                //delete UI
                                memoItems.removeAt(curPos)
                                notifyItemRemoved(curPos)


                            }



                        }

                        Toast.makeText(context,"????????????!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        init {


            binding.item.setOnClickListener {
                //item??? ???????????? ????????????.
                //?????? ??? ?????? memoItem??? ?????????,??????

                var curPos: Int = adapterPosition
                var memoItem: Memo = memoItems.get(curPos)


                var intent=Intent(context,DetailedMemo::class.java)
                intent.putExtra("memoItem",memoItem)
                context.startActivity(intent)



            }

        }
    }


    //?????????????????? ???????????? ???????????? ?????? ???????????? ????????? ????????? ???????????? ???????????? ???????????? ????????????.
    fun addItem(item:Memo){
        memoItems.add(0,item)//0????????? add
        notifyItemInserted(0)//??????

    }


  //??? ????????? ???????????? ????????? ???????????? ???????????? ??????
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val dialogbinding=DialogEditBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding,dialogbinding)
    }

    // ????????? ????????? ??????????????? ??? ?????? ??? ????????? ??????
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvTitle.text= memoItems[position].title
        holder.binding.tvContent.text= memoItems[position].content
        holder.binding.tvDate.text= memoItems[position].writeDate
    }


    override fun getItemCount() = memoItems.size


}