package ru.nutscoon.mapsproject.Screens;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.search.DiscoveryLink;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.nutscoon.mapsproject.Models.OrganizationsOnMapData;
import ru.nutscoon.mapsproject.R;
import ru.nutscoon.mapsproject.ViewModels.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };


    private Map map = null;
    GeoCoordinate _currentGeoCoordinate;
    DiscoveryResultPage mResultPage = null;
    MapCircle currentLocationMapCicle;
    static  final  double Radius=1000;
    DiscoveryRequest request;

    private SupportMapFragment mapFragment = null;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();

        initialize();
    }


    private SupportMapFragment getSupportMapFragment() {
        return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.g_map);
    }

    int openWindow=0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1111){
            openWindow = 0;
        }
    }

    private void initialize() {
        mapFragment = getSupportMapFragment();
       final MapGesture.OnGestureListener listener =
                new MapGesture.OnGestureListener.OnGestureListenerAdapter() {
                    @Override
                    public boolean onMapObjectsSelected(List<ViewObject> objects) {
                        for (ViewObject viewObj : objects) {
                            if (viewObj.getBaseType() == ViewObject.Type.USER_OBJECT) {
                                if (((MapObject) viewObj).getType() == MapObject.Type.MARKER) {
                                    // At this point we have the originally added
                                    // map marker, so we can do something with it
                                    // (like change the visibility, or more
                                    // marker-specific actions)

                                    MapObject object = (MapObject) viewObj;
                                    MapMarker mapMarker = (MapMarker) object;
                                    GeoCoordinate curMarker = mapMarker.getCoordinate();
                                    if (curMarker == null)
                                        return false;

                                    for (DiscoveryResult item : _currentItems) {
                                        if (curMarker.getLatitude() == ((PlaceLink) item).getPosition().getLatitude() &&
                                                curMarker.getLongitude() == ((PlaceLink) item).getPosition().getLongitude()
                                        ) {
                                           if(openWindow!=0)
                                               return false;
                                            openWindow++;
                                            PlaceLink placeLink=(PlaceLink) item;

                                           // ShowToast(item.getTitle());
                                            final Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                                            //intent.putExtra("orgAddress", ((PlaceLink) item).getAverageRating());
                                            intent.putExtra("orgName", item.getTitle());
                                            intent.putExtra("url", item.getIconUrl());
                                            intent.putExtra("latitude", ((PlaceLink) item).getPosition().getLatitude());
                                            intent.putExtra("longitude", ((PlaceLink) item).getPosition().getLongitude());
                                            startActivityForResult(intent, 1111);
                                            //startActivity(intent);
                                            return  false;
                                        }
                                    }

                                }

                            }

                        }
                        openWindow=0;
                        // return false to allow the map to handle this callback also
                        return false;
                    }
                };
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    map = mapFragment.getMap().setZoomLevel(14);
                    //fake coordinates
                    _currentGeoCoordinate =new GeoCoordinate(54.173678, 37.591930, 0.0);
                    map.setCenter(_currentGeoCoordinate, Map.Animation.LINEAR);


                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.point_green);
                    Image image = new Image();
                    image.setBitmap(largeIcon);
                    MapMarker marker = new MapMarker(_currentGeoCoordinate, image);
                    map.addMapObject(marker);
                     PositioningManager posManager = PositioningManager.getInstance();
                    posManager.start(PositioningManager.LocationMethod.GPS_NETWORK);
                    posManager.start(PositioningManager.LocationMethod.GPS);

                    if(posManager.getPosition() != null) {
                        _currentGeoCoordinate = posManager.getPosition().getCoordinate();


                        map.addMapObject(marker);
                    }

                    map.setCenter(_currentGeoCoordinate, Map.Animation.LINEAR);
                    map.setZoomLevel(16);



                    //
                    Request();
                   mapFragment.getMapGesture().addOnGestureListener(listener);
                   mapFragment.getMapGesture().addOnGestureListener(new MyOnGestureListener());

                } else {
                    Log.e(LOG_TAG, "Cannot initialize SupportMapFragment (" + error + ")");
                }

            }
        });
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
             //   initialize();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
        mainViewModel.ready();
    }

    private  void ShowToast(String info)
    {
        Toast toast = Toast.makeText(getApplicationContext(),
                info, Toast.LENGTH_SHORT);
        toast.show();

    }
    private void setupViewModel(){
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getOrganizationsLiveData().observe(this, new Observer<List<OrganizationsOnMapData>>() {
            @Override
            public void onChanged(@Nullable List<OrganizationsOnMapData> organizationsOnMapData) {
                if(organizationsOnMapData != null){
                    //addOrganizationsOnMap(organizationsOnMapData);
                }
            }
        });
    }

    private  boolean IsInnerPoint(double lat1,double  lon1)
    {
        return  IsInnerPoint( lat1, lat1, _currentGeoCoordinate.getLatitude(), _currentGeoCoordinate.getLongitude());
    }


    private boolean IsInnerPoint(double lat1,double  lon1,double lat2,double lon2 )
    {
         double r = Haversin.mesure(lat1,lon2,lat2,lon2);
        if(r+100<Radius)
            return true;
        return false;

    }

    private void Request()
    {
        try {
            request = new SearchRequest("restaurant").setSearchCenter(_currentGeoCoordinate);

            request.setCollectionSize(100);

            ErrorCode error = request.execute(new SearchRequestListener());
            if (error != ErrorCode.NONE) {
                // Handle request error

            }

        }
        catch (IllegalArgumentException ex) {

        }

    }

    List<DiscoveryResult> _currentItems= new ArrayList<>();
    List<MapMarker> _currentMapMarkers;

    private  int countRequest=2;

    class SearchRequestListener implements ResultListener<DiscoveryResultPage> {


        @Override
        public void onCompleted(DiscoveryResultPage data, ErrorCode error) {
            if (error != ErrorCode.NONE) {
                // Handle error

            } else {
                // Process result data
                mResultPage = data;
                 _currentItems.addAll(data.getItems());
                _currentMapMarkers= new ArrayList<>();
                for (DiscoveryResult item : _currentItems) {

                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                        PlaceLink placeLink = (PlaceLink) item;

                        GeoCoordinate tempCoordinate = placeLink.getPosition();
                        MapMarker defaultMarker = new MapMarker();
                        defaultMarker.setCoordinate(tempCoordinate);
                        defaultMarker.setTitle(item.getTitle());
                        defaultMarker.showInfoBubble();
                        _currentMapMarkers.add(defaultMarker);
                        map.addMapObject(defaultMarker);

                    } else if (item.getResultType() == DiscoveryResult.ResultType.DISCOVERY) {
                        DiscoveryLink discoveryLink = (DiscoveryLink) item;


                    }

                    countRequest--;
                    if(countRequest>0)
                    {
                         DiscoveryRequest nextPageRequest = mResultPage.getNextPageRequest();
                         if(nextPageRequest!=null)
                          nextPageRequest.execute(new SearchRequestListener());

                    }
                }


            }
        }
    }

    private class MyOnGestureListener implements MapGesture.OnGestureListener {

        @Override
        public void onPanStart() {
        }

        @Override
        public void onPanEnd() {
        }

        @Override
        public void onMultiFingerManipulationStart() {
        }

        @Override
        public void onMultiFingerManipulationEnd() {
        }

        @Override
        public boolean onMapObjectsSelected(List<ViewObject> objects) {

            return false;
        }

        @Override
        public boolean onTapEvent(PointF p) {
            GeoCoordinate c = map.pixelToGeo(p);
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(PointF p) {
            return false;
        }

        @Override
        public void onPinchLocked() {
        }

        int i=0;

        @Override
        public boolean onPinchZoomEvent(float scaleFactor, PointF p)
        {

        /*  if(scaleFactor>1) {

              if(i==0)
              {
                  DiscoveryRequest nextPageRequest = mResultPage.getNextPageRequest();
                   if (nextPageRequest != null) {
                    ErrorCode error = nextPageRequest.execute(new SearchRequestListener());
                       if (error != ErrorCode.NONE) {
                           // Handle request error
                       }
                       i=0;
                   }
              }
              i=i+1;
          }
          else
          {
            i=0;
          }*/
          return false;
        }

        @Override
        public void onRotateLocked() {
        }

        @Override
        public boolean onRotateEvent(float rotateAngle) {
            return false;
        }

        @Override
        public boolean onTiltEvent(float angle) {
            return false;
        }

        @Override
        public boolean onLongPressEvent(PointF p) {
            return false;
        }

        @Override
        public void onLongPressRelease() {
        }

        @Override
        public boolean onTwoFingerTapEvent(PointF p) {
            return false;
        }
    }


}
