package com.getmebag.bag.ftx;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.IconTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.CustomObjectPreference;
import com.getmebag.bag.annotations.CurrentUser;
import com.getmebag.bag.annotations.CurrentUserPreference;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.connections.InviteContactsActivity;
import com.getmebag.bag.dialog.DialogActionsListener;
import com.getmebag.bag.dialog.LocationZipCodeDialogFragment;
import com.getmebag.bag.location.MyLocation;
import com.getmebag.bag.model.BagUser;
import com.getmebag.bag.model.UserLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.animation.AnimationUtils.loadAnimation;
import static com.getmebag.bag.location.MyLocation.LocationResult;

public class FTXLocationFragment extends BagAuthBaseFragment implements DialogActionsListener {

    private boolean isLocationFound = false;
    private boolean fetchLocationButtonClicked = false;
    private GoogleMap map;

    @InjectView(R.id.location_main_icon)
    IconTextView locationMainIcon;

    @InjectView(R.id.location_main_description)
    TextView locationMainDescription;

    @InjectView(R.id.location_fetch_automatically_button)
    Button fetchAutomaticallyButton;

    @InjectView(R.id.location_enter_manually_button)
    Button enterManuallyButton;

    @InjectView(R.id.location_mapview_layout)
    View locationMapViewLayout;

    @InjectView(R.id.location_mapview)
    MapView locationMapView;

    @InjectView(R.id.location_mapview_overlay_text)
    TextView locationMapViewOverLayText;

    @Inject
    LocationZipCodeDialogFragment locationZipCodeDialogFragment;

    Geocoder geocoder;

    @Inject
    @CurrentUserPreference
    CustomObjectPreference<BagUser> currentUserPreference;

    @Inject
    @CurrentUser
    BagUser currentUser;

