package com.chate.connect

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val manager: SplitInstallManager by lazy {
        SplitInstallManagerFactory.create(this)
    }

    private val CHOCOLATE_FEATURE = "chocolatefeature"
    private val STRAWBERRY_FEATURE = "strawberryfeature"
    private var mySessionID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        val installListener = SplitInstallStateUpdatedListener {
            if (it.sessionId() == mySessionID) {
                when (it.status()) {
                    REQUIRES_USER_CONFIRMATION -> showStatus("REQUIRES_USER_CONFIRMATION")
                    DOWNLOADING -> showStatus("DOWNLOADING")
                    INSTALLING -> showStatus("INSTALLING")
                    DOWNLOADED -> showStatus("DOWNLOADED")
                    INSTALLED -> showStatus("INSTALLED")
                    CANCELED -> showStatus("CANCELED")
                    PENDING -> showStatus("PENDING")
                    FAILED -> showStatus("FAILED")
                    CANCELING -> showStatus("CANCELING")
                    UNKNOWN -> showStatus("UNKNOWN")
                }
            }
        }

        btnFeature1.setOnClickListener {
            val request = SplitInstallRequest.newBuilder()
                .addModule(CHOCOLATE_FEATURE)
                .build()

            manager.registerListener(installListener)
            manager.startInstall(request)
                .addOnSuccessListener {
                    showStatus("Chocolate install success")
                    Toast.makeText(this, "Session ID: $it", Toast.LENGTH_SHORT).show()
                    mySessionID = it
                }
                .addOnFailureListener {
                    showStatus("Chocolate install failure")
                    Toast.makeText(this, "Exception $it", Toast.LENGTH_SHORT).show()
                }

        }

        btnOpenFeature1.setOnClickListener {
            if (manager.installedModules.contains(CHOCOLATE_FEATURE)) {
                val i = Intent()
                i.setClassName(
                    BuildConfig.APPLICATION_ID,
                    "com.chate.chocolatefeature.ChocolateActivity"
                )
                startActivity(i)
            } else {
                showStatus("Not installed")
            }
        }

        btnFeature2.setOnClickListener {
            val request = SplitInstallRequest.newBuilder()
                .addModule(STRAWBERRY_FEATURE)
                .build()

            manager.registerListener(installListener)
            manager.startInstall(request)
                .addOnSuccessListener {
                    showStatus("Strawberry install success")
                    Toast.makeText(this, "Session ID: $it", Toast.LENGTH_SHORT).show()
                    mySessionID = it
                }
                .addOnFailureListener {
                    showStatus("Strawberry install failure")
                    Toast.makeText(this, "Exception $it", Toast.LENGTH_SHORT).show()
                }

        }

        btnOpenFeature2.setOnClickListener {
            if (manager.installedModules.contains(STRAWBERRY_FEATURE)) {
                val i = Intent()
                i.setClassName(
                    BuildConfig.APPLICATION_ID,
                    "com.chate.strawberryfeature.StrawberryActivity"
                )
                startActivity(i)
            } else {
                showStatus("Not installed")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showStatus(s: String) {
        runOnUiThread {
            tvStatus.text = "Status: $s"
        }
    }
}