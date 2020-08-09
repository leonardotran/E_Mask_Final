package com.vietnamese.maskregion.ui.survey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vietnamese.maskregion.R
import com.vietnamese.maskregion.ui.home.DeviceAdapter

class SurveyAdapter() : RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

    var items = mutableListOf<String>()
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_survey, viewGroup, false)
        return ViewHolder(v)
    }

    fun clear() {
        this.items.clear()
    }

    fun insertItem(item: String) {
            this.items.add(item)
            notifyDataSetChanged()
    }


    fun insertItem(item: String, position: Int) {
        this.items.add(item)
        notifyItemInserted(position)
    }

    fun insertItems(items: MutableList<String>) {
        this.items = items
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { v ->
            listener?.onItemClickListener(v, items.get(holder.layoutPosition))
        }
    }

    /**
     * The interface that receives onItemClick messages.
     */
    interface OnItemClickListener {
        fun onItemClickListener(view: View, item: String)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun initRecyclerViews(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, RecyclerView.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = this
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvItem: TextView = view.findViewById(R.id.tvItem)
        fun bind(item: String) {
            tvItem.text = item
        }
    }


}