    @Inject
    public FTXLocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ftxlocation, container, false);
        ButterKnife.inject(this, rootView);
        setViewsDefaultVisibility();
        setFetchAutomaticallyButtonListener();
        seEnterManuallyButtonListener();

        geocoder = new Geocoder(getActivity());

        locationMapView.onCreate(savedInstanceState);
        initMaps();

        showNextButtonIfRequired();

        return rootView;
    }

    private void seEnterManuallyButtonListener() {
        enterManuallyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationZipCodeDialogFragment.show(getActivity().getSupportFragmentManager(),
                        getActivity().getString(R.string.dialog_location_zipcode));
                locationZipCodeDialogFragment.dialogActionsListener = FTXLocationFragment.this;
            }
        });
    }

    private void setFetchAutomaticallyButtonListener() {
        fetchAutomaticallyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocationButtonClicked = true;
                fetchLocation();
            }
        });
    }

    private void setViewsDefaultVisibility() {
        locationMainIcon.setVisibility(VISIBLE);
        locationMainDescription.setVisibility(VISIBLE);
        fetchAutomaticallyButton.setVisibility(VISIBLE);
        enterManuallyButton.setVisibility(VISIBLE);

        locationMapViewLayout.setVisibility(GONE);
        locationMapView.setVisibility(GONE);
        locationMapViewOverLayText.setVisibility(GONE);
    }

    private void initMaps() {
        if (map == null) {
            map = locationMapView.getMap();
        }
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNextButtonIfRequired() {
        if (!isThisLoggedInFTX.get()) {
            if (currentUser.getLocation().getZipCode() != null) {
                locationMainDescription.setText(getString(R.string.location_your_current_zip_code,
                        currentUser.getLocation().getZipCode()));
                enterManuallyButton.setText(getString(R.string.update_zipcode_manually_button));
                fetchAutomaticallyButton.setText(getString(R.string.update_location_automatically_button));
            } else {
                locationMainDescription.setText(getString(R.string.location_no_zip_code,
                        currentUser.getLocation().getZipCode()));
            }
        }
    }

    private void fetchLocation() {
        LocationResult locationResult = new LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if (isAdded() && !isLocationFound) {
                    isLocationFound = true;
                    startViewAnimations(location);

                    reviseInVisibility();
                }
            }
        };
        MyLocation myLocation = new MyLocation(getActivity().getSupportFragmentManager());
        myLocation.getLocation(getActivity(), locationResult);
    }

    private void startViewAnimations(Location location) {
        startViewAnimation(locationMainIcon, R.anim.location_to_left, 400, false, null);
        startViewAnimation(locationMainDescription, R.anim.location_to_left, 500, true, location);
    }

    private void startViewAnimation(final View view, int anim, int startOffset,
                                    final Boolean isLast, final Location location) {

        final Address address = getAddress(location);
        final String zipCode = getZipCodeFromAddress(address);

        final Animation rightToLeftAnim = loadAnimation(getActivity(), anim);
        rightToLeftAnim.setStartOffset(startOffset);
        rightToLeftAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isAdded()) {
                    view.setVisibility(GONE);
                }
                if (isLast && location != null) {
                    reviseVisibility();
                    showAddressOnMap(location, address);
                    fetchAutomaticallyButton.setOnClickListener(null);
                    fetchAutomaticallyButton.setText(getActivity()
                            .getString(R.string.location_confirm_zipcode));

                    final AlphaAnimation blinkanimation = new AlphaAnimation(1.0f, 0.4f); // Change alpha from fully visible to invisible
                    blinkanimation.setDuration(300); // duration - half a second
                    blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                    blinkanimation.setRepeatCount(5); // Repeat animation infinitely
                    blinkanimation.setRepeatMode(Animation.REVERSE);

                    fetchAutomaticallyButton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fetchAutomaticallyButton.startAnimation(blinkanimation);
                        }
                    }, 1200);

                    fetchAutomaticallyButton.setOnClickListener(null);

                    //Set next listener if tis FTX
                    fetchAutomaticallyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onConfirmButtonClick(location, zipCode);
                        }
                    });

                } else if (isLast) { //isLast && location == null
                    Toast.makeText(getActivity(), "Some Error.Please try again!", Toast.LENGTH_LONG).show();
                    //Try again if location is null.
                    fetchLocation();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (isAdded() && (view.getVisibility() == VISIBLE)) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    view.startAnimation(rightToLeftAnim);
                }
            });
        }
    }

    private void onConfirmButtonClick(Location location, String zipCode) {
        onConfirmButtonClick(new UserLocation(location.getLatitude(),
                location.getLongitude(), zipCode));
    }

    private void onConfirmButtonClick(String zipcode) {
        Address address = getAddressFromZipCode(zipcode);
        if (address != null) {
            onConfirmButtonClick(new UserLocation(address.getLatitude(),
                    address.getLongitude(), zipcode));
        } else {
            onConfirmButtonClick(new UserLocation(null, null, zipcode));
        }
    }

    private void onConfirmButtonClick(UserLocation userLocation) {
        // 1. Update firebase location stuff
        firebaseUsersRef.child(currentUser.getUniqueId())
                .child(getString(R.string.firebase_user_location_url_part))
                .setValue(userLocation);

        // 2. Create new immutable BagUser object and update data in sharedPreferences.
        addLocationToLocalUserObject(currentUser, userLocation);

        if (isThisLoggedInFTX.get()) {
            startActivity(InviteContactsActivity.intent(getActivity()));
            getActivity().overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        } else {
            getActivity().finish();
            startActivity(FTXLocationActivity.intent(getActivity()));
        }
    }

    private void addLocationToLocalUserObject(BagUser currentUser, UserLocation userLocation) {
        currentUserPreference.set(new BagUser
                .Builder(currentUser)
                .setLocation(userLocation)
                .build());
    }

    private void reviseVisibility() {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    locationMapViewLayout.setVisibility(VISIBLE);
                    locationMapView.setVisibility(VISIBLE);

                }
            });
        }
    }

    private void reviseInVisibility() {
        if (isAdded()) {
            locationMapView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    locationMapViewLayout.setVisibility(INVISIBLE);
                    locationMapView.setVisibility(INVISIBLE);
                }
            }, 950);
        }
    }

    private void showAddressOnMap(Location location, Address address) {
        LatLng latlon = new LatLng(location.getLatitude(),
                location.getLongitude());
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(latlon);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);

        map.moveCamera(center);
        map.animateCamera(zoom);
        map.setMyLocationEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latlon)
                .title("Your current location")
                .draggable(false);

        if (!TextUtils.isEmpty(getZipCodeFromAddress(address))) {
            markerOptions.snippet(getString(R.string.location_zip_code,
                    getZipCodeFromAddress(address)));
        }

        Marker marker = map.addMarker(markerOptions);
        marker.showInfoWindow();

        locationMapViewOverLayText.setVisibility(VISIBLE);

        if (!TextUtils.isEmpty(getZipCodeFromAddress(address))) {
            locationMapViewOverLayText.setText(getString(R.string.location_your_zip_code,
                    getZipCodeFromAddress(address)));
        }
    }

    private Address getAddress(Location location) {
        if (location != null) {
            try {
                Address address = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(),
                        1).get(0);
                return address;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getZipCodeFromAddress(Address address) {
        if (address != null) {
            return address.getPostalCode();
        }
        return null;
    }

    private Address getAddressFromZipCode(String zipCode) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);
            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLocationFound && fetchLocationButtonClicked) {
            fetchLocation();
        }
        initMaps();
        locationMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        locationMapView.onLowMemory();
    }

    @Override
    public void setOnDialogNeutralButtonListener(String arg, Dialog dialog) {

    }

    @Override
    public void setOnDialogPositiveButtonListener(String zipcode) {
        Toast.makeText(getActivity(), zipcode, Toast.LENGTH_LONG).show();
        try {
            geocoder.getFromLocationName(zipcode, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onConfirmButtonClick(zipcode);
    }

    @Override
    public void setOnDialogNegativeButtonListener(Dialog dialog) {
        dialog.dismiss();
    }
}
