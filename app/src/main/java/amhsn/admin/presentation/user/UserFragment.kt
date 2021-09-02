package amhsn.admin.presentation.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import amhsn.admin.R
import amhsn.admin.data.model.UserModel
import amhsn.admin.databinding.FragmentUserBinding
import amhsn.admin.presentation.util.NetworkConnection
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import gizahost.alkora.presentation.social.UserListAdapter


class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: UserListAdapter
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private var list = mutableListOf<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("User")

        initRecyclerView()


        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner, Observer {
            if (it) {
                getdata()
            } else {
                getdata()
                val snack =
                    Snackbar.make(requireView(), "لايوجد الاتصال بالانترنت", Snackbar.LENGTH_LONG)
                snack.show()
            }
        })


        binding.userToolbar.imgBack.setOnClickListener {
            it.findNavController().popBackStack()
        }


    }


    private fun getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                list.clear()

                for (item in snapshot.children) {
                    list.add(
                        UserModel(
                            item.key!!.toString(),
                            item.child("name").value.toString(),
                            item.child("email").value.toString(),
                            item.child("phone").value.toString(),
                            item.child("comment").value.toString()
                        )
                    )
                }

                adapter.setList(list)

            }

            override fun onCancelled(error: DatabaseError) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(requireContext(), "Fail to get data.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun initRecyclerView() {
        binding.recUser.setHasFixedSize(true)
        binding.recUser.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserListAdapter()
        binding.recUser.adapter = adapter
    }


    private fun onBackPressed() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireView().findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }
}