package com.hustunique.vlive

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ar.core.ArCoreApk
import com.hustunique.resonance_audio.AudioRender
import com.hustunique.vlive.agora.AgoraActivity
import com.hustunique.vlive.databinding.ActivityMainBinding
import com.hustunique.vlive.ui.SceneActivity
import com.hustunique.vlive.util.Utils
import com.hustunique.vlive.util.startActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (UserInfoManager.uid.isEmpty()) {
//            startActivity<LoginActivity>()
//            finish()
//        }

        val t = AudioRender
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            getRuntimePermissions()
            Log.i(TAG, "onCreate: Request permission")
        }

        binding.btnMainEntry.setOnClickListener {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "No permission, no work!", Toast.LENGTH_SHORT).show()
            } else {
                Utils.init(this)
                startActivity<CameraXActivity>()
            }
        }

        binding.btnModelEntry.setOnClickListener {
            startActivity<ModelActivity>()
        }

        binding.btnAgoraEntry.setOnClickListener {
//            if (!allPermissionsGranted()) {
//                Toast.makeText(this, "No permission, no work!", Toast.LENGTH_SHORT).show()
//            } else {
            startActivity<AgoraActivity>()
//            }
        }

        binding.btnArCoreEntry.setOnClickListener {
            testArCoreAvailability()
        }
        binding.btnSceneEntry.setOnClickListener {
            startActivity<SceneActivity>()
        }
    }

    private fun testArCoreAvailability() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)
        if (availability.isTransient) {
            Toast.makeText(this, "Checking...", Toast.LENGTH_SHORT).show()
        } else if (!availability.isSupported) {
            Toast.makeText(this, "Not support", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "AR core support ok", Toast.LENGTH_SHORT).show()
//            startActivity<ARCoreActivity>()
        }
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = ArrayList<String>()
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    allNeededPermissions.add(permission)
                }
            }
        }

        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
            )
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRequiredPermissions(): Array<String?> {
        return try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val PERMISSION_REQUESTS = 1
    }
}