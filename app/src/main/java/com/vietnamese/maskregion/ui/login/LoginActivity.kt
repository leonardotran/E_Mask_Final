package com.vietnamese.maskregion.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vietnamese.maskregion.MainActivity
import com.vietnamese.maskregion.R
import com.vietnamese.maskregion.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel


    val RC_SIGN_IN = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = Firebase.auth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)
            loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
                val loginState = it ?: return@Observer
                // disable login button unless both username / password is valid
                binding.login.isEnabled = loginState.isDataValid
                if (loginState.usernameError != null) {
                    binding.username.error = getString(loginState.usernameError)
                }
                if (loginState.passwordError != null) {
                    binding.password.error = getString(loginState.passwordError)
                }
            })
            loginViewModel.loginResult.observe(this@LoginActivity, Observer {
                val loginResult = it ?: return@Observer
                binding.loading.visibility = View.GONE
                if (loginResult.error != null) {
                    showLoginFailed(loginResult.error)
                }
                if (loginResult.success != null) {
                    updateUiWithUser(loginResult.success)
                }
                setResult(Activity.RESULT_OK)
                //Complete and destroy login activity once successful
                finish()
            })
            binding.username.afterTextChanged {
                loginViewModel.loginDataChanged(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }
            binding.password.apply {
                afterTextChanged {
                    loginViewModel.loginDataChanged(
                        binding.username.text.toString(),
                        binding.password.text.toString()
                    )
                }

                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE ->
                            loginViewModel.login(
                                binding.username.text.toString(),
                                binding.password.text.toString()
                            )
                    }
                    false
                }

                binding.login.setOnClickListener {
                    binding.loading.visibility = View.VISIBLE
                    loginViewModel.login(
                        binding.username.text.toString(),
                        binding.password.text.toString()
                    )
                }
            }
            val customLayout =
                AuthMethodPickerLayout.Builder(R.layout.activity_login)
                    .setGoogleButtonId(R.id.google)
                    .setEmailButtonId(R.id.login)
                    .build()

            binding.google.setOnClickListener {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder().setAvailableProviders(
                            arrayListOf(
                                AuthUI.IdpConfig.GoogleBuilder().build()
                            )
                        )
                        .setAuthMethodPickerLayout(customLayout)
                        .build(),
                    RC_SIGN_IN
                )
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
//            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Log.d("UID", "" + user?.uid)
                Log.d("Name", "" + user?.displayName)
                Log.d("Email", "" + user?.email)
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}