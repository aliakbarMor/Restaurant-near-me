package mor.aliakbar.restaurantnearme.ui.addNewRest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.ItemAddFoodBinding
import mor.aliakbar.restaurantnearme.storage.database.model.Food

class AddFoodAdapter(private var list: ArrayList<Food>) :
    RecyclerView.Adapter<AddFoodAdapter.ProductsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemAddFoodBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_add_food, parent, false)
        return ProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addItem() {
        list.add(Food(0, ""))
    }

    fun getAllItem(): ArrayList<Food> {
        return list
    }


    class ProductsViewHolder(private var binding: ItemAddFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food) {
            binding.viewmodel = food
            binding.executePendingBindings()
        }

    }
}