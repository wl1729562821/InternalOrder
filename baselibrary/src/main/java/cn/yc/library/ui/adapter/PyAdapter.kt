package cn.yc.library.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import cn.yc.library.data.mobile.PyEntity
import java.util.*

/**
 * Created by android on 3/15/2018.
 */

open abstract class PyAdapter<VH : RecyclerView.ViewHolder>(entities: List<PyEntity>?) : RecyclerView.Adapter<VH>(), View.OnClickListener {
    private val holders = WeakHashMap<View, VH>()
    val entityList = ArrayList<PyEntity>()
    val letterSet = HashSet<LetterEntity>()
    private var listener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(entity: PyEntity, position: Int) {

        }
    }

    init {
        if (entities == null) throw NullPointerException("entities == null!")
        update(entities)
    }

    fun update(entities: List<PyEntity>?) {
        if (entities == null) throw NullPointerException("entities == null!")
        entityList.clear()
        entityList.addAll(entities)
        letterSet.clear()
        for (entity in entities) {
            val pinyin = entity.pinyin
            if (!TextUtils.isEmpty(pinyin)) {
                var letter = pinyin[0]
                if (!isLetter(letter))
                    letter = 35.toChar()
                letterSet.add(LetterEntity(letter + ""))
            }
        }
        entityList.addAll(letterSet)
        entityList.sortWith(Comparator { o1, o2 ->
            val pinyin = o1.pinyin.toLowerCase()
            val anotherPinyin = o2.pinyin.toLowerCase()
            val letter = pinyin[0]
            val otherLetter = anotherPinyin[0]
            if (isLetter(letter) && isLetter(otherLetter))
                pinyin.compareTo(anotherPinyin)
            else if (isLetter(letter) && !isLetter(otherLetter)) {
                -1
            } else if (!isLetter(letter) && isLetter(otherLetter)) {
                1
            } else {
                if (letter.toInt() == 35 && o1 is LetterEntity)
                    -1
                else if (otherLetter.toInt() == 35 && o2 is LetterEntity)
                    1
                else
                    pinyin.compareTo(anotherPinyin)
            }
        })
        notifyDataSetChanged()
    }

    private fun isLetter(letter: Char): Boolean {
        return 'a' <= letter && 'z' >= letter || 'A' <= letter && 'Z' >= letter
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val entity = entityList[position]
        holders[holder.itemView] = holder
        holder.itemView.setOnClickListener(this)
        if (entity is LetterEntity) {
            onBindLetterHolder(holder, entity, position)
        } else {
            onBindHolder(holder, entity, position)
        }
    }

    fun getEntityPosition(entity: PyEntity): Int {
        return entityList.indexOf(entity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return if (viewType == TYPE_LETTER)
            onCreateLetterHolder(parent, viewType)
        else
            onCreateHolder(parent, viewType)
    }

    abstract fun onCreateLetterHolder(parent: ViewGroup, viewType: Int): VH
    abstract fun onCreateHolder(parent: ViewGroup, viewType: Int): VH

    fun getLetterPosition(letter: String): Int {
        val entity = LetterEntity(letter)
        return entityList.indexOf(entity)
    }

    override fun getItemViewType(position: Int): Int {
        val entity = entityList[position]
        return if (entity is LetterEntity) TYPE_LETTER else getViewType(entity, position)
    }

    fun getViewType(entity: PyEntity, position: Int): Int {
        return TYPE_OTHER
    }

    override fun getItemCount(): Int {
        return entityList.size
    }

    open fun onBindLetterHolder(holder: VH, entity: LetterEntity, position: Int) {

    }

    open fun onBindHolder(holder: VH, entity: PyEntity, position: Int) {

    }

    fun isLetter(position: Int): Boolean {
        return if (position < 0 || position >= entityList.size)
            false
        else
            entityList[position] is LetterEntity
    }

    interface OnItemClickListener {
        fun onItemClick(entity: PyEntity, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onClick(v: View) {
        val holder = holders[v]
        if (holder == null) {
            Log.e(TAG, "Holder onClick event, but why holder == null?")
            return
        }
        val position = holder.adapterPosition
        val pyEntity = entityList[position]
        listener.onItemClick(pyEntity, position)
    }

    class LetterEntity(val letter: String) : PyEntity {

        override fun getPinyin(): String {
            return letter.toLowerCase()
        }

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false

            val that = o as LetterEntity?

            return letter.toLowerCase() == that!!.letter.toLowerCase()
        }

        override fun hashCode(): Int {
            return letter.toLowerCase().hashCode()
        }
    }

    companion object {

        private val TAG = "PyAdapter"
        val TYPE_LETTER = 0
        val TYPE_OTHER = 1
    }
}
