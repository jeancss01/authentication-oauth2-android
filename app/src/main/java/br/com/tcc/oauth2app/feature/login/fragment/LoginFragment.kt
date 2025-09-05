package br.com.tcc.oauth2app.feature.login.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import br.com.tcc.oauth2app.R
import br.com.tcc.oauth2app.common.closeKeyboard
import br.com.tcc.oauth2app.common.collectInLifecycle
import br.com.tcc.oauth2app.common.showCustomDialog
import br.com.tcc.oauth2app.databinding.FragmentLoginBinding
import br.com.tcc.oauth2app.feature.login.viewModel.LoginViewModel
import br.com.tcc.oauth2app.feature.login.viewState.LoginActionState
import br.com.tcc.oauth2app.feature.login.viewState.LoginState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        observeViewModel()
        setLoginDefault()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        val code = arguments?.getString("code")
        Timber.d("Received code_challenge: $code")
        if (code != null) {
            viewModel.setCodeChallenge(code)
        } else {
            Timber.d("No code_challenge received")
        }
    }

    private fun setListeners() {
        binding.btnLogin.setOnClickListener {
            setLoading(true)
            closeKeyboard()

            val email = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.edtUsername.error = "Email é obrigatório"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtPassword.error = "Senha é obrigatória"
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.edtPassword.error = "A senha deve ter pelo menos 6 caracteres"
                return@setOnClickListener
            }

            validateInputs()
        }

    }

    private fun validateInputs() {
        val email = binding.edtUsername.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        viewModel.validateInputs(email, password)


    }

    private fun observeViewModel() {
        viewModel.loginViewState.observe(viewLifecycleOwner) {
            when (it.loginState) {
                is LoginState.ValidInputs -> {
                    viewModel.login(
                        email = binding.edtUsername.text.toString().trim(),
                        password = binding.edtPassword.text.toString().trim()
                    )
                    setLoading(false)
                }
                is LoginState.InvalidInputs -> {
                    setLoading(false)
                    showErrorInputsInvalid()
                }
                is LoginState.Loading -> {
                    closeKeyboard()
                    setLoading(true)
                }
                is LoginState.EmptyInputs -> {}
            }
        }

        viewLifecycleOwner.collectInLifecycle(viewModel.loginActionState) {
            when (it) {
                is LoginActionState.ValidCredentials -> {
                    setLoading(false)
                    Toast.makeText(requireContext(), "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    // Navigate to the next screen or perform any action after successful login
                    val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
                    findNavController().navigate(action)
                }
                is LoginActionState.InvalidCredentialsError -> {
                    setLoading(false)
                    showErrorInputsInvalid()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.fragLoginViewData.visibility = if (loading) View.GONE else View.VISIBLE
        binding.fragLoginViewLoading.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showErrorInputsInvalid() {
        context?.showCustomDialog(
            title = getString(R.string.label_error_title_default),
            message = getString(R.string.label_error_retry_default),
            cancelable = false,
            positiveBtnMsgId = R.string.ok,
            positiveClickListener = { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    private fun setLoginDefault() {
        binding.edtUsername.setText("jeancss01@gmail.com")
        binding.edtPassword.setText("teste123")
        binding.edtUsername.error = null
        binding.edtPassword.error = null
    }
}