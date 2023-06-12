package com.tovalue.me.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.tovalue.me.R

class PermissionHelper {

    public fun isGrantedStoragePermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
    }

    public fun isGrantedCameraPermissions(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    /**
     * Check if Camera Permission needs to be requested to the user
     *
     * @param context of the App
     *
     * @return True if [Manifest.permission.CAMERA] is present on the App Manifest and user didn't grant it,
     * False in another case
     */
    public fun isNeededToRequestForCameraPermissions(context: Context): Boolean =
        isPermissionContainedOnManifest(context, Manifest.permission.CAMERA) && !isGrantedCameraPermissions(context)

    public fun checkStoragePermissions(
        view: View,
        onPermissionDenied: () -> Unit = { },
        onPermissionGranted: () -> Unit,
    ) {
        checkPermissions(
            view,
            view.context.getString(R.string.storage_access_permission),
            view.context.getString(R.string.storage_msg),
            view.context.getString(R.string.storage_enable_permission),
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            onPermissionDenied,
            onPermissionGranted
        )
    }

    public fun checkWriteStoragePermissions(
        view: View,
        onPermissionDenied: () -> Unit = { },
        onPermissionGranted: () -> Unit,
    ) {
        checkPermissions(
            view,
            view.context.getString(R.string.storage_access_permission),
            view.context.getString(R.string.storage_msg),
            view.context.getString(R.string.storage_enable_permission),
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            onPermissionDenied,
            onPermissionGranted
        )
    }
    
    
    public fun isGrantedLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    public fun checkLocationPermission(
        view: View,
        onPermissionDenied: () -> Unit = { },
        onPermissionGranted: () -> Unit,
    ) {
        checkPermissions(
            view,
            view.context.getString(R.string.location_access_permission),
            view.context.getString(R.string.location_msg),
            view.context.getString(R.string.location_enable_permission),
            listOf(Manifest.permission.ACCESS_FINE_LOCATION),
            onPermissionDenied,
            onPermissionGranted
        )
    }

    /**
     * Check if the [permission] was declared on the App Manifest
     *
     * @param context of the current Application
     * @param permission name to be checked
     *
     * @return True if the permission is present on the App Manifest
     */
    private fun isPermissionContainedOnManifest(context: Context, permission: String): Boolean =
        context.packageManager
            .getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            .requestedPermissions
            .contains(permission)

    @Suppress("LongParameterList")
    private fun checkPermissions(
        view: View,
        dialogTitle: String,
        dialogMessage: String,
        snackbarMessage: String,
        permissions: List<String>,
        onPermissionDenied: () -> Unit,
        onPermissionGranted: () -> Unit,
    ) {

        val permissionsListener = object : BaseMultiplePermissionsListener() {

            override fun onPermissionsChecked(mumultiplePermissionsReport: MultiplePermissionsReport) {
                if (mumultiplePermissionsReport.areAllPermissionsGranted()) {
                    onPermissionGranted()
                } else {
                    if (mumultiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        snackbarPermissionsListener(view, snackbarMessage).onPermissionsChecked(
                            mumultiplePermissionsReport
                        )
                    } else {
                        dialogPermissionsListener(
                            view.context,
                            dialogTitle,
                            dialogMessage
                        ).onPermissionsChecked(mumultiplePermissionsReport)
                    }
                    onPermissionDenied()
                }
            }
        }

        Dexter.withContext(view.context)
            .withPermissions(permissions)
            .withListener(permissionsListener)
            .check()
    }

    private fun snackbarPermissionsListener(
        view: View,
        snackbarMessage: String,
    ): SnackbarOnAnyDeniedMultiplePermissionsListener =
        SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
            .with(view, snackbarMessage)
            .withOpenSettingsButton(R.string.setting_title)
            .build()

    private fun dialogPermissionsListener(
        context: Context,
        dialogTitle: String,
        dialogMessage: String,
    ): DialogOnAnyDeniedMultiplePermissionsListener =
        DialogOnAnyDeniedMultiplePermissionsListener.Builder
            .withContext(context)
            .withTitle(dialogTitle)
            .withMessage(dialogMessage)
            .withButtonText(android.R.string.ok)
            .build()
}
