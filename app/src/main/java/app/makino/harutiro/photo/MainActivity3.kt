package app.makino.harutiro.photo

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView

class MainActivity3 : AppCompatActivity() {

    val REQUEST_PREVIEW = 1
    val REQUEST_PICTURE = 2

    var outImage:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        outImage = findViewById(R.id.outView)


        //インテント開始
        findViewById<Button>(R.id.inButton).setOnClickListener {

            //カメラを起動させて、画像を取得と指定
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //インテントするときに対応したアプリケーションがあるかどうか判定してくれるところ
            intent.resolveActivity(packageManager)
            //どこからやってきたインテントか判断するために送る目印リクエストコード
            startActivityForResult(intent, REQUEST_PREVIEW)

        }
    }

    //インテントから帰ってきたデータはここに入ってくる
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //requestCodeにはインテントするときに渡したリクエストコードが入ってる
        //resultCodeには処理の結果が入ってる
        //RESULT_OK→正常終了
        if(requestCode == REQUEST_PREVIEW && resultCode == RESULT_OK){

            //dateの中にアイコンデータが入ってる
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //はめ込み
            outImage?.setImageBitmap(imageBitmap)
        }
    }
}