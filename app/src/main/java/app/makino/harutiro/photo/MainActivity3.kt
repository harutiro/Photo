package app.makino.harutiro.photo

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity3 : AppCompatActivity() {

    val REQUEST_PREVIEW = 1
    val REQUEST_PICTURE = 2

    var outImage:ImageView? = null

    lateinit var currentPhotoUri: Uri

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

        findViewById<Button>(R.id.inButton1).setOnClickListener{

            val context: Context = applicationContext

            // 保存先のフォルダー
            val cFolder: File? = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)

            //        *名前関係*       //
            //　フォーマット作成
            val fileDate: String = SimpleDateFormat("ddHHmmss", Locale.US).format(Date())
            //　名前作成
            val fileName: String = String.format("CameraIntent_%s.jpg", fileDate)

            //uriの前作成
            val cameraFile: File = File(cFolder, fileName)

            //uri最終作成
            currentPhotoUri = FileProvider.getUriForFile(this, context.packageName.toString() + ".fileprovider", cameraFile)


            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
            startActivityForResult(intent, REQUEST_PICTURE)
            Log.d("debug", "startActivityForResult()")


//            //================================もとのやつ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//            //カメラ起動アンド画像取得を指定
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            //インテントするときに対応したアプリがあるか判断
//            intent.resolveActivity(packageManager)
//
//            //日付データの取得フォーマットを指定
//            val time: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//
//            //コンテンツプロバイダへアクセスする
//            val values = ContentValues().apply {
//                //ファイル名の指定
//                put(MediaStore.Images.Media.DISPLAY_NAME, "${time}_.jpg")
//                //持ってくるファイル形式の指定
//                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            }
//
//            //画像メディアを格納するための外部ストレージの場所(external)を取得している
//            //内部ストレージ→internal
//            //外部ストレージ→external
//            val collection = MediaStore.Images.Media.getContentUri("external")
//
//            //コンテンツプロバイダにデータを挿入するときに呼び出される
//            //uri: データを保存する場所
//            //values: 新しく追加するコンテンツの情報
//            val photoUri = contentResolver.insert(collection, values)
//
//            //ファイルの場所を取得
//            photoUri?.let {
//                currentPhotoUri = it
//            }
//
//            //インテントとする先に持っていくデータ　保存先はどうするかなどの情報をおくる
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//            //インテント開始
//            startActivityForResult(intent, REQUEST_PICTURE)


        }
    }

    //インテントから帰ってきたデータはここに入ってくる
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //requestCodeにはインテントするときに渡したリクエストコードが入ってる
        //resultCodeには処理の結果が入ってる
        //RESULT_OK→正常終了
        if (requestCode == REQUEST_PREVIEW && resultCode == RESULT_OK) {

            //dateの中にアイコンデータが入ってる
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //はめ込み
            outImage?.setImageBitmap(imageBitmap)

        } else if (requestCode == REQUEST_PICTURE) {

            when (resultCode) {
                RESULT_OK -> {

                    outImage?.setImageURI(currentPhotoUri)

                }

                //正常じゃないとき　Cancelされたとき
                else -> {
                    //メディアプレイヤーに追加したデータを消去する
                    contentResolver.delete(currentPhotoUri, null, null)
                }

            }
        }

    }
}