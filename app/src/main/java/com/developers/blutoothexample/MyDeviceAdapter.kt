package com.developers.blutoothexample

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.developers.blutoothexample.databinding.ItemDeviceBinding

class MyDeviceAdapter(
    private val context: Context,
) : RecyclerView.Adapter<MyDeviceAdapter.ViewHolder>() {




    //
    var devices: List<BluetoothDevice>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    private val diffCallback = object : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        //
        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem.address == newItem.address
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    inner class ViewHolder(val itemBinding:  ItemDeviceBinding) : RecyclerView.ViewHolder(itemBinding.root) {


        @RequiresApi(Build.VERSION_CODES.R)
        fun bindData(item: BluetoothDevice) {
            itemBinding.deviceAddress.text=item.address
            itemBinding.deviceName.text=item.name
            itemBinding.deviceStrength.text=item.type.toString()

            setupActions(item)
        }

        private fun setupActions(item: BluetoothDevice){
            itemBinding.root.setOnClickListener {
//                onItemClickListener?.let { click->
//                    click(item)
//                }
            }


        }



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val device = devices[position]


        holder.apply {
            bindData(device)
        }


    }


    override fun getItemCount(): Int = devices.size

    private var onItemClickListener: ((Device) -> Unit)? = null

    fun setOnItemClickListener(listener: (Device) -> Unit) {
        onItemClickListener = listener
    }

    private var onSavedClickListener: ((Device, ImageView, Int) -> Unit)? = null

    fun setOnSavedClickListener(listener: (Device, ImageView, Int) -> Unit) {
        onSavedClickListener = listener
    }


}