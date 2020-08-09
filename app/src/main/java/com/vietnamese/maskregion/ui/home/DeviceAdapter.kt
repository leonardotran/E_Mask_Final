package com.vietnamese.maskregion.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vietnamese.maskregion.R
import com.vietnamese.maskregion.model.Device

class DeviceAdapter() : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    var items = mutableListOf<Device>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_bluetooth_device, viewGroup, false)
        return ViewHolder(v)
    }

    fun clear() {
        this.items.clear()
    }

    fun insertItem(item: Device) {
            this.items.add(item)
            notifyDataSetChanged()
    }

    fun exists(item: Device, items: MutableList<Device>):Boolean {
        items.any { i ->
            i.userId.equals(item.userId)
            return true
        }
        return false
    }

    fun insertItem(item: Device, position: Int) {
        this.items.add(item)
        notifyItemInserted(position)
    }

    fun insertItems(items: MutableList<Device>) {
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
        fun onItemClickListener(view: View, item: Device)
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
        private val userId: TextView = view.findViewById(R.id.userId)
        private val macAddress: TextView = view.findViewById(R.id.macAddress)

        fun bind(item: Device) {
            userId.text = item.userId
            macAddress.text = item.deviceMacAddress
        }
    }


}
