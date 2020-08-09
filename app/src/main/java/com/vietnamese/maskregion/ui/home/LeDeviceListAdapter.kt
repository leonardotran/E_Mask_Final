package com.vietnamese.maskregion.ui.home

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.vietnamese.maskregion.R
import kotlin.coroutines.coroutineContext

// Adapter for holding devices found through scanning.
class LeDeviceListAdapter : BaseAdapter() {
    private val mLeDevices: ArrayList<BluetoothDevice>
    init {
        mLeDevices = ArrayList()
    }

    fun addDevice(device: BluetoothDevice) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device)
        }
    }

    fun getDevice(position: Int): BluetoothDevice {
        return mLeDevices[position]
    }

    fun clear() {
        mLeDevices.clear()
    }

    override fun getCount(): Int {
        return mLeDevices.size
    }

    override fun getItem(i: Int): Any {
        return mLeDevices[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        val viewHolder: ViewHolder
        // General ListView optimization code.
        viewHolder = view.tag as ViewHolder
        val device = mLeDevices[i]
        val deviceName = device.name
        if (deviceName != null && deviceName.isNotEmpty()) viewHolder.deviceName!!.text =
            deviceName else viewHolder.deviceName?.text = view.context.getString(R.string.unknown_device)
        viewHolder.deviceAddress!!.text = device.address
        return view
    }
}


internal class ViewHolder {
    var deviceName: TextView? = null
    var deviceAddress: TextView? = null
}