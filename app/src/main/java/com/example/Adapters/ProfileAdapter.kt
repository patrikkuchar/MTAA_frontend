package com.example.Adapters

import android.app.Activity
import android.content.Intent
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.example.Activity.MainSearchActivity
import com.example.Activity.ProfileActivity
import com.example.Activity.SellPropertyActivity
import com.example.R
import com.example.ViewModel.ProfileActivityViewModel
import com.example.WebRTC.RTCActivity
import com.example.data.BookingSellerData
import com.example.data.UserProperty
import com.example.storage.SharedPrefManager
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ProfileAdapter(private val context: Activity, private val dataSource: ArrayList<UserProperty>) : BaseAdapter() {

    lateinit var viewModel: ProfileActivityViewModel
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.profile_homepage, parent, false)
        this.notifyDataSetChanged()


        val prop_image = rowView.findViewById<ImageView>(R.id.profile_image_view)
        val rooms = rowView.findViewById<TextView>(R.id.profile_rooms_text)
        val area = rowView.findViewById<TextView>(R.id.profile_area_text)
        val price = rowView.findViewById<TextView>(R.id.profile_price_text)
        val address = rowView.findViewById<TextView>(R.id.profile_address_text)
        val EditButton = rowView.findViewById<Button>(R.id.editPropertyButton)
        val DeleteButton = rowView.findViewById<Button>(R.id.deletePropertyButton)
        val create_at = rowView.findViewById<TextView>(R.id.profile_create_at_text)


        val property = getItem(position) as UserProperty

        DeleteButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val a = context as ProfileActivity
                val token = SharedPrefManager.getInstance(context).user.token.toString()
                a.viewModel.delete_property(token = "Bearer " + token, property.id)
                Toast.makeText(context, "Delete Button Clicked", Toast.LENGTH_SHORT).show()
                dataSource.remove(property)
                notifyDataSetChanged()

            }
        })

        EditButton.setOnClickListener {
            val intent = Intent(context, SellPropertyActivity::class.java)
            intent.putExtra("editProperty", 1)
            intent.putExtra("propertyId", property.id)
            startActivity(context,intent,null)

        }

        val imageBytes = Base64.decode(property.image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        prop_image.setImageBitmap(image)

        //spilt date by "T"
        val booking_datetime_string_split = property.last_updated.split("T")
        //get first part of date
        val time = booking_datetime_string_split[1].substring(0, 5)
        //increment time by 1 hour
        val incremented = time.substring(0, 2).toInt() + 1
        val time_incremented = incremented.toString() + time.substring(2)


        create_at.text = property.last_updated.substring(0, 10) + " " + time_incremented
        rooms.text = property.rooms.toString() + "-rooms"
        area.text = property.area.toString() + " m2"
        price.text = modelPrice(property.price.toString())
        address.text = property.address

        return rowView
    }

    private fun initViewModel() {
        println("SS")
        var a = ProfileActivity()
        viewModel = ViewModelProvider(a).get(ProfileActivityViewModel::class.java)
    }
}