package dam.ipt.jogodogalo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import dam.ipt.jogodogalo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class Jogo : AppCompatActivity() {

    enum class Turn
    {
        jogador1,
        jogador2
    }

    private var firstTurn = Turn.jogador1
    private var currentTurn = Turn.jogador1

    private var PontoJgdr1 = 0
    private var PontoJgdr2 = 0

    private var boardList = mutableListOf<Button>()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard()
        turnTV.text = "Vez " + currentTurn.toString()

    }


    private fun initBoard()
    {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)

    }

    fun boardTapped(view: View)
    {
        if(view !is Button)
            return
        addToBoard(view)

        if(checkForVictory(NOUGHT))
        {
            PontoJgdr2++
            result("Jogador 1 Ganha")
        }
        else if(checkForVictory(CROSS))
        {
            PontoJgdr1++
            result("Jogador 2 Ganha")
        }else if(fullBoard())
        {
            result("Empate")
        }



    }

    private fun checkForVictory(s: String): Boolean
    {
        //Horizontal Victory
        if(match(binding.a1,s) && match(binding.a2,s) && match(binding.a3,s))
            return true
        if(match(binding.b1,s) && match(binding.b2,s) && match(binding.b3,s))
            return true
        if(match(binding.c1,s) && match(binding.c2,s) && match(binding.c3,s))
            return true

        //Vertical Victory
        if(match(binding.a1,s) && match(binding.b1,s) && match(binding.c1,s))
            return true
        if(match(binding.a2,s) && match(binding.b2,s) && match(binding.c2,s))
            return true
        if(match(binding.a3,s) && match(binding.b3,s) && match(binding.c3,s))
            return true

        //Diagonal Victory
        if(match(binding.a1,s) && match(binding.b2,s) && match(binding.c3,s))
            return true
        if(match(binding.a3,s) && match(binding.b2,s) && match(binding.c1,s))
            return true

        return false
    }

    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    private fun result(title: String)
    {
        val message = "\n Jogador 1 $PontoJgdr2\n\n Jogador2 $PontoJgdr1"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Novo Jogo")
            { _,_ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetBoard()
    {
        for(button in boardList)
        {
            button.text = ""
        }

        if(firstTurn == Turn.jogador1)
            firstTurn = Turn.jogador2
        else if(firstTurn == Turn.jogador2)
            firstTurn = Turn.jogador1

        currentTurn = firstTurn
        setTurnLabel()
    }

    private fun fullBoard(): Boolean
    {
        for(button in boardList)
        {
            if(button.text == "")
                return false
        }
        return true
    }

    private fun addToBoard(button: Button)
    {
        if(button.text != "")
            return



        if(currentTurn == Turn.jogador1)
        {
            button.text = NOUGHT
            currentTurn = Turn.jogador2
        }
        else if(currentTurn == Turn.jogador2)
        {
            button.text = CROSS
            currentTurn = Turn.jogador1
        }
        setTurnLabel()
    }

    private fun setTurnLabel()
    {
        var Ajogar = ""
        if(currentTurn == Turn.jogador1)
            Ajogar = "Vez Jogador 1"
        else if(currentTurn == Turn.jogador2)
            Ajogar = "Vez Jogador 2"

        binding.turnTV.text = Ajogar
    }

    companion object
    {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }
}