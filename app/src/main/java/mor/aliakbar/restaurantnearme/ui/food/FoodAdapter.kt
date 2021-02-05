package mor.aliakbar.restaurantnearme.ui.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.ItemFoodBinding
import mor.aliakbar.restaurantnearme.storage.database.model.Food

class FoodAdapter(var list: ArrayList<Food>, var itemViewModel: FoodItemViewModel) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemFoodBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_food, parent, false)
        return FoodViewHolder(binding, itemViewModel)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = list[position]
        holder.bind(food)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class FoodViewHolder(
        private var binding: ItemFoodBinding,
        var itemViewModel: FoodItemViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            itemViewModel.init(food)
            binding.viewmodel = itemViewModel
            binding.executePendingBindings()
        }
    }
}
