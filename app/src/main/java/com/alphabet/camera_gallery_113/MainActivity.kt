package com.alphabet.camera_gallery_113

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.BitmapCompat
import com.alphabet.camera_gallery_113.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //gallery
        val iGallery = Intent()
        iGallery.type = "image/*"
        iGallery.action = Intent.ACTION_GET_CONTENT


        val galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){result ->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.let{

                    val uri = result.data!!.data

                    lateinit var imgBitmap: Bitmap

                    try{
                        imgBitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            uri
                        )

                        val baos = ByteArrayOutputStream()

                        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                        val bytes = baos.toByteArray()

                        binding.imageView.setImageBitmap(imgBitmap)
                    } catch(e: Exception){

                    }
                }

            }
        }

        //camera
        val iCam = Intent()
        iCam.action = MediaStore.ACTION_IMAGE_CAPTURE

        val cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){result ->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.let{
                    val image = result.data!!.extras!!.get("data")  as Bitmap
                    binding.imageView.setImageBitmap(image)
                }

            }
        }


        with(binding){

            btnCamera.setOnClickListener{
                cameraLauncher.launch(iCam)
            }

            btnGallery.setOnClickListener{
                galleryLauncher.launch(iGallery)
            }

        }

    }

}