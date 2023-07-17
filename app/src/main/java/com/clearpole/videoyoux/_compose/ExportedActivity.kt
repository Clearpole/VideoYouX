package com.clearpole.videoyoux._compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.clearpole.videoyoux.R
import com.clearpole.videoyoux._compose.theme.VideoYouXTheme
import com.clearpole.videoyoux._utils.SubStringX.Companion.subStringX
import com.drake.serialize.intent.openActivity
import com.drake.tooltip.toast
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExportedActivity : ComponentActivity() {

    private var isGetPermission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        openActivity<MainPlayerActivity>(
            "uri" to intent.data!!,
            "paths" to intent.data!!.path
        )
        finish()
        /*val folder = intent.data!!.path!!.subStringX("/Android/data/", "/")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && intent.data!!.path!!.contains(
                "/Android/data/"
            ) && !canReadDataDir(folder!!)
        ) {
            val requestAccessDataDirLauncher =
                registerForActivityResult(RequestAccessAppDataDir()) {
                    if (it != null) {
                        contentResolver.takePersistableUriPermission(
                            it,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                }
            MaterialAlertDialogBuilder(this).setTitle(getString(R.string.get_saf_permission))
                .setMessage(getString(R.string.exported_data_folder, folder))
                .setPositiveButton(getString(R.string.get)) { _, _ ->
                    isGetPermission = true
                    requestAccessDataDirLauncher.launch(folder)
                }.setCancelable(false)
                .show()
        } else {
            openActivity<MainPlayerActivity>(
                "uri" to intent.data!!,
                "paths" to intent.data!!.path
            )
            finish()
        }*/
        setContent {
            VideoYouXTheme(hideBar = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }

    @Composable
    fun Screen() {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(70.dp),
                strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 20.dp), text = stringResource(R.string.exported_data)
            )
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (isGetPermission) {
            val folder = intent.data!!.path!!.subStringX("/Android/data/", "/")
            if (canReadDataDir(folder!!)) {
                openActivity<MainPlayerActivity>(
                    "uri" to intent.data!!,
                    "paths" to intent.data!!.path
                )
                finish()
            } else {
                toast("获取权限失败")
                finish()
            }
        }
    }

}

private fun createAppDataDirUri(packageName: String) =
    Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2F${packageName}")!!

fun Context.canReadDataDir(packageName: String): Boolean {
    return DocumentFile.fromTreeUri(applicationContext, createAppDataDirUri(packageName))?.canRead()
        ?: false
}

class RequestAccessAppDataDir : ActivityResultContract<String, Uri?>() {
    override fun createIntent(context: Context, input: String): Intent {


        val dirUri = createAppDataDirUri(input)

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
        val documentFile = DocumentFile.fromTreeUri(context.applicationContext, dirUri)!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentFile.uri)
        }

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent?.data
    }

}
