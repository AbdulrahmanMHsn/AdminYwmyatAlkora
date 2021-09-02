package gizahost.alkora.presentation.social

import amhsn.admin.R
import amhsn.admin.data.model.SocialModel
import amhsn.admin.data.model.UserModel
import amhsn.admin.databinding.LayoutItemSocialBinding
import amhsn.admin.databinding.LayoutItemUserBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView



class UserListAdapter() : RecyclerView.Adapter<UserListAdapter.UserHolder>() {

    var userList: List<UserModel> = ArrayList()
    lateinit var bindingAdapter: LayoutItemUserBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {

        bindingAdapter = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.layout_item_user, parent, false)

        return UserHolder(
            bindingAdapter
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {

        if (userList.isEmpty()) {
            return
        }

        holder.bind(userList[position])

    }


    fun setList(list: List<UserModel>){
        userList = list
        notifyDataSetChanged()
    }

    class UserHolder(val binding:LayoutItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel){
            binding.txtName.text = user.name
            binding.txtEmail.text = user.email
            binding.txtPhone.text = user.phone
            binding.txtComment.text = user.comment
//            binding.itemImgDelete.setOnClickListener{
//                clickListener(social)
//            }
        }
    }


}