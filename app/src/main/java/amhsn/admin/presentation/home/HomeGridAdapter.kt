package amhsn.admin.presentation.home

import amhsn.admin.R
import amhsn.admin.data.model.HomeModel
import amhsn.admin.databinding.LayoutItemHomeBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView


class HomeGridAdapter(private val clickListener:(HomeModel)->Unit) : RecyclerView.Adapter<HomeGridAdapter.HomeHolder>() {

    var homeLists: List<HomeModel> = ArrayList()
    lateinit var bindingAdapter: LayoutItemHomeBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {

        bindingAdapter = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.layout_item_home, parent, false)

        return HomeHolder(
            bindingAdapter
        )
    }

    override fun getItemCount(): Int {
        return homeLists.size
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {

        if (homeLists.isEmpty()) {
            return
        }

        holder.bind(homeLists[position],clickListener)

    }


    fun setList(homeList: List<HomeModel>){
        homeLists = homeList
        notifyDataSetChanged()
    }

    class HomeHolder(val binding:LayoutItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(home: HomeModel,clickListener:(HomeModel)->Unit){
            binding.itemTxtHome.text = home.body
            binding.itemImgHome.setImageResource(home.resId)
            binding.layoutListHome.setOnClickListener{
                clickListener(home)
            }
        }
    }


}