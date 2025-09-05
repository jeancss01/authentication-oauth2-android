package br.com.tcc.oauth2app.feature.splash.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.tcc.oauth2app.R
import br.com.tcc.oauth2app.common.SecureStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject
    lateinit var secureStorage: SecureStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = activity?.intent?.data
        if (data != null && data.scheme == "br.com.tcc.oauth2app" && data.host == "local") {
            val uri: Uri? = activity?.intent?.data
            val code = uri?.getQueryParameter("code_challenge")
            if (code != null) {
                SplashFragmentDirections.actionSplashFragmentToLoginFragment(code).let {
                    findNavController().navigate(it)
                }
            } else {
                verifyAuthenticated()
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                verifyAuthenticated()
            }, 500)
        }
    }


    private fun verifyAuthenticated() {
        val isAuthenticated = secureStorage.getToken()?.isNotEmpty()
        val tokenExpired = secureStorage.isTokenExpired()

        if (isAuthenticated == true) {
            val action = SplashFragmentDirections.actionSplashFragmentToProfileFragment()
            findNavController().navigate(action)
        } else {
            val action = SplashFragmentDirections.actionSplashFragmentToWelcomeFragment()
            findNavController().navigate(action)
        }


    }
}