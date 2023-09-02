package id.gdev.regist.ui.screen.scanner

import android.content.Context
import com.google.android.gms.common.api.OptionalModuleApi
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallClient
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

fun checkModule(
    context: Context, module: OptionalModuleApi,
    onCheck: (isSuccess: Boolean, message: String) -> Unit
) {
    val moduleInstallClient = ModuleInstall.getClient(context)
    moduleInstallClient
        .areModulesAvailable(module)
        .addOnSuccessListener {
            if (it.areModulesAvailable()) {
                onCheck(true, "Module are available")
            } else {
                installModule(moduleInstallClient, module, onCheck)
            }
        }
        .addOnFailureListener {
            onCheck(false, "Failed to checked module")
        }
}

fun installModule(
    moduleInstallClient: ModuleInstallClient,
    module: OptionalModuleApi,
    onInstall: (isSuccess: Boolean, message: String) -> Unit
) {
    val moduleInstallRequest =
        ModuleInstallRequest.newBuilder()
            .addApi(module)
            .build()
    moduleInstallClient
        .installModules(moduleInstallRequest)
        .addOnSuccessListener {
            if (it.areModulesAlreadyInstalled()) {
                onInstall(true, "Module installed successfully")
            }
        }
        .addOnFailureListener {
            onInstall(false, "Failed to install module")
        }
}

fun getScanner(context: Context): GmsBarcodeScanner = GmsBarcodeScanning.getClient(
    context, GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_CODABAR,
            Barcode.FORMAT_PDF417,
            Barcode.TYPE_ISBN
        )
        .build()
)