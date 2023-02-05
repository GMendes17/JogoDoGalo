package dam.ipt.jogodogalo.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.android.volley.Response
import com.google.android.material.textfield.TextInputLayout
import dam.ipt.jogodogalo.R
import dam.ipt.jogodogalo.databinding.ActivityRegistarBinding
import dam.ipt.jogodogalo.volley.VolleyRequest
import org.json.JSONObject

class Registar : AppCompatActivity() {
    private lateinit var binding: ActivityRegistarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val usernameEt: EditText = binding.userName
        val usernameLayout: TextInputLayout = binding.userNameLayout
        val passEt: EditText = binding.passInput
        val passLayout: TextInputLayout = binding.passInputLayout
        val passConfirmEt: EditText = binding.userConfirmPass

        usernameEt.doOnTextChanged { text, start, before, count ->
            usernameLayout.error = null
        }

        passEt.doOnTextChanged { text, start, before, count ->
            passLayout.error = null
        }

        //listener para edição do user
        binding.submitButton.setOnClickListener {
            val usernameText = usernameEt.text.toString()
            val pass = passEt.text.toString()
            val passConfirm = passConfirmEt.text.toString()

            if (usernameText.isNotEmpty()) {

                if (pass.isNotEmpty()) {
                    if (pass == passConfirm) {

                        //preparar pedido para a API
                        val response = Response.Listener<String> { response ->
                            //Log.e("res", response.toString())
                            Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                        }

                        val responseError = Response.ErrorListener { error ->
                            Log.e("res", error.toString())
                            Toast.makeText(this, "Conecte-se à internet para criar o utilizador", Toast.LENGTH_SHORT).show()
                        }

                        val jsonBody = JSONObject()
                        jsonBody.put("username", usernameText)
                        jsonBody.put("password", pass)
                        jsonBody.put("img", Definicoes.bitmapToBase64(BitmapFactory.decodeResource(resources, R.drawable.x)))

                        VolleyRequest().User().create(this, response, responseError, jsonBody)

                    }else{
                        Toast.makeText(this, "As palavras-passe não são iguais", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    passLayout.error = "A Palavra-Passe é obrigatória"
                }

            }else{
                usernameLayout.error = "O Nome de Utilizador é obrigatório"
            }
        }

    }

    /**
     * Voltar ao login quando pressionado home
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}