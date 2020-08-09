package com.vietnamese.maskregion.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vietnamese.maskregion.R
import com.vietnamese.maskregion.model.CovidTestingSite

class MapAdapter() : RecyclerView.Adapter<MapAdapter.ViewHolder>() {

    var items = mutableListOf<CovidTestingSite>()
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_daily_mission, viewGroup, false)
        return ViewHolder(v)
    }

    fun insertItem(item: CovidTestingSite) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    fun insertItem(item: CovidTestingSite, position: Int) {
        this.items.add(item)
        notifyItemInserted(position)
    }

    fun insertItems(items: MutableList<CovidTestingSite>) {
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
        fun onItemClickListener(view: View, item: CovidTestingSite)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    fun initRecyclerViews(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = this
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imagePoster: ImageView = view.findViewById(R.id.image_poster)
        private val text_title: TextView = view.findViewById(R.id.text_title)
        fun bind(item: CovidTestingSite) {

//            loadImage(imagePoster, AppConstants.POSTER_PATH + item.posterPath)
            text_title.text = item.title
//            imagePoster.setImageResource(getResource(item.id))
        }

//        fun getResource(id: String): Int {
//            when (id) {
//                "1" -> return R.drawable.category_1
//                "2" -> return R.drawable.category_2
//                "3" -> return R.drawable.category_3
//                "4" -> return R.drawable.category_4
//                else -> return R.drawable.category_5
//            }
//        }
    }


}
