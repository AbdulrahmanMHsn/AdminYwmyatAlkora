package amhsn.admin.presentation.about

import amhsn.admin.R
import amhsn.admin.databinding.FragmentAboutBinding
import amhsn.admin.presentation.util.NetworkConnection
import amhsn.admin.presentation.util.Utils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*


class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_about, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("About")

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        binding.imgBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            val body: String = binding.edTxtBody.text.toString()
            addDatatoFirebase(body)

            if (Utils.isNetworkAvailable(requireContext())) {
                addDatatoFirebase(body)
            } else {
                val snack = Snackbar.make(it, "لايوجد الاتصال بالانترنت", Snackbar.LENGTH_LONG)
                snack.show()
            }
        }


        val networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner, Observer {
            if (it) {
                getdata()
            } else {
                getdata()
                val snack = Snackbar.make(requireView(), "لايوجد الاتصال بالانترنت", Snackbar.LENGTH_LONG)
                snack.show()
            }
        })


    }


    private fun getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                val value = snapshot.getValue().toString()
                binding.edTxtBody.setText(value)


            }

            override fun onCancelled(error: DatabaseError) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(requireContext(), "Fail to get data.", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun addDatatoFirebase(body: String) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference.setValue(body)

                Toast.makeText(requireContext(), "تم الحفظ", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "فشل في إضافة البيانا ", Toast.LENGTH_SHORT)
                    .show()
            }
        })
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