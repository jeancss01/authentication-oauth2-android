package br.com.tcc.oauth2app.feature.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.tcc.oauth2app.common.SecureStorage
import br.com.tcc.oauth2app.databinding.FragmentProfileBinding
import br.com.tcc.oauth2app.feature.profile.viewModel.ProfileViewModel
import br.com.tcc.oauth2app.feature.profile.viewState.ProfileState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    @Inject
    lateinit var secureStorage: SecureStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAccount()
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.btnLogout.setOnClickListener {
            // Clear secure storage and navigate to WelcomeFragment
            secureStorage.clearTokens()

            val action = ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun setObservers() {
        viewModel.profileViewState.observe(viewLifecycleOwner) { state ->
            when (state.profileState) {
                is ProfileState.Loading -> {
                    showLoading()
                }
                is ProfileState.Success -> {
                    // Update UI with account data
                    val account = state.profileState.account
                    binding.tvProfileName.text = account.name
                    binding.tvProfileEmail.text = account.email
                    binding.tvProfileCountry.text = account.country
                    binding.tvProfileCity.text = account.city
                    binding.tvProfileState.text = account.state
                    hideLoading()
                }
                is ProfileState.Error -> {
                    // Show error message
                    val errorMessage = state.profileState.message
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }

                ProfileState.Initial -> TODO()
            }
        }
    }

    private fun showLoading() {
        // Show loading indicator
        binding.pbLoading.visibility = View.VISIBLE
        binding.groupContent.visibility = View.GONE
    }

    private fun hideLoading() {
        // Hide loading indicator
        binding.pbLoading.visibility = View.GONE
        binding.groupContent.visibility = View.VISIBLE
    }
}