package dam.ipt.jogodogalo

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dam.ipt.jogodogalo.databinding.ActivityDefinicoesBinding
import dam.ipt.jogodogalo.databinding.ActivityMenuBinding



class Definicoes : AppCompatActivity() {

    private lateinit var binding : ActivityDefinicoesBinding
    lateinit var pickLauncher: ActivityResultLauncher<Intent>
    //Utilização da camera
    lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    //Autorização de permissões
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var addImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definicoes)
        binding = ActivityDefinicoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Jogo.peca1 == null)
            Jogo.peca1 = BitmapFactory.decodeResource(resources, R.drawable.x)

        if (Jogo.peca2 == null)
            Jogo.peca2 = BitmapFactory.decodeResource(resources, R.drawable.o)



        if (Jogo.jogador1 != "Jogador 1")
            binding.ply1Input.setText(Jogo.jogador1, TextView.BufferType.EDITABLE)

        if (Jogo.jogador2 != "Jogador 2")
            binding.ply2Input.setText(Jogo.jogador2, TextView.BufferType.EDITABLE)



        binding.imageView1.setImageBitmap(Jogo.peca1)
        binding.imageView2.setImageBitmap(Jogo.peca2)

        binding.imageView1.setOnClickListener{
            escolherImg(this, binding.imageView1)
        }

        binding.imageView2.setOnClickListener{
            escolherImg(this, binding.imageView2)

        }

        cameraLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {

                    val bundle: Bundle? = result.data!!.extras
                    val bitmap = bundle?.get("data") as Bitmap?

                    if(bitmap != null)
                        addImage.setImageBitmap(bitmap)

                }
            }

        pickLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {

                    val selectedImage: Uri? = result.data?.data

                    if(selectedImage != null)
                        addImage.setImageURI(selectedImage)
                }
            }

        val requestPermissionIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // caso a permissão seja concedida, procedemos com a inicialização
                    pickLauncher.launch(requestPermissionIntent)
                } else {

                    // caso a permissão seja negada, informamos que não é possível continuar com o pretendido
                    val alertBuilder = AlertDialog.Builder(this)
                    alertBuilder.setMessage("Não é possível escolher imagens guardadas, conceda a permissão e tente novamente")
                        .setCancelable(false)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Conceder") { dialog, which ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                    val alert = alertBuilder.create()
                    alert.show()
                }
            }

        binding.btnSave.setOnClickListener{
            Jogo.peca1 = convertToBitmap(binding.imageView1.drawable, binding.imageView1.drawable.intrinsicWidth, binding.imageView1.drawable.intrinsicHeight)
            Jogo.peca2 = convertToBitmap(binding.imageView2.drawable, binding.imageView2.drawable.intrinsicWidth, binding.imageView2.drawable.intrinsicHeight)

            val player1Name = binding.ply1Input.text.toString()
            val player2Name = binding.ply2Input.text.toString()

            if (player1Name.isNotEmpty())
                Jogo.jogador1 = player1Name

            if (player2Name.isNotEmpty())
                Jogo.jogador2 = player2Name

            Toast.makeText(this, "Definições guardadas com sucesso", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Inicia o popup para escolher uma imagem/capturar uma com a camara
     */
    private fun escolherImg(context: Context, imageView: ImageView) {

        addImage = imageView

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Adiconar Imagem")

        val items = arrayOf("Escolher Imagem", "Câmara")

        builder.setItems(items) { dialog, which ->
            when (which) {
                0 -> {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                    try {

                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            -> {
                                // You can use the API that requires the permission.
                                pickLauncher.launch(intent)
                            }
                            else -> {
                                // You can directly ask for the permission.
                                // The registered ActivityResultCallback gets the result of this request.
                                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        }

                    }catch (e: Exception){
                        Toast.makeText(context, "Não há uma aplicação que suporta esta ação", Toast.LENGTH_SHORT).show()
                    }

                    dialog.dismiss()
                }
                1 -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    try {
                        cameraLauncher.launch(intent)
                    }catch (e: Exception){
                        Toast.makeText(context, "Não há uma aplicação que suporta esta ação", Toast.LENGTH_SHORT).show()
                    }

                    dialog.dismiss()
                }
            }
        }

        val typeMatDialog = builder.create()
        typeMatDialog.show()
    }

    /**
     * Converte uma imagem num bitmap
     */
    private fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, widthPixels, heightPixels)
        drawable.draw(canvas)
        return bitmap
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