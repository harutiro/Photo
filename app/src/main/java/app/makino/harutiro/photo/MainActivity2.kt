package app.makino.harutiro.photo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MainActivity2 : AppCompatActivity() {

    private val RESULT_CAMERA = 1001

    private var imageView: ImageView? = null
    private var cameraUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("debug", "onCreate()")
        setContentView(R.layout.activity_main2)


        imageView = findViewById(R.id.outView)
        val cameraButton = findViewById<Button>(R.id.inButton)
        cameraButton.setOnClickListener {

            //権限
            if (isExternalStorageWritable()) {
                cameraIntent()
            }
        }
    }

    private fun cameraIntent() {
        val context: Context = applicationContext
        // 保存先のフォルダー
        val cFolder: File? = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        Log.d("log", "path: " + java.lang.String.valueOf(cFolder))

        // ファイル名
        val fileDate: String = SimpleDateFormat("ddHHmmss", Locale.US).format(Date())
        val fileName: String = String.format("CameraIntent_%s.jpg", fileDate)

        //uri
        val cameraFile: File = File(cFolder, fileName)

        cameraUri = FileProvider.getUriForFile(this, context.packageName.toString() + ".fileprovider", cameraFile)


        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        startActivityForResult(intent, RESULT_CAMERA)
        Log.d("debug", "startActivityForResult()")
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == RESULT_CAMERA) {
            if (cameraUri != null && isExternalStorageReadable()) {
                imageView!!.setImageURI(cameraUri)
            } else {
                Log.d("debug", "cameraUri == null")
            }
        }
    }

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state ||
                Environment.MEDIA_MOUNTED_READ_ONLY == state
    }
}