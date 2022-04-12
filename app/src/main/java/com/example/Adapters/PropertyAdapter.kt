package com.example.Adapters

import android.R.attr.data
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.R
import com.example.data.Prop


class PropertyAdapter(private val context: Activity, private val dataSource: ArrayList<Prop>) :
    BaseAdapter() {

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

    fun clearData() {
        // clear the data
        this.dataSource.clear()
    }

    private fun modelPrice(price: String) : String {
        var modeledPrice = ""

        var count = 0
        for (i in price.length - 1 downTo 0) {
            modeledPrice += price[i].toString()
            if (count % 3 == 2) {
                modeledPrice += " "
            }
            count++
        }
        return modeledPrice.reversed()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.property_homepage, parent, false)
        this.notifyDataSetChanged()

        val priceTextView = rowView.findViewById<TextView>(R.id.price)
        val addressTextView = rowView.findViewById<TextView>(R.id.address)
        val areaTextView = rowView.findViewById<TextView>(R.id.area)
        val roomsTextView = rowView.findViewById<TextView>(R.id.rooms)
        val image_property = rowView.findViewById<ImageView>(R.id.prop_image)


   /*     val propertyDiv = rowView.findViewById<LinearLayout>(R.id.property_model)
        val params: ViewGroup.LayoutParams = propertyDiv.layoutParams
        params.height = dpToPx(120)
        propertyDiv.layoutParams = params*/

        val property = getItem(position) as Prop


        val imageBytes = Base64.decode(property.image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        image_property.setImageBitmap(image)
        priceTextView.text = modelPrice(property.price.toString())//property.price.toString()
        addressTextView.text = property.address
        areaTextView.text = property.area.toString() + " m2"
        roomsTextView.text = property.rooms.toString() + "-rooms"



        return rowView
    }
}