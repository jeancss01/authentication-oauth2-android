package br.com.tcc.oauth2app.feature.welcome.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.tcc.oauth2app.databinding.FragmentWelcomeBinding
import br.com.tcc.oauth2app.feature.welcome.viewModel.WelcomeViewModel
import br.com.tcc.oauth2app.feature.welcome.viewState.WelcomeState
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri

@AndroidEntryPoint
class WelcomeFragment : Fragment()  {
    private lateinit var binding: FragmentWelcomeBinding
    private val viewModel by viewModels<WelcomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        observeViewModel()
    }

    private fun setListeners() {
        binding.fragWelcomeBtnLogin.setOnClickListener {
            Toast.makeText(requireContext(), "Redirecionando para o fluxo de login...", Toast.LENGTH_SHORT).show()
            viewModel.getAuthenticationUrl()
        }
        binding.fragWelcomeBtnRegister.setOnClickListener {

        }
    }

    private fun observeViewModel() {
        viewModel.welcomeUiState.observe(viewLifecycleOwner) {
            when (it.welcomeState) {
                is WelcomeState.Loading -> {
                    // Show loading state
                }
                is WelcomeState.Success -> {
                    // Handle success state, e.g., navigate to another screen
                    val url = (it.welcomeState).urlAuthentication
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    startActivity(intent)
                }
                is WelcomeState.Error -> {
                    // Handle error state, e.g., show an error message
                }

                WelcomeState.Initial -> {
                    // Initial state, do nothing or show initial UI
                }
            }
        }
    }
}