package mor.aliakbar.restaurantnearme.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.ItemRestaurantBinding
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant

class RestaurantsAdapter(
    private var list: ArrayList<Restaurant>,
    private var itemViewModel: RestaurantItemViewModel,
    private var restListener: RestListener
) :
    RecyclerView.Adapter<RestaurantsAdapter.ProductsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemRestaurantBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_restaurant, parent, false)
        return ProductsViewHolder(binding, itemViewModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val restaurant = list[position]
        holder.bind(restaurant)
        holder.itemView.setOnClickListener {
            restListener.onRestaurantClicked(restaurant)
        }
        holder.itemView.setOnLongClickListener {
            restListener.onRestaurantLongClicked(restaurant)
            return@setOnLongClickListener true
        }
    }

    fun updateData(newList: List<Restaurant>) {
        list.clear()
        list.addAll(newList)
    }

    class ProductsViewHolder(
        private var binding: ItemRestaurantBinding,
        var itemViewModel: RestaurantItemViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            itemViewModel.init(restaurant)
            binding.viewmodel = itemViewModel
            binding.executePendingBindings()
        }

    }
}