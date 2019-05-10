package com.rukiasoft.androidapps.cocinaconroll.permission


import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rukiasoft.androidapps.cocinaconroll.R
import com.rukiasoft.androidapps.cocinaconroll.resources.ResourcesManager
import com.rukiasoft.androidapps.cocinaconroll.utils.ViewUtils
import java.lang.ref.WeakReference
import javax.inject.Inject

class PermissionManager @Inject constructor(
    private val viewUtils: ViewUtils,
    private val resourcesManager: ResourcesManager
) {


    private var requestPermission = false
    private var needToShowDialogs = false

    fun requestPermissions(
        activity: Activity,
        callbackAllPermissionsGranted: ((m: Unit?) -> Unit?)? = null,
        permissions: List<String>,
        messageRationale: String?,
        code: Int,
        showRationaleMessageIfNeeded: Boolean = false
    ) {
        requestPermissionsInternal(activity, callbackAllPermissionsGranted, permissions, messageRationale, code, showRationaleMessageIfNeeded)
    }

    fun requestPermissions(
        fragment: Fragment,
        callbackAllPermissionsGranted: ((m: Unit?) -> Unit?)? = null,
        permissions: List<String>,
        messageRationale: String?,
        code: Int,
        showRationaleMessageIfNeeded: Boolean = false
    ) {
        requestPermissionsInternal(fragment, callbackAllPermissionsGranted, permissions, messageRationale, code, showRationaleMessageIfNeeded)
    }

    private fun requestPermissionsInternal(
        caller: Any,
        callbackAllPermissionsGranted: ((m: Unit?) -> Unit?)? = null,
        permissions: List<String>,
        messageRationale: String? = null,
        code: Int,
        showRationaleMessageIfNeeded: Boolean
    ) {
        requestPermission = false
        needToShowDialogs = false

        permissions.forEach { permission ->
            if (isPermissionGranted(caller, permission).not()) {
                requestPermission = true
                // Should we show an explanation?
                needToShowDialogs = shouldShowRequestPermissionRationale(caller, permission) and showRationaleMessageIfNeeded
            }
        }
        when {
            needToShowDialogs -> getActivityFromCaller(caller)?.let { activity ->
                viewUtils.showAlertDialog(
                    activity = WeakReference(activity),
                    allowCancelWhenTouchingOutside = false,
                    title = resourcesManager.getString(R.string.permissions_title),
                    message = messageRationale,
                    positiveButton = resourcesManager.getString(R.string.accept),
                    callbackPositive = { askForPermission(caller, permissions, code) }
                )
            }
            requestPermission -> askForPermission(caller, permissions, code)
            else -> callbackAllPermissionsGranted?.invoke(null)
        }

    }

    fun isPermissionGranted(caller: Any, permission: String): Boolean {
        return when (caller) {
            is Activity -> ContextCompat.checkSelfPermission(caller, permission) == PackageManager.PERMISSION_GRANTED
            is Fragment -> {
                caller.context?.let {
                    ContextCompat.checkSelfPermission(it, permission) == PackageManager.PERMISSION_GRANTED
                } ?: false
            }
            else -> false
        }

    }

    private fun getActivityFromCaller(caller: Any): Activity? {
        return when (caller) {
            is Activity -> caller
            is Fragment -> caller.activity
            else -> null
        }
    }

    private fun shouldShowRequestPermissionRationale(caller: Any, permission: String): Boolean {
        var needToShowDialogs = false
        when (caller) {
            is Activity -> {
                if (ActivityCompat.shouldShowRequestPermissionRationale(caller, permission)) {
                    needToShowDialogs = true
                }
            }
            is Fragment -> {
                if (caller.shouldShowRequestPermissionRationale(permission)) {
                    needToShowDialogs = true
                }
            }
        }
        return needToShowDialogs
    }

    private fun askForPermission(caller: Any, permissions: List<String>, code: Int) {

        when (caller) {
            is Activity -> ActivityCompat.requestPermissions(caller, permissions.toTypedArray(), code)
            is Fragment -> caller.requestPermissions(permissions.toTypedArray(), code)
        }
    }

    fun arePermissionsGrantedByTheUser(grantResults: IntArray): Boolean {
        return grantResults.contains(PackageManager.PERMISSION_DENIED).not()
    }
}