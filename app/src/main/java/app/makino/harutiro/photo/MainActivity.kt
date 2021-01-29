package app.makino.harutiro.photo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    //インテントの戻るいちを指定
    val REQUEST_PREVIEW = 1

    var imageView:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener{
            preaview()
        }

        imageView = findViewById(R.id.imageView)






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

        //データを受け取る
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
        }
    }

}