package com.vietnamese.maskregion.ui.home

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vietnamese.maskregion.R
import com.vietnamese.maskregion.databinding.FragmentHomeBinding
import com.vietnamese.maskregion.model.Device
import com.vietnamese.maskregion.model.Profile


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private val TAG = "Permission"
    private val REQUEST_ENABLE_BT = 101

    lateinit var bluetoothAdapter: BluetoothAdapter
    val bluetoothTag = "EMask20"
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    lateinit var userRef: DocumentReference
    var contacted_ids: MutableList<String> = mutableListOf()
    private lateinit var deviceAdapter: DeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val homeViewModel: HomeViewModel by viewModels()
        viewModel = homeViewModel

        deviceAdapter = DeviceAdapter()
        deviceAdapter.initRecyclerViews(binding.recyclerView)


        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textStatus.text = it
        })


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        homeViewModel.setBluetoothName(bluetoothAdapter.name)


        user?.uid?.let {
            userRef = user.uid.let { db.collection("profile").document(it) }
            userRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val profile = document.toObject(Profile::class.java)
                    profile?.let {
                        contacted_ids = profile.contacted_ids
                    }
                } else {
                    Log.d(TAG, "Not exists")
                }
            }.addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        }
        binding.scan.setOnClickListener {
            scanDevices()
        }
        binding.recyclerView
        return binding.root
    }

    private fun scanDevices() {
        deviceAdapter.clear()
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(ACTION_DISCOVERY_FINISHED)
        activity?.registerReceiver(receiver, filter)
        bluetoothAdapter.startDiscovery()
        bluetoothAdapter.name = bluetoothTag + "_" + user?.uid
        binding.determinateBar.visibility = View.VISIBLE
        viewModel.setStatus("Scanning")
        binding.image.visibility = View.GONE
        binding.scan.setText("Stop")
        binding.scan.setBackgroundColor(Color.RED)
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    Log.d("ACTION_FOUND", "---------------------")
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    var id: String? = getIdFromName(deviceName)
                    val rssi: Int =
                        intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE).toInt()
                    Log.d("RSSI", "" + rssi)
                    id?.let {
                        deviceAdapter.insertItem(Device(id, device?.address))
                        if (!contacted_ids.contains(id)) {
                            Log.d("ID", "" + id)
                            contacted_ids.add(it)
                            userRef.update(
                                "contacted_ids",
                                contacted_ids,
                                "contacted",
                                contacted_ids.size
                            )

                        } else {
                            Log.d("NOT ID", "" + id)
                        }
                    }
                }
                ACTION_DISCOVERY_FINISHED -> {
                    deviceAdapter.clear()
                    binding.scan.setText("Start")
                    binding.determinateBar.visibility = View.GONE
                    binding.scan.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                        )
                    )
                    binding.image.visibility = View.VISIBLE
                    if (!binding.autoStop.isChecked) {
                        scanDevices()
                    }

                    Log.d("DISCOVERY_FINISHED", "---------------------")
                    viewModel.setStatus("Stopped")
                }
            }
        }
    }
//
//    fun getFakeIds(count: Int): String {
//        when (count) {
//            1 -> return "83D0mL4QTENG4Eba4YMtp2QBOtD2"
//            2 -> return "TwcX9yusb4M2yktwDPiXhIxUD2O2"
//            3 -> return "21GxTFhvPhe3dWMAAZDulCUPgTw2"
//        }
//        return ""
//    }

    fun <E> List<E>.randomOrNull(): E? = if (size > 0) random() else null

    private fun getIdFromName(deviceName: String?): String? {
        try {
            val separated: List<String>? = deviceName?.split("_")
            if (separated?.get(0).equals(bluetoothTag)) {
                return separated?.get(1)
            }
            return null
        } catch (ex: Exception) {
            return null
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("Home", "onStart!")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Home", "onStop!")
    }

}