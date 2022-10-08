package binar.academy.challenge_chapter5.ui.authentication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import binar.academy.challenge_chapter5.R
import binar.academy.challenge_chapter5.data.model.UserResponse
import binar.academy.challenge_chapter5.databinding.FragmentLoginBinding
import binar.academy.challenge_chapter5.ui.authentication.viewmodel.AuthenticationViewModel
import binar.academy.challenge_chapter5.utils.Constant.Companion.ID_USER
import binar.academy.challenge_chapter5.utils.Constant.Companion.STATUS_LOGIN
import binar.academy.challenge_chapter5.utils.Constant.Companion.USER
import binar.academy.challenge_chapter5.utils.Constant.Companion.USERNAME
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authenticationViewModel : AuthenticationViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(USER, Context.MODE_PRIVATE)

        binding.apply {

            btnLogin.setOnClickListener {
                login()
            }

            btnToRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    private fun login() {
        binding.apply {
            val email = etEmailLogin.text.toString()
            val password = etPassLogin.text.toString()
            val listUser : MutableList<UserResponse> = mutableListOf()

            if (etEmailLogin.text!!.isEmpty() || etPassLogin.text!!.isEmpty()){
                Snackbar.make(binding.root, getString(R.string.tidak_boleh_kosong), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                    .show()
            }else{
                authenticationViewModel.login()
                authenticationViewModel.loginObserver().observe(viewLifecycleOwner){
                    if (it != null){
                        it.forEach { users ->
                            listUser.addAll(mutableListOf(users))
                        }

                        val currentUser = listUser.filter { it.email == email && it.password == password }
                        if (currentUser.isNotEmpty()){
                            inputUsernameSharePreferences(currentUser[0].username, currentUser[0].id)

                            Snackbar.make(binding.root, getString(R.string.login_berhasil), Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                                .show()

                            Log.d("User", currentUser.toString())

                            listUser.clear()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }else{
                            Snackbar.make(binding.root, getString(R.string.email_password_salah), Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                                .show()
                        }
                    }else{
                        Snackbar.make(binding.root, getString(R.string.login_gagal), Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.light_gray))
                            .setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
                            .show()
                    }
                }
            }
        }
    }

    private fun inputUsernameSharePreferences(username : String, id : String) {
        val setUsername = sharedPreferences.edit()
        setUsername.putString(USERNAME, username)
        setUsername.putString(ID_USER, id)
        setUsername.putBoolean(STATUS_LOGIN, true)
        setUsername.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}