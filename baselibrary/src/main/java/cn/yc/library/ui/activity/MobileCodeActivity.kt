package cn.yc.library.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.yc.library.R
import cn.yc.library.base.BaseActivity
import cn.yc.library.data.mobile.Country
import cn.yc.library.data.mobile.PyEntity
import cn.yc.library.ui.adapter.PyAdapter
import kotlinx.android.synthetic.main.activity_mobilecode.*
import kotlinx.android.synthetic.main.item_country_large_padding.view.*
import kotlinx.android.synthetic.main.item_letter.view.*
import java.util.*
class MobileCodeActivity:BaseActivity(){

    private val selectedCountries= arrayListOf<Country>()
    private val allCountries = ArrayList<Country>()
    private var mAdapter:CAdapter?=null

    override val layoutId: Int
        get() = R.layout.activity_mobilecode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allCountries.clear()
        allCountries.addAll(Country.getAll(this, null))
        selectedCountries.clear()
        selectedCountries.addAll(allCountries)
        rv_pick?.apply {
            layoutManager=LinearLayoutManager(this@MobileCodeActivity)
            mAdapter=CAdapter(selectedCountries)
            adapter=mAdapter
            addItemDecoration(DividerItemDecoration(this@MobileCodeActivity, DividerItemDecoration.VERTICAL))
        }
        et_search.addTextChangedListener(object :TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.isNullOrEmpty()==true || s?.isNullOrBlank()==true){
                    return
                }
                val string = "$s"
                selectedCountries.clear()
                allCountries.forEach {
                    if(it.name.toLowerCase().contains(string.toLowerCase())){
                        selectedCountries.add(it)
                    }
                }
                mAdapter?.update(selectedCountries);
            }
        })

    }

    inner class CAdapter(entities: List<PyEntity>) : PyAdapter<RecyclerView.ViewHolder>(entities) {

        override fun onCreateHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return VH(layoutInflater.inflate(R.layout.item_country_large_padding, parent, false))
        }

        override fun onCreateLetterHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return LetterHolder(layoutInflater.inflate(R.layout.item_letter, parent, false))
        }

        override fun onBindHolder(holder: RecyclerView.ViewHolder, entity: PyEntity, position: Int) {
            val country = entity as Country
            holder.itemView?.apply {
                iv_flag?.setImageResource(country.flag)
                tv_name?.text = country.name
                tv_code?.text = "+" + country.code
                setOnClickListener { v ->
                    val data = Intent()
                    data.putExtra("country", country.toJson())
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }
            }
        }
        override fun onBindLetterHolder(holder: RecyclerView.ViewHolder, entity: LetterEntity, position: Int) {
            holder.itemView?.letter_tv?.text=entity.letter.toUpperCase()
        }

        inner class LetterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        }

        inner class VH(item:View):RecyclerView.ViewHolder(item){
            var tvName: TextView
            var tvCode: TextView
            var ivFlag: ImageView

            init {
                ivFlag = itemView.findViewById(R.id.iv_flag) as ImageView
                tvName = itemView.findViewById(R.id.tv_name) as TextView
                tvCode = itemView.findViewById(R.id.tv_code) as TextView
            }
        }

    }

}