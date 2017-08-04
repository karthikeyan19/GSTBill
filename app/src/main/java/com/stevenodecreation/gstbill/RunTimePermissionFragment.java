package com.stevenodecreation.gstbill;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

public abstract class RunTimePermissionFragment extends BaseFragment {

    protected abstract void onPermissionsGranted(int requestCode);

    protected boolean hasPermission(String[] permissions) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(getActivity(), permission);
        }
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestAppPermissions(String[] permissions, int requestCode) {
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int result : grantResults) {
            permissionCheck += result;
        }
        if (grantResults.length > 0 && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        }
    }
}
