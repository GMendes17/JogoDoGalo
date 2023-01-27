package dam.ipt.jogodogalo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        Btn1.setOnClickListener{

            Jogar()
        }

    }
    private fun Jogar(){
        val jogo = Intent(this,Jogo::class.java)
        startActivity(jogo)
    }

}