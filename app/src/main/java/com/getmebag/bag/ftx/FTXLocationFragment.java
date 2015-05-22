package com.getmebag.bag.ftx;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.getmebag.bag.R;
import com.getmebag.bag.androidspecific.prefs.BooleanPreference;
import com.getmebag.bag.annotations.IsThisLoggedInFirstTimeUse;
import com.getmebag.bag.base.BagAuthBaseFragment;
import com.getmebag.bag.connections.InviteContactsActivity;
import com.getmebag.bag.location.MyLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.getmebag.bag.location.MyLocation.LocationResult;

public class FTXLocationFragment extends BagAuthBaseFragment {

    @InjectView(R.id.textView)
    TextView tv;

    @InjectView(R.id.buttonshadow)
    CircleImageView bs;

    @InjectView(R.id.mapview)
    MapView mapView;

    @InjectView(R.id.ftx_location_next)
    Button nextButton;

    private boolean isLocationFound = false;
    private GoogleMap map;

    @Inject
    public FTXLocationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ftxlocation, container, false);
        ButterKnife.inject(this, rootView);
        setScaleFromCenterAnimation(bs);

        mapView.onCreate(savedInstanceState);
        initMaps();

        showNextButtonIfRequired();

        return rootView;
    }

    private void initMaps() {
        if (map == null) {
            map = mapView.getMap();
        }
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddressOnMap(Location location) {
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
        Marker marker = map.addMarker(new MarkerOptions()
                .position(latlon)
                .title("Your Location")
                .draggable(false));
        marker.showInfoWindow();
    }

    private void showNextButtonIfRequired() {
        if(!isThisLoggedInFTX.get()) {
            nextButton.setVisibility(GONE);
        } else {
            nextButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLocationFound) {
            fetchLocation();
        }
        initMaps();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void fetchLocation() {
        LocationResult locationResult = new LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if (isAdded() && !isLocationFound) {
                    isLocationFound = true;
                    removeScaleFromCenterAnimation(bs, location);
                    showAddressOnMap(location);
                }
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getActivity(), locationResult);
    }

    private void removeScaleFromCenterAnimation(CircleImageView bs, Location location) {
        bs.setAnimation(null);
        bs.setVisibility(GONE);
        Geocoder g = new Geocoder(getActivity());
        try {
            Address a = g.getFromLocation(
                    location.getLatitude(), location.getLongitude(),
                    1).get(0);
            tv.setText("Location Found : " + a.getPostalCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapView.setVisibility(VISIBLE);
    }

    private void setScaleFromCenterAnimation(final View view) {
        final Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_from_center);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_from_center);
                anim.setAnimationListener(this);
                view.startAnimation(anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    @OnClick(R.id.ftx_location_next)
    public void ftxLocationScreenNextButton(View view) {
        startActivity(InviteContactsActivity.intent(getActivity()));
        getActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

}
