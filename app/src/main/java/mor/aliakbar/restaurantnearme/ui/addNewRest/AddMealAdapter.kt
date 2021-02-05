package mor.aliakbar.restaurantnearme.ui.addNewRest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.ItemAddMealBinding
import mor.aliakbar.restaurantnearme.storage.database.model.Meal
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AddMealAdapter(private var list: ArrayList<Meal>) :
    RecyclerView.Adapter<AddMealAdapter.ProductsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemAddMealBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_add_meal, parent, false)
        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addItem() {
        list.add(Meal(0, "", "", ""))
    }

    fun getAllItem(): java.util.ArrayList<Meal> {
        return list
    }


    class ProductsViewHolder(private var binding: ItemAddMealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: Meal) {
            binding.viewmodel = meal

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                var mHour = binding.clockStartTimeOrder.hour
                val minute = binding.clockStartTimeOrder.minute
                val amPm: String
                if (mHour < 12) {
                    amPm = "AM"
                } else {
                    amPm = "PM"
                    mHour -= 12
                }

                meal.startTime = "$mHour:$minute $amPm"
                meal.endTime = "$mHour:$minute $amPm"
            } else {
                var mHour = binding.clockStartTimeOrder.currentHour
                val minute = binding.clockStartTimeOrder.currentMinute
                val amPm: String
                if (mHour < 12) {
                    amPm = "AM"
                } else {
                    amPm = "PM"
                    mHour -= 12
                }

                meal.startTime = "$mHour:$minute $amPm"
                meal.endTime = "$mHour:$minute $amPm"
            }
            binding.clockStartTimeOrder.setOnTimeChangedListener { _, hour, minute ->
                var mHour = hour
                val amPm: String
                if (hour < 12) {
                    amPm = "AM"
                } else {
                    amPm = "PM"
                    mHour = hour - 12
                }
                meal.startTime = "$mHour:$minute $amPm"
            }
            binding.clockEndTimeOrder.setOnTimeChangedListener { _, hour, minute ->
                var mHour = hour
                val amPm: String
                if (hour < 12) {
                    amPm = "AM"
                } else {
                    amPm = "PM"
                    mHour = hour - 12
                }
                meal.endTime = "$mHour:$minute $amPm"
            }

            binding.executePendingBindings()
        }

    }
}