package com.example.smartalarm.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.smartalarm.data.Alarm

// todo 1 = buat kelas diffutil

// class alarmDiffUtil berfungsi utk membandingkan list lama dan list yang baru
// jika ada perbedaan dalam data maka kita ambil data dari list yg baru
class AlarmDiffUtil(private val oldList: List<Alarm>, private val newList: List<Alarm>) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    // untuk bandingin posisi list
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    // utk bandingkan konten list yang lama dan yg baru
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.id == newData.id
                && oldData.date == newData.date
                && oldData.time == newData.time
                && oldData.message == newData.message

    }
}