package amhsn.admin.presentation.social

import amhsn.admin.R
import amhsn.admin.databinding.FragmentAddSocialBinding
import amhsn.admin.presentation.util.Utils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddSocialFragment : Fragment() {

    private lateinit var binding: FragmentAddSocialBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_social, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()

        firebaseDatabase = FirebaseDatabase.getInstance()

        databaseReference = firebaseDatabase.getReference("Social")
        databaseReference.keepSynced(true)


        binding.btnSave.setOnClickListener {


            if (binding.edTxtBody.length() == 0) {
                binding.edTxtBody.setError("هذا مطلوب")
                return@setOnClickListener
            }

            if (Utils.isNetworkAvailable(requireContext())) {
                addToFirebase()
            } else {
                val snack = Snackbar.make(it, "لايوجد الاتصال بالانترنت", Snackbar.LENGTH_LONG)
                snack.show()
            }
        }

        binding.imgBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

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

    fun addToFirebase() {
        val key: String = databaseReference.push().getKey().toString()
        databaseReference.child(key).setValue(binding.edTxtBody.text.toString())
        Toast.makeText(requireContext(), "تم الحفظ", Toast.LENGTH_SHORT).show()
        requireView().findNavController().popBackStack()
    }


}