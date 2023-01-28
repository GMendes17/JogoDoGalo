package dam.ipt.jogodogalo

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.ImageView
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



        binding.btnP1.setOnClickListener{

            escolherImg(binding.imageView1)
        }

        binding.btnP2.setOnClickListener{

            escolherImg(binding.imageView2)

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
    }

    private fun escolherImg(imageView: ImageView){



        addImage = imageView

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
            Toast.makeText(this, "Não há uma aplicação que suporta esta ação", Toast.LENGTH_SHORT).show()
        }

        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            cameraLauncher.launch(intent1)
        }catch (e: Exception){
            Toast.makeText(this, "Não há uma aplicação que suporta esta ação", Toast.LENGTH_SHORT).show()
        }



    }
}