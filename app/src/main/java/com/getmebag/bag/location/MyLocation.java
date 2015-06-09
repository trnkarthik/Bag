package com.getmebag.bag.location;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;

import com.getmebag.bag.R;
import com.getmebag.bag.dialog.CommonAlertDialogFragment;
import com.getmebag.bag.dialog.DialogActionsListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by karthiktangirala on 4/13/15.
 */

public class MyLocation {
    private final FragmentManager supportFragmentManager;
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    public ProgressDialog dialog;

    public MyLocation(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    public boolean getLocation(Context context, LocationResult result) {
        dialog = ProgressDialog.show(context, null, context.getString(R.string.fetching_current_zipcode));

        showDialogIfNotShowing();

        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled) {
            dialog.dismiss();
            showEnableLocationDialog(context);
            return false;
        }

        if (gps_enabled)
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        if (network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 6000);
        return true;
    }

    private void showDialogIfNotShowing() {
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    private void showEnableLocationDialog(final Context context) {
        CommonAlertDialogFragment enableAlertDialogFragment = new CommonAlertDialogFragment.Builder()
                .setTitle(context.getString(R.string.location_services_not_active))
                .setMessage(context.getString(R.string.please_enable_location_services))
                .setNeutralButtonText(context.getString(R.string.dialog_ok))
                .setCanceledOnTouchOutside(false)
                .build();
        enableAlertDialogFragment.dialogActionsListener = new DialogActionsListener() {
            @Override
            public void setOnDialogNeutralButtonListener(String arg, Dialog dialog) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }

            @Override
            public void setOnDialogPositiveButtonListener(String arg) {

            }

            @Override
            public void setOnDialogNegativeButtonListener(Dialog dialog) {

            }
        };

        enableAlertDialogFragment.show(supportFragmentManager,
                context.getString(R.string.dialog_common_alert));

    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            updateLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            updateLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null;
            if (gps_enabled)
                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_enabled)
                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime()) {
                    updateLocation(gps_loc);
                } else {
                    updateLocation(net_loc);
                }
                return;
            }

            if (gps_loc != null) {
                updateLocation(gps_loc);
                return;
            }

            if (net_loc != null) {
                updateLocation(net_loc);
                return;
            }

            updateLocation(null);
            lm = null;
        }
    }

    private void updateLocation(Location net_loc) {
        dialog.dismiss();
        locationResult.gotLocation(net_loc);
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
}