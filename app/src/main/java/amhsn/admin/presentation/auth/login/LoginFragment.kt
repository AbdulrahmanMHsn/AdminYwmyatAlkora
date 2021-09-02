package amhsn.admin.presentation.auth.login

import amhsn.admin.R
import amhsn.admin.databinding.FragmentLoginBinding
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
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private var isValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
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

                if (txtPassword.length() == 0) {

                    txtPassword.setError("من فضلك ادخل الرقم السرى")

                    isValid = true
                }

                if (!isValid) {
                    if (Utils.isNetworkAvailable(requireContext())) {
                        userLogin(txtEmail.text.toString(), txtPassword.text.toString(), it)
                    }else{
                        val snack = Snackbar.make(it,"لايوجد الاتصال بالانترنت",Snackbar.LENGTH_LONG)
                        snack.show()
                    }
                }

            }


            btnRegisteration.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_registerationFragment)
            }
        }
    }

    fun userLogin(
        email: String,
        password: String,
        view: View
    ) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    view.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(
                        context,
                        "الرجاء ادخال البريد الالكترونى او الباسورد صحيح",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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