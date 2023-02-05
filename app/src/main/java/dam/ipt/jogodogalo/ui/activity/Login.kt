package dam.ipt.jogodogalo.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.android.volley.Response
import dam.ipt.jogodogalo.data.Session
import dam.ipt.jogodogalo.databinding.ActivityLoginBinding
import dam.ipt.jogodogalo.volley.VolleyRequest
import org.json.JSONObject

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //botoes utilizados na view
        val loginButton = binding.submitButton
        val guestButton = binding.buttonGuest
        val createAccount = binding.buttonCreateAccount
        val username = binding.nameInput
        val password = binding.passInput

        username.doOnTextChanged { text, start, before, count ->
            binding.nameInputLayout.error = null
        }

        password.doOnTextChanged { text, start, before, count ->
            binding.passInputLayout.error = null
        }

        Jogo.peca1 = null
        Jogo.peca2 = null

        Jogo.jogador1 = "Jogador 1"
        Jogo.jogador2 = "Jogador 2"

        Session().setUser(0, "", "", "")

        //listener do login
        loginButton.setOnClickListener{
            val usernameTxt = username.text.toString()
            val passwordTxt = password.text.toString()

            if (usernameTxt.isNotEmpty()) {
                if (passwordTxt.isNotEmpty()) {
                    //preparar o pedido para o API
                    val response = Response.Listener<String> { response ->
                        val res = response.trim('"').split(';')

                        if (res[0].toDoubleOrNull() != null){
                            //preparar a sessão
                            Session().setUser(res[0].toInt(), usernameTxt, passwordTxt, res[1])

                            Jogo.jogador1 = usernameTxt
                            Jogo.peca1 = Definicoes.base64ToBitmap(res[1])

                            val intent = Intent(this, Menu::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "O nome de utilizador ou palavra-passe está incorreto", Toast.LENGTH_SHORT).show()
                        }
                    }

                    val responseError = Response.ErrorListener { error ->
                        Log.e("res", error.toString())
                        Toast.makeText(this, "Não foi possível fazer o login, tente novamente mais tarde", Toast.LENGTH_SHORT).show()
                    }

                    val jsonBody = JSONObject()
                    jsonBody.put("username", usernameTxt)
                    jsonBody.put("password", passwordTxt)

                    VolleyRequest().Auth().login(this, response, responseError, jsonBody)

                }else{
                    binding.passInputLayout.error = "A Palavra-Passe é obrigatória"
                }

            }else{
                binding.nameInputLayout.error = "O Nome de Utilizador é obrigatório"
            }

        }

        //listener do convidado
        guestButton.setOnClickListener{
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }

        //listener de criar conta
        createAccount.setOnClickListener{
            val intent = Intent(this, Registar::class.java)
            startActivity(intent)
            finish()
        }

    }
}