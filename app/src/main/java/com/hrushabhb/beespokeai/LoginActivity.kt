package com.hrushabhb.beespokeai

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hrushabhb.beespokeai.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        binding.loginButton.setOnClickListener {

            val username = "mor_2314"
            val password = "83r5^_"
            performLogin(username, password)

        }

        binding.signupbtn.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performLogin(username: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val requestBody = JSONObject()
                requestBody.put("username", username)
                requestBody.put("password", password)
                val url = URL("https://fakestoreapi.com/auth/login")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"

                connection.setRequestProperty("Content-Type", "application/json")

                connection.doInput = true
                connection.doOutput = true

                val outputStreamWriter = OutputStreamWriter(connection.outputStream)
                outputStreamWriter.write(requestBody.toString())
                outputStreamWriter.close()

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    val responseStream = connection.inputStream
                    val responseText = responseStream.bufferedReader().use { it.readText() }

                    val jsonResponse = JSONObject(responseText)
                    val token = jsonResponse.optString("token")
                    if (token.isNotEmpty()) {

                        sharedPreferences.edit().putString("token", token).apply()

                        withContext(Dispatchers.Main) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            showToast("Login failed")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("HTTP Error: $responseCode")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("An error occurred: ${e.message}")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}