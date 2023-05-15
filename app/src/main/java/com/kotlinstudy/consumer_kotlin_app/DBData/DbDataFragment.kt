package com.kotlinstudy.consumer_kotlin_app.DBData

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinstudy.consumer_kotlin_app.R


class dbDataFragment : Fragment() {

    // RecyclerView.adapter에 지정할 Adapter
    private lateinit var dbDataAdapter: DBdataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_db_data_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireActivity().intent?.extras
        Log.e("bundle", bundle.toString())
        if (bundle != null && bundle.containsKey("DB_Data_all")) {
            val dbDataAll = bundle.getSerializable("DB_Data_all") as ArrayList<dbListData>
            Log.e("dbDataAll", "DB_Data_List: $dbDataAll")
            dbDataAdapter = DBdataAdapter(dbDataAll)
            val rv_dbdata = view.findViewById<RecyclerView>(R.id.rv_dbdata)
            rv_dbdata.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            rv_dbdata.adapter = dbDataAdapter
        }
    }

}
