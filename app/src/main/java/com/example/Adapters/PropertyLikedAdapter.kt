package com.example.Adapters

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.Activity.LikedPropertiesActivity
import com.example.Activity.MainSearchActivity
import com.example.R
import com.example.ViewModel.LikedPropertiesActivityViewModel
import com.example.data.Prop


class PropertyLikedAdapter(private val context: Activity, private val dataSource: ArrayList<Prop>) :
    BaseAdapter() {
    lateinit var viewModel: LikedPropertiesActivityViewModel

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

    private inner class ViewHolder {
        internal var main_text: TextView? = null //Display Name
        internal var subtitle: TextView? = null  //Display Description
        internal var can_view_you_online: Button? = null   //Button to set and display status of CanViewYouOnline flag of the class

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val rowView = inflater.inflate(R.layout.property_homepage, parent, false)
        this.notifyDataSetChanged()

        val priceTextView = rowView.findViewById<TextView>(R.id.price)
        val addressTextView = rowView.findViewById<TextView>(R.id.address)
        val areaTextView = rowView.findViewById<TextView>(R.id.area)
        val roomsTextView = rowView.findViewById<TextView>(R.id.rooms)
        val image_property = rowView.findViewById<ImageView>(R.id.prop_image)
        val heart_button = rowView.findViewById<ImageView>(R.id.liked_button)


   /*     val propertyDiv = rowView.findViewById<LinearLayout>(R.id.property_model)
        val params: ViewGroup.LayoutParams = propertyDiv.layoutParams
        params.height = dpToPx(120)
        propertyDiv.layoutParams = params*/

        val property = getItem(position) as Prop

        var liked_button = rowView.findViewById<ImageView>(R.id.liked_button)

        liked_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val propertyaa = getItem(position) as Prop
                println("Clicked")
                println(propertyaa.id)

                propertyaa.liked = false
                val a = context as LikedPropertiesActivity
                val id = propertyaa.id.toInt()
                a.viewModel.unlikedProperties(id,context)
                dataSource.remove(propertyaa)
                //refresh page
                notifyDataSetChanged()


                /*if (property.liked == "true") {
                    liked_button.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    property.liked = "false"
                } else {
                    liked_button.setImageResource(R.drawable.ic_favorite_black_24dp)
                    property.liked = "true"
                }*/
            }
        })



        val imageBytes = Base64.decode(property.image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        image_property.setImageBitmap(image)
        priceTextView.text = modelPrice(property.price.toString())//property.price.toString()
        addressTextView.text = property.address
        areaTextView.text = property.area.toString() + " m2"
        roomsTextView.text = property.rooms.toString() + "-rooms"
        liked_button.setImageResource(R.drawable.fullhearth_icon)


        return rowView
    }

    private fun initViewModel() {

        println("SS")
        var a = MainSearchActivity()
        viewModel = ViewModelProvider(a).get(LikedPropertiesActivityViewModel::class.java)



    }
}