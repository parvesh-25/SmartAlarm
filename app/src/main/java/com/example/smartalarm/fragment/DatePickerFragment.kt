package com.example.smartalarm.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(),DatePickerDialog.OnDateSetListener {

    private var dialogListener: DateDialogListener? = null

    // untuk inisialisasi dialog listener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogListener = context as DateDialogListener
    }

    // untuk memerikasa apakah kosong sebelum memunculkan dialognya
    override fun onDetach() {
        super.onDetach()
        if (dialogListener !=null) dialogListener = null
    }

    // untuk memunculkan kotak dialognya
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(activity as Context, this, year,month, dayOfMonth)
    }

    // untuk menampung informasi date yang sudah kita pilih
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dialogListener?.onDialogDateset(tag, year, month, dayOfMonth)
        Log.i(tag,"onDateSet: $year $month $dayOfMonth")
    }

    // buat dipanggil di activity supaya dapat nilai inputan yang dipilih
    interface DateDialogListener {
        fun onDialogDateset(tag:String?, year:Int, month: Int, dayOfMonth: Int)
    }
}