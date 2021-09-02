package amhsn.admin.presentation.auth.registeration

import amhsn.admin.R
import amhsn.admin.databinding.FragmentRegisterationBinding
import amhsn.admin.presentation.util.Utils
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class RegisterationFragment : Fragment() {

    private lateinit var binding: FragmentRegisterationBinding
    private lateinit var mAuth: FirebaseAuth
    private var isValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registeration, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        onBackPressed()

        mAuth = FirebaseAuth.getInstance()

        binding.apply {
            btnLogin.setOnClickListener {

                if (txtEmail.length() == 0) {

                    txtEmail.setError("من فضلك ادخل البريد الالكترونى")

                    isValid = true
                }

                if (Patterns.EMAIL_ADDRESS.matcher(txtEmail.toString()).matches()) {

                    txtEmail.setError("تحقق من صيغة الايميل الالكترونى")

                    isValid = true
                }

                if (txtPassword.length() == 0) {

                    txtPassword.setError("من فضلك ادخل الرقم السرى")

                    isValid = true
                }

                if (txtPassword.length() < 6) {

                    txtPassword.setError("لا يقل عن 6 ارقام او احرف")

                    isValid = true
                }

                if (!isValid) {
                    if (Utils.isNetworkAvailable(requireContext())) {
                        userRegisteration(txtEmail.text.toString(), txtPassword.text.toString(), it)
                    } else {
                        val snack =
                            Snackbar.make(it, "لايوجد الاتصال بالانترنت", Snackbar.LENGTH_LONG)
                        snack.show()
                    }

                }

            }
        }
    }


    fun userRegisteration(
        email: String,
        password: String,
        view: View
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    view.findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        "خطأ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"هذا الايميل تم اختياره", Toast.LENGTH_SHORT).show()
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


}