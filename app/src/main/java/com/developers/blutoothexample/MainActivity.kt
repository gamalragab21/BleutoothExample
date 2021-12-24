package com.developers.blutoothexample

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.developers.blutoothexample.databinding.ActivityMainBinding
import android.widget.Toast
import android.bluetooth.BluetoothDevice
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val REQUEST_ENABLE_BT = 0
    private val REQUEST_DISCOVER_BT = 1

    var mBlueAdapter: BluetoothAdapter? = null


    private lateinit var binding: ActivityMainBinding

    val myAdapter by lazy { MyDeviceAdapter(this) }


    private val mBroadcastReceiver1: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        checkBluetoothStatus()
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {}
                    BluetoothAdapter.STATE_ON -> {
                        checkBluetoothStatus()
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {

                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter()

        val filter1 = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(mBroadcastReceiver1, filter1)
        checkBluetoothStatus()
        setupRecyclerView()
        //on btn click

//        binding.openBluetooth.setOnClickListener{
//            showToast("Turning On ...")
//
//            if (!mBlueAdapter!!.isEnabled) {
//                showToast("Turning On Bluetooth...")
//                //intent to on bluetooth
//                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                startActivityForResult(intent, REQUEST_ENABLE_BT)
//            } else {
//                showToast("Bluetooth is already on")
//            }
//        }

        //off btn click
        binding.bluetoothStateImg.setOnClickListener {
            if (mBlueAdapter?.isEnabled!!) {
                mBlueAdapter!!.disable()
                showToast("Turning Bluetooth Off")
                binding.bluetoothStateImg.setImageResource(R.drawable.bluetooth_off)
            } else {
                showToast("Turning On Bluetooth...")
                //intent to on bluetooth
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_ENABLE_BT)
            }
            checkBluetoothStatus()
        }


    }

    private fun checkBluetoothStatus() {
        Log.i(TAG, "checkBluetoothStatus:  ")

        //check if bluetooth is available or not
        if (!mBlueAdapter!!.isEnabled) {
            Log.i(TAG, "checkBluetoothStatus: true ")
            binding.constrainNotEnabled.isVisible = true
            binding.bluetoothStateTv.text = "Bluetooth is not available"
            binding.bluetoothStateImg.setImageResource(R.drawable.bluetooth_off)

            myAdapter.devices = listOf()

        } else {

            Log.i(TAG, "checkBluetoothStatus: flase ")

            binding.constrainNotEnabled.isVisible = false
            binding.bluetoothStateTv.text = "Bluetooth is available"
            binding.bluetoothStateImg.setImageResource(R.drawable.bluetooth)
            loadAllDevices()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ENABLE_BT -> if (resultCode == RESULT_OK) {
                //bluetooth is on
                binding.bluetoothStateTv.text = "Bluetooth is on"
                binding.bluetoothStateImg.setImageResource(R.drawable.bluetooth)
                loadAllDevices()
                showToast("Bluetooth is on")
            } else {
                //user denied to turn bluetooth on
                showToast("could't on bluetooth")
                binding.bluetoothStateTv.text = "could't on bluetooth"
                binding.bluetoothStateImg.setImageResource(R.drawable.bluetooth_off)
            }
        }
        checkBluetoothStatus()
        super.onActivityResult(requestCode, resultCode, data)
    }

    //toast message function
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun loadAllDevices() {
        //get paired devices btn click
        //get paired devices btn click
        if (mBlueAdapter!!.isEnabled) {
            val devices = mBlueAdapter!!.bondedDevices

            myAdapter.devices = devices.toList()
//            for (device in devices) {
//
//
//                val myDevice=Device(
//                    device.name,
//                    device.type.toString(),
//                    device.address,
//                )
//
//
//
//            }
        } else {
            //bluetooth is off so can't get paired devices
            showToast("Turn on bluetooth to get paired devices")
        }


    }


    private fun setupRecyclerView() = binding.allDevices.apply {
        itemAnimator = null
        isNestedScrollingEnabled = true
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = myAdapter


    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mBroadcastReceiver1)
    }


}