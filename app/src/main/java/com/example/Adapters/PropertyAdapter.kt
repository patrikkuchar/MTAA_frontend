package com.example.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.R
import com.example.data.Property

class PropertyAdapter(private val context: Context, private val dataSource: ArrayList<Property>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun dpToPx(dp: Int): Int {
        val density = context.resources
                .displayMetrics
                .density
        return Math.round(dp.toFloat() * density)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.property_homepage, parent, false)

        val priceTextView = rowView.findViewById<TextView>(R.id.price)
        val addressTextView = rowView.findViewById<TextView>(R.id.address)
        val areaTextView = rowView.findViewById<TextView>(R.id.area)
        val roomsTextView = rowView.findViewById<TextView>(R.id.rooms)

   /*     val propertyDiv = rowView.findViewById<LinearLayout>(R.id.property_model)
        val params: ViewGroup.LayoutParams = propertyDiv.layoutParams
        params.height = dpToPx(120)
        propertyDiv.layoutParams = params*/

        val property = getItem(position) as Property

        priceTextView.text = property.price.toString()
        addressTextView.text = property.address
        areaTextView.text = property.area.toString()
        roomsTextView.text = property.rooms.toString()



        return rowView
    }
}