package app.makino.harutiro.photo

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    //インテントの戻るいちを指定
    val REQUEST_PREVIEW = 1
    val REQUEST_PICTURE = 2

    lateinit var currentPhotoUri :Uri

    var imageView:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener{
            preaview()
        }

        imageView = findViewById(R.id.outView)



        findViewById<Button>(R.id.button2).setOnClickListener {
            takePicuture()
        }

        findViewById<Button>(R.id.button4).setOnClickListener {
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.book).setOnClickListener {
            val intent = Intent(this,MainActivity3::class.java)
            startActivity(intent)
        }



    }

    private fun takePicuture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                val time: String = SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(Date())
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "${time}_.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                val collection = MediaStore.Images.Media
                        .getContentUri("external")
                val photoUri = contentResolver.insert(collection, values)
                photoUri?.let {
                    currentPhotoUri = it
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_PICTURE)
            }
        }
    }

    private fun preaview(){

        //インテント、Androidマニュフェストを使うため注意
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_PREVIEW)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //=====================================データを受け取る ========================================
        //画質悪いデータ
        if (requestCode == REQUEST_PREVIEW && data!=null && resultCode == RESULT_OK){
            //データの格納
            val imagebitmap = data?.extras?.get("data") as Bitmap
            //imageView?.setImageBitmap(imagebitmap)

            //＝＝＝＝＝＝＝＝＝＝＝＝＝＝BASE６４＝＝＝＝＝＝＝＝＝＝＝＝＝＝
            //エンコード
            val immagex: Bitmap = imagebitmap
            val baos = ByteArrayOutputStream()
            immagex.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            val output = Base64.encodeToString(b, Base64.NO_WRAP)

            //デコード
            val decodedByte: ByteArray = Base64.decode(output, 0)
            imageView?.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size))

        //フルデータ
        }else if(requestCode == REQUEST_PICTURE){
            when(resultCode){
                RESULT_OK ->{

                    //＝＝＝＝＝＝＝＝＝＝＝＝＝＝BASE６４＝＝＝＝＝＝＝＝＝＝＝＝＝＝
                    //エンコード
                    //uri→Bitmapに変換
                    val immagex: Bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), currentPhotoUri);
                    val baos = ByteArrayOutputStream()
                    immagex.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    val b: ByteArray = baos.toByteArray()
                    val output = Base64.encodeToString(b, Base64.NO_WRAP)

                    //デコード
                    val decodedByte: ByteArray = Base64.decode(output, 0)
                    imageView?.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size))

                }

                else ->{
                    contentResolver.delete(currentPhotoUri, null, null)
                }
            }
        }
    }

}