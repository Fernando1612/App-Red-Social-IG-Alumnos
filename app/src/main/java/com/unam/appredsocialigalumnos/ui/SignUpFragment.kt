package com.unam.appredsocialigalumnos.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.unam.appredsocialigalumnos.R
import com.unam.appredsocialigalumnos.databinding.FragmentSingUpBinding
import kotlinx.coroutines.launch
import kotlin.Result.Companion.failure

class SignUpFragment :  FragmentBase<FragmentSingUpBinding>(
    R.layout.fragment_sing_up, FragmentSingUpBinding::bind) {

    private lateinit var auth: FirebaseAuth

    private lateinit var mDatabase: DatabaseReference

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        //Database
        mDatabase =  FirebaseDatabase.getInstance().reference

        // DataStore
        dataStore = context!!.applicationContext.createDataStore(name = "settings")
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            reload()
        }
    }

    private fun reload() {

    }

    override fun initElements() {
        binding.btnRegister.setOnClickListener {
            val username = binding.tvUsernameSignup.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.editPasswordSingup.text.toString()
            val repassword = binding.editRePasswordSignup.text.toString()
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repassword.isNotEmpty()){
                if (password == repassword){
                    createAccount(email,password)
                }
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    //dataStore
                    lifecycleScope.launch{
                        save_email("email",email)
                        save_password("password", password)
                    }

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    mDatabase.createUser(user!!.uid, user.toString()){
                        startNavMainActivity(user)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    startNavMainActivity(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun startNavMainActivity(user: FirebaseUser?) {
        val intent = Intent(requireContext(),NavigationActivity::class.java)
        intent.putExtra(USERNAME_KEY, user)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun DatabaseReference.createUser(uid: String, user: String, onSuccess: () -> Unit) {
        val reference = child("users").child(uid).child("photo")
        reference.setValue(user).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                Log.w(TAG, "createUserWithEmail:failure")
            }
        }
    }

    private suspend fun save_email(key: String, email: String){
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = email
        }
    }
    private suspend fun save_password(key: String, password: String){
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = password
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}