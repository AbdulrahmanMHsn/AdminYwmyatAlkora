package amhsn.admin.presentation.home

import amhsn.admin.R
import amhsn.admin.data.model.HomeModel
import amhsn.admin.databinding.FragmentHomeBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HomeGridAdapter
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()
        // initlization firebase authentication
        mAuth = FirebaseAuth.getInstance()

        initRecyclerView()

        val list: List<HomeModel> = mutableListOf(
            HomeModel(
                1, getString(R.string.about),
                R.drawable.ic_info
            )
            , HomeModel(
                2, getString(R.string.social),
                R.drawable.ic_social_media
            )
            , HomeModel(
                3, getString(R.string.user),
                R.drawable.ic_user
            )
            , HomeModel(
                4, getString(R.string.logOut),
                R.drawable.ic_logout
            )

        )

        adapter.setList(list)

    }

    private fun initRecyclerView() {
        binding.homeContainer.setHasFixedSize(true)
        binding.homeContainer.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter =
            HomeGridAdapter({ selectedItem: HomeModel ->
                listItemClicked(selectedItem)
            })
        binding.homeContainer.adapter = adapter
    }

    private fun listItemClicked(selectedItem: HomeModel) {
        when (selectedItem.id) {
            1 -> {
                requireView().findNavController()
                    .navigate(R.id.action_homeFragment_to_aboutFragment)
            }
            2 -> {
                requireView().findNavController()
                    .navigate(R.id.action_homeFragment_to_socialFragment)
            }
            3 -> {
                requireView().findNavController()
                    .navigate(R.id.action_homeFragment_to_userFragment)            }
            4 -> {
                logOut()
            }

        }
    }

    fun logOut() {
        mAuth.signOut()
        requireView().findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    private fun onBackPressed() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

}