package com.example.baseprojectandroid.ui.component.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseprojectandroid.R
import kotlinx.android.synthetic.main.type_two.view.*
import kotlinx.android.synthetic.main.typw_one.view.*

class AdapterYourLibrary(
    private val typeOneList: MutableList<String>,
    private val typeTwoList: MutableList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        return when (p1) {
            ViewType.TYPE_ONE.type -> {
                val view = inflater.inflate(R.layout.typw_one, p0, false)
                TypeOneViewHodel(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.type_two, p0, false)
                TypeTwoViewHodel(view)
            }
        }

    }

    override fun getItemCount(): Int {
        return typeOneList.count() + typeTwoList.count()
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        p0.apply {
            when (p0) {
                is TypeOneViewHodel -> p0.bind(typeOneList[p1])
                is TypeTwoViewHodel -> p0.bind(typeTwoList[p1 - typeOneList.count()])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            in 0 until typeOneList.count() -> ViewType.TYPE_ONE.type
            else -> ViewType.TYPE_TWO.type
        }
    }

    inner class TypeOneViewHodel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            with(itemView) {
                tvContact.text = item
            }
        }

    }

    inner class TypeTwoViewHodel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            with(itemView) {
                tvAccount.text = item

            }
        }

    }

}

enum class ViewType(val type: Int) {
    TYPE_ONE(0),
    TYPE_TWO(1)
}