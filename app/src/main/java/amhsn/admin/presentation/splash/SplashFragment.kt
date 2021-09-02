package amhsn.admin.presentation.home

import amhsn.admin.R
import amhsn.admin.presentation.util.Utils
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import java.util.*


class SplashFragment : Fragment() {


    private lateinit var authStateListener: AuthStateListener
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Handler().postDelayed({
//            view.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
//        }, 3000)


        authStateListener = AuthStateListener { firebaseAuth: FirebaseAuth? ->
            val user = auth.currentUser
            Timer().schedule(object : TimerTask() {
                override fun run() {

                    if (user != null) {
                        Utils.setmUID(auth.currentUser.uid)
                        Utils.setmEmail(auth.currentUser.email)
                        view.findNavController()
                            .navigate(R.id.action_splashFragment_to_homeFragment)
                    } else {
                        view.findNavController()
                            .navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }, 3000)
        }
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener)
        }
    }

}