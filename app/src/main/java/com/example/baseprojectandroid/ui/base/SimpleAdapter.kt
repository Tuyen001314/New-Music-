package com.example.baseprojectandroid.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.baseprojectandroid.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

abstract class SimpleAdapter<T, VH : SimpleAdapter.ViewHolder, VB : ViewDataBinding>(protected val context: Context) :
    RecyclerView.Adapter<VH>(), Filterable, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val inflater = LayoutInflater.from(context)

    private var weakRecyclerView: WeakReference<RecyclerView>? = null

    var originData = ArrayList<T>()
        private set
    var data = ArrayList<T>()
        private set

    lateinit var dataBinding: VB

    private lateinit var onItemClicked: (position: Int) -> Unit
    private var onItemClick: (item: T) -> Unit = {}
    private lateinit var onDataFiltered: () -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        //val itemView = parent.inflate(inflater, getItemViewId(viewType))
        dataBinding = createViewBinding(parent)
        return getViewHolder(viewType, dataBinding)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        weakRecyclerView = WeakReference(recyclerView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (getRegisterOnItemClickListener()) {
            registerOnItemClickListener(holder)
        }
        holder.bindViews(position)
    }

    override fun getFilter(): Filter? = null

    fun getItem(position: Int) = data[position]

    fun submitList(list: List<T>, updateOriginData: Boolean) {
        if (updateOriginData) {
            originData.clear()
            originData.addAll(list)
        }
        data.clear()
        data.addAll(list)

        runLayoutAnimation(recyclerView)
        notifyDataSetChanged()
    }

    fun submitListWithoutAnim(list: List<T>, updateOriginData: Boolean) {
        if (updateOriginData) {
            originData.clear()
            originData.addAll(list)
        }
        data.clear()
        data.addAll(list)

        notifyDataSetChanged()
    }

    fun submitListItem(list: List<T>, updateOriginData: Boolean, position: Int) {
        if (updateOriginData) {
            originData.clear()
            originData.addAll(list)
        }
        data.clear()
        data.addAll(list)

        notifyItemChanged(position)
    }

    fun setList(list: List<T>) {
        originData.clear()
        originData.addAll(list)
        data.clear()
        data.addAll(list)
    }

    val recyclerView get() = weakRecyclerView?.get()

    fun setOnItemClickListener(onItemClicked: (position: Int) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    fun setOnIemClickListener(onItemClicked: (item: T) -> Unit) {
        this.onItemClick = onItemClicked
    }

    fun setOnDataFilterListener(onDataFiltered: () -> Unit) {
        this.onDataFiltered = onDataFiltered
    }

    fun invokeOnItemClicked(position: Int) {
        if (::onItemClicked.isInitialized) {
            onItemClicked.invoke(position)
        } else {
            onItemClick.invoke(getItem(position))
        }
    }

    fun invokeOnDataFiltered() {
        if (::onDataFiltered.isInitialized) {
            onDataFiltered.invoke()
        }
    }

    open fun getRegisterOnItemClickListener() = true

    abstract fun getItemViewId(viewType: Int): Int

    abstract fun getViewHolder(viewType: Int, dataBinding: VB): VH

    abstract fun createViewBinding(parent: ViewGroup): VB

    // ---------------------------------------------------------------------------------------------

    private fun runLayoutAnimation(recyclerView: RecyclerView?) {
        if (recyclerView != null) {
            val controller = AnimationUtils.loadLayoutAnimation(
                recyclerView.context,
                R.anim.layout_animation_fall_down
            )
            recyclerView.layoutAnimation = controller
            recyclerView.scheduleLayoutAnimation()
        }
    }

    fun registerOnItemClickListener(holder: ViewHolder) {
        holder.itemView.setOnClickListener {
            if (::onItemClicked.isInitialized) {
                onItemClicked.invoke(holder.adapterPosition)
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    abstract class ViewHolder(itemView: ViewDataBinding) : RecyclerView.ViewHolder(itemView.root) {
        abstract fun bindViews(position: Int)
    }

}