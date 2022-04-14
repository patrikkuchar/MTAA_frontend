package com.example.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.Activity.BookingActivity
import com.example.Activity.MainSearchActivity
import com.example.R
import com.example.ViewModel.BookingActivityViewModel
import com.example.WebRTC.RTCActivity
import com.example.data.BookingSellerData
import com.example.storage.SharedPrefManager
import kotlinx.coroutines.ExperimentalCoroutinesApi

class BookingSellerAdapter(private val context: Activity, private val dataSource: ArrayList<BookingSellerData>) : BaseAdapter() {
    lateinit var viewModel: BookingActivityViewModel
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
        val rowView = inflater.inflate(R.layout.booking_homepage, parent, false)
        this.notifyDataSetChanged()

        val booking_datetime = rowView.findViewById<TextView>(R.id.booking_datetime)
        val prop_image = rowView.findViewById<ImageView>(R.id.prop_image)
        val rooms = rowView.findViewById<TextView>(R.id.rooms)
        val area = rowView.findViewById<TextView>(R.id.area)
        val price = rowView.findViewById<TextView>(R.id.price)
        val address = rowView.findViewById<TextView>(R.id.address)
        val booking_name = rowView.findViewById<TextView>(R.id.booking_name)
        val booking_videocall_button = rowView.findViewById<Button>(R.id.booking_videocall_button)
        val booking_delete_button = rowView.findViewById<Button>(R.id.booking_delete_button)


        val booking = getItem(position) as BookingSellerData

        booking_delete_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val a = context as BookingActivity

                val token = SharedPrefManager.getInstance(context).user.token.toString()
                a.viewModel.delete_booking(token = "Bearer " + token, booking.id)
                Toast.makeText(context, "Delete Button Clicked", Toast.LENGTH_SHORT).show()
                dataSource.remove(booking)
                notifyDataSetChanged()

            }
        })

        booking_videocall_button.setOnClickListener {
            val intent = Intent(context, RTCActivity::class.java)
            intent.putExtra("meeting_id", booking.id)
            intent.putExtra("isJoin", true)
            ContextCompat.startActivity(context, intent, null)
        }

        val imageBytes = Base64.decode(booking.image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        prop_image.setImageBitmap(image)

        //spilt date by "T"
        val booking_datetime_string_split = booking.date.split("T")
        //get first part of date
        val time = booking_datetime_string_split[1].substring(0, 5)
        //increment time by 1 hour
        val incremented = time.substring(0, 2).toInt() + 1
        val time_incremented = incremented.toString() + time.substring(2)


        booking_datetime.text = booking_datetime_string_split[0] + "  " + time + " - " + time_incremented
        rooms.text = booking.rooms.toString() + "-rooms"
        area.text = booking.area.toString() + " m2"
        price.text = modelPrice(booking.price.toString())
        address.text = booking.address
        booking_name.text = booking.buyer




        return rowView
    }

    private fun initViewModel() {
        println("SS")
        var a = BookingActivity()
        viewModel = ViewModelProvider(a).get(BookingActivityViewModel::class.java)
    }
}