package com.kotlinstudy.consumer_kotlin_app

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class dbDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_db_data_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var dbDataAll: ArrayList<dbListData> = requireActivity().intent!!.extras!!.get("DB_Data_all") as ArrayList<dbListData>
        Log.e("dbDataAll", "DB_Data_List: ${dbDataAll}")
    }

}
