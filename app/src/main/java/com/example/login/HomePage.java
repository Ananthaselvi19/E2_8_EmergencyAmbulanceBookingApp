package com.example.login;

import static com.example.login.R.layout.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.material.appbar.MaterialToolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    View hView;
    BroadcastReceiver br = null;
    Button book;
    View accountlayout;
    ConstraintLayout constraintlayout;
    private GoogleMap map;
    Location currentLocation;
    Location destinationlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng userLatLng, tappedLatLng;
    LatLng end, start;
    private static final int REQUEST_CODE = 101;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";
    private List<Polyline> polylines = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home_page);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        br = new CheckInternet();
        checkInternetService();
        constraintlayout = (ConstraintLayout)findViewById(R.id.layout1);
//        accountlayout = LayoutInflater.from(this).inflate(R.layout.my_account_fragment, TableLayout)

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
//
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        hView = navigationView.getHeaderView(0);
        TextView UserName = (TextView)hView.findViewById(R.id.username);
        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        String strUsername = credentials.getString("Username", null);
        UserName.setText(strUsername);

        book = findViewById(R.id.book);
        book.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                displayAlert();
                return false;
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent1 = new Intent(HomePage.this, Notification.class);
                intent1.putExtra("Accept", true);
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent1 = PendingIntent.getActivity(HomePage.this, 0, intent1, PendingIntent.FLAG_ONE_SHOT);

                Intent intent2 = new Intent(HomePage.this, Notification.class);
                intent2.putExtra("View", false);
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent2 = PendingIntent.getActivity(HomePage.this, 1, intent2, PendingIntent.FLAG_ONE_SHOT);

                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(HomePage.this, getString(R.string.app_name));
                builder.setContentTitle("Driver Request");
                builder.setContentText("Are you sure you want to accept the request?");
                builder.setSmallIcon(R.drawable.ic_notifications);
                builder.setSound(uri);
                builder.setAutoCancel(true);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.addAction(R.drawable.ic_launcher_foreground, "Accept", pendingIntent1);
                builder.addAction(R.drawable.ic_launcher_foreground, "View", pendingIntent2);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1){
                    String channelId = "Your channel_id";
                    NotificationChannel channel = new NotificationChannel(channelId, "channel human readable title", NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                    builder.setChannelId(channelId);
                }
                manager.notify(1, builder.build());
            }
        });

        if(savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyAccount_Fragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(),currentLocation.getLatitude()+""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(HomePage.this);
                }
            }
        });
    }

    @SuppressLint("ResourceType")
    private void displayAlert() {
        final AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = HomePage.this.getLayoutInflater();
        View dialogView = inflater.inflate(book_dialog, null);
        alert.setPositiveButton("OK", null);
        alert.setCancelable(false);
        alert.setInverseBackgroundForced(true);
        alert.setView(dialogView);
        alert.show();
    }

    private void checkInternetService() {
        registerReceiver(br, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.my_account:
//                HomePage.this.finish();
                constraintlayout.setVisibility(View.GONE);
//                Fragment fragment = getSupportFragmentManager().findFragmentById();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_container, new MyAccount_Fragment());
                ft.commitNow();
                Toast.makeText(this,"My Account",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home:
//                constraintlayout.setVisibility(View.VISIBLE);
                Intent intent = new Intent(HomePage.this, HomePage.class);
                startActivity(intent);
                break;
            case R.id.logout:
                Intent intent1 = new Intent(HomePage.this, LoginActivity.class);
                startActivity(intent1);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Geocoder geocoder = new Geocoder(HomePage.this, Locale.getDefault());
        map = googleMap;
        map.setOnMapLongClickListener(this);
        userLatLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

        try {
            List<Address> address = geocoder.getFromLocation(userLatLng.latitude, userLatLng.longitude, 1);
            Address obj = address.get(0);
            String location = obj.getSubLocality();
            makeToast(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
//                map.clear();
        MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("Your Location");
        map.animateCamera(CameraUpdateFactory.newLatLng(userLatLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
        map.addMarker(markerOptions);
    }

    private void makeToast(String location) {
        Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
            }
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng tappedLocation) {
        map.addMarker(new MarkerOptions().position(tappedLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(tappedLocation, 10));
        end = tappedLatLng;
        double tappedLat = tappedLocation.latitude;
        double tappedLong = tappedLocation.longitude;
        Geocoder geocoder = new Geocoder(HomePage.this, Locale.getDefault());
        tappedLatLng = new LatLng(tappedLat,tappedLong);
        try {
            List<Address> address1 = geocoder.getFromLocation(tappedLat, tappedLong, 1);
            Address obj1 = address1.get(0);
            String tappedlocationadd = obj1.getAdminArea();
            makeToastTapped(tappedlocationadd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        start = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Findroutes(start, tappedLatLng);

    }
    public void Findroutes(LatLng Start, LatLng End){
        if (Start == null || End == null) {

            Toast.makeText(HomePage.this, "Unable to get location", Toast.LENGTH_SHORT).show();
        }
        else {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyDlfqcU7tMN_eWrPnyd-FnTKm_mOuTgbUQ")
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar
        .LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(HomePage.this, "Finding Route...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(userLatLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.black));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        map.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        map.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start, tappedLatLng);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start, tappedLatLng);

    }

    private void makeToastTapped(String tappedlocationadd) {
        Toast.makeText(this, tappedlocationadd, Toast.LENGTH_SHORT).show();
    }


//    public boolean onCreateOptionsMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
//
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.homepage, menu);
//        return true;
//    }
//    public boolean onItemsSelected(MenuItem item){
//        Toast.makeText(this,"Selected Item: "+item.getTitle(), Toast.LENGTH_SHORT).show();
//        return super.onOptionsItemSelected(item);
//    }
}