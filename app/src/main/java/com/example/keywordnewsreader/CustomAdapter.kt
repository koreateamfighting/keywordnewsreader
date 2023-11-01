package com.example.keywordnewsreader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso;


class Data(val photo : String, val title : String)

class CustomAdapter(val context: Context, val DataList: ArrayList<Data>) : BaseAdapter()
{

    override fun getCount() = DataList.size

    // any
    override fun getItem(position: Int) = DataList[position]

    // long
    override fun getItemId(position: Int) = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_list, null)
        val photo = view.findViewById<ImageView>(R.id.iv_custom)
        val title = view.findViewById<TextView>(R.id.tv_custom)
        val data = DataList[position]

        Picasso.get().load(data.photo).into(photo)
        title.text = data.title


        return view
    }

}