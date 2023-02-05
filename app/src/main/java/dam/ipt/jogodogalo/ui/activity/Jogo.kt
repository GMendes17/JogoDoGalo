package dam.ipt.jogodogalo.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import dam.ipt.jogodogalo.R
import dam.ipt.jogodogalo.data.Session
import dam.ipt.jogodogalo.databinding.ActivityJogoBinding
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class Jogo : AppCompatActivity() {

    enum class Turn {
        jogador1,
        jogador2
    }

    private var firstTurn = Turn.jogador1
    private var currentTurn = Turn.jogador1

    private var PontoJgdr1 = 0
    private var PontoJgdr2 = 0

    private var boardList = mutableListOf<ImageButton>()

    private lateinit var binding: ActivityJogoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initBoard()
        binding.turnTV.text = "Vez $jogador1"

        readPoints()

        if (peca1 == null)
            peca1 = BitmapFactory.decodeResource(resources, R.drawable.x)

        if (peca2 == null)
            peca2 = BitmapFactory.decodeResource(resources, R.drawable.o)

        binding.butReset.setOnClickListener {

            val alertBuilder = android.app.AlertDialog.Builder(this)
            alertBuilder.setMessage("Tem a certeza de que quer apagar os pontos?")
                .setNegativeButton("NÃ£o", null)
                .setPositiveButton("Sim") { dialog, which ->

                    savePoints(0, 0)

                }
            val alert = alertBuilder.create()
            alert.show()
        }
    }


    private fun initBoard() {
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

    fun boardTapped(view: View) {
        if (view !is ImageButton)
            return
        addToBoard(view)

        if (checkForVictory("peca2")) {

            PontoJgdr2++
            result("$jogador2 Ganha")
            binding.vit2.text = "$jogador2 : $PontoJgdr2"

        } else if (checkForVictory("peca1")) {

            PontoJgdr1++
            result("$jogador1 Ganha")
            binding.vit1.text = "$jogador1 : $PontoJgdr1"

        } else if (fullBoard()) {
            result("Empate")
        }

        savePoints(PontoJgdr1, PontoJgdr2)
    }

    private fun setVisible(view: View){

        if (!view.isVisible){
            view.visibility = View.VISIBLE
        }
    }
    private fun setInvisible(view: View){

        if (view.isVisible){
            view.visibility = View.INVISIBLE
        }
    }

    private fun checkForVictory(s: String): Boolean {

        //Horizontal Victory
        if (match(binding.a1, s) && match(binding.a2, s) && match(binding.a3, s)){
            setVisible(binding.VitHor1)
            return true
        }
        if (match(binding.b1, s) && match(binding.b2, s) && match(binding.b3, s)){
            setVisible(binding.VitHor2)
            return true
        }


        if (match(binding.c1, s) && match(binding.c2, s) && match(binding.c3, s)){
            setVisible(binding.VitHor3)
            return true
        }



        //Vertical Victory
        if (match(binding.a1, s) && match(binding.b1, s) && match(binding.c1, s)){
            setVisible(binding.Vitver1)
            return true
        }


        if (match(binding.a2, s) && match(binding.b2, s) && match(binding.c2, s)){
            setVisible(binding.Vitver2)
            return true
        }


        if (match(binding.a3, s) && match(binding.b3, s) && match(binding.c3, s)){
            setVisible(binding.Vitver3)
            return true
        }

        //Diagonal Victory
        if (match(binding.a1, s) && match(binding.b2, s) && match(binding.c3, s)){
            setVisible(binding.Vitdig2)
            return true
        }

        if (match(binding.a3, s) && match(binding.b2, s) && match(binding.c1, s)){
            setVisible(binding.Vitdig1)
            return true
        }



        return false
    }
    private fun match(button: ImageButton, symbol: String): Boolean = button.tag == symbol


    private fun result(title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setPositiveButton("Novo Jogo")
            { _, _ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetBoard() {

        setInvisible(binding.VitHor1)
        setInvisible(binding.VitHor2)
        setInvisible(binding.VitHor3)
        setInvisible(binding.Vitver1)
        setInvisible(binding.Vitver2)
        setInvisible(binding.Vitver3)
        setInvisible(binding.Vitdig1)
        setInvisible(binding.Vitdig2)

        for (button in boardList) {
            button.setImageResource(0)
            button.tag = 0
        }

        if (firstTurn == Turn.jogador1)
            firstTurn = Turn.jogador2
        else if (firstTurn == Turn.jogador2)
            firstTurn = Turn.jogador1

        currentTurn = firstTurn
        setTurnLabel()
    }

    private fun fullBoard(): Boolean {
        for (button in boardList) {
            if (button.drawable == null)
                return false
        }
        return true
    }

    private fun addToBoard(button: ImageButton) {
        if (button.drawable != null)
            return



        if (currentTurn == Turn.jogador1) {
            button.setImageBitmap(peca1)
            button.tag = "peca1"
            currentTurn = Turn.jogador2
        } else if (currentTurn == Turn.jogador2) {
            button.setImageBitmap(peca2)
            button.tag = "peca2"
            currentTurn = Turn.jogador1
        }
        setTurnLabel()
    }

    private fun setTurnLabel() {
        var ajogar = ""
        if (currentTurn == Turn.jogador1)
            ajogar = "Vez $jogador1"
        else if (currentTurn == Turn.jogador2)
            ajogar = "Vez $jogador2"

        binding.turnTV.text = ajogar
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { pontos ->
            pontos[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private fun savePoints(jgdr1P: Int, jgdr2P: Int) {
        var player = "default"

        if (Session().getUser().id != 0)
            player = Session().getUser().id.toString()

        lifecycleScope.launch {
            save(
                player,
                "$jgdr1P:$jgdr2P"
            )

            PontoJgdr1 = jgdr1P
            PontoJgdr2 = jgdr2P

            updatePointsView()
        }
    }

    private fun readPoints() {
        var player = "default"

        if (Session().getUser().id != 0)
            player = Session().getUser().id.toString()

        lifecycleScope.launch {

            var ply1P = 0
            var ply2P = 0

            read(player)?.let {
                val points = it.split(":")
                ply1P = points[0].toInt()
                ply2P = points[1].toInt()
            }

            PontoJgdr1 = ply1P
            PontoJgdr2 = ply2P

            updatePointsView()
        }
    }

    private fun updatePointsView() {
        binding.vit1.text = "$jogador1 : $PontoJgdr1"
        binding.vit2.text = "$jogador2 : $PontoJgdr2"
    }


    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("pontos")

        var peca1: Bitmap? = null
        var peca2: Bitmap? = null

        var jogador1: String = "Jogador 1"
        var jogador2: String = "Jogador 2"
    }

    /**
     * Voltar ao login quando pressionado home
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}