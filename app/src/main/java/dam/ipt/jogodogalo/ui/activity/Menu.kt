package dam.ipt.jogodogalo.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dam.ipt.jogodogalo.databinding.ActivityMenuBinding

class Menu : AppCompatActivity() {
    private lateinit var binding : ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Btn1.setOnClickListener{
            val jogo = Intent(this,Jogo::class.java)
            startActivity(jogo)
            finish()
        }
        binding.btnDef.setOnClickListener{
            val defs = Intent(this,Definicoes::class.java)
            startActivity(defs)
            finish()
        }

        binding.btnAbout.setOnClickListener{
            val acerca = Intent(this, Acerca::class.java)
            startActivity(acerca)
            finish()
        }

    }


}