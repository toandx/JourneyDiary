package svmc.toandx.journeydiary.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import svmc.toandx.journeydiary.R;
import svmc.toandx.journeydiary.object.Event;

public class AddEventActivity extends AppCompatActivity {
    private FloatingActionButton btnAdd;
    private ImageButton btnPickImage,btnPickLocation,btnFavor,btnRestau,btnHotel;
    private TextView addressTextView;
    private ImageView imageView;
    private EditText editText;
    private String address;
    private SimpleDateFormat sdf;
    private Intent intent;
    private String action;
    private int event_id;
    private final int PICK_IMAGE_REQUEST=1;
    private final int READ_FILE_PERMISSION_REQ=2;
    private final int LOCALTION_PERMISSION_REQ=3;
    private final int ADD_EVENT_REQ=-1;
    private Boolean hasLocation,favorite;
    private double latitude,longitude;
    private String img_uri,name,time;
    private Boolean readFilePermission;
    private Uri gmmIntentUri;
    private Event newEvent;
    private FusedLocationProviderClient mFusedLocationClient;
    private void init_data()
    {
        readFilePermission=false;
        intent=getIntent();
        action=intent.getAction();
        if (action.equals("edit"))
        {
            event_id=intent.getIntExtra("event_id",0);
            name=MainActivity.value.get(event_id).name;
            time=MainActivity.value.get(event_id).time;
            img_uri=MainActivity.value.get(event_id).image_uri;
            hasLocation=MainActivity.value.get(event_id).location;
            favorite=MainActivity.value.get(event_id).favorite;
            if (hasLocation) {
                latitude = MainActivity.value.get(event_id).latitude;
                longitude = MainActivity.value.get(event_id).longitude;
                address = MainActivity.value.get(event_id).address;
                addressTextView.setText(address);
            }
            editText.setText(name);
            if (img_uri.length()!=0)
            {
                Glide.with(AddEventActivity.this).load(Uri.parse(img_uri)).into(imageView);
            }
            if (!favorite)
            {
                btnFavor.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                btnFavor.setColorFilter(Color.argb(255,3,169,244));
            } else
            {
                btnFavor.setImageResource(R.drawable.ic_favorite_black_24dp);
                btnFavor.setColorFilter(Color.argb(255,255,0,0));
            }
        } else
        {
            img_uri="";
            favorite=false;
            hasLocation=false;
            addressTextView.setText("");
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        btnPickImage=findViewById(R.id.add_activ_image_btn);
        btnPickLocation=findViewById(R.id.add_activ_loc_btn);
        btnFavor=findViewById(R.id.add_activ_favor_btn);
        btnAdd=findViewById(R.id.add_activ_add_btn);
        btnRestau=findViewById(R.id.add_activ_restau_btn);
        btnHotel=findViewById(R.id.add_activ_hotel_btn);
        editText=findViewById(R.id.add_activ_edit_text);
        imageView=findViewById(R.id.add_activ_image_view);
        addressTextView=findViewById(R.id.add_activ_address_tv);
        sdf=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        init_data();
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestReadFilePermission();
                if (readFilePermission) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=editText.getText().toString();
                time=sdf.format(Calendar.getInstance().getTime());
                if (hasLocation)
                {
                    newEvent=new Event(name,time,img_uri,favorite,latitude,longitude,address);
                }
                else
                    newEvent=new Event(name,time,img_uri,favorite);
                if (action.equals("edit"))
                    MainActivity.value.set(event_id,newEvent);
                else
                    MainActivity.value.add(newEvent);
                setResult(RESULT_OK);
                img_uri="";
                finish();
            }
        });
        btnPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLocationPermissions())
                {
                    if (isLocationEnabled())
                    {
                        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                                new OnCompleteListener<Location>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Location> task) {
                                        Location location = task.getResult();
                                        if (location == null) {
                                            requestNewLocationData();
                                        } else {
                                            hasLocation=true;
                                            btnHotel.setVisibility(View.VISIBLE);
                                            btnRestau.setVisibility(View.VISIBLE);
                                            latitude=location.getLatitude();
                                            longitude=location.getLongitude();
                                            address=getTextAdrress(latitude,longitude);
                                            addressTextView.setText(address);
                                            Log.d("toandz",address);
                                        }
                                    }
                                }
                        );
                    } else
                    {
                        Toast.makeText(AddEventActivity.this, "Turn on location and network to use this function", Toast.LENGTH_LONG).show();
                        intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                } else requestLocationPermissions();
            }
        });
        btnFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favorite)
                {
                    favorite=false;
                    btnFavor.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    btnFavor.setColorFilter(Color.argb(255,3,169,244));
                } else
                {
                    favorite=true;
                    btnFavor.setImageResource(R.drawable.ic_favorite_black_24dp);
                    btnFavor.setColorFilter(Color.argb(255,255,0,0));
                }
            }
        });
        btnRestau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmmIntentUri = Uri.parse("geo:"+Double.toString(latitude)+","+Double.toString(longitude)+"?q=restaurants");
                intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        btnHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmmIntentUri = Uri.parse("geo:"+Double.toString(latitude)+","+Double.toString(longitude)+"?q=hotels");
                intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (img_uri.length()!=0)
                {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
                }
            }
        });
    }
    private boolean checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    private void requestReadFilePermission()
    {
        if (ContextCompat.checkSelfPermission(AddEventActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            readFilePermission=false;
            Log.d("toandz","request permission");
            ActivityCompat.requestPermissions(AddEventActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_FILE_PERMISSION_REQ);
        } else
        {
            readFilePermission = true;
        }
    }
    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                LOCALTION_PERMISSION_REQ
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER));
    }
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }
    private String getTextAdrress(double latitude,double longitude) {
        String result = "";
        Geocoder geocoder = new Geocoder(AddEventActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Log.d("toandz",Integer.toString(addresses.get(0).getMaxAddressLineIndex()));
                for(int i=0;i<=addresses.get(0).getMaxAddressLineIndex();i++) {
                    result = result + addresses.get(0).getAddressLine(i) + " ";
                    Log.d("toandz",addresses.get(0).getAddressLine(i));
                }
            }}
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return(result);
    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            hasLocation=true;
            btnHotel.setVisibility(View.VISIBLE);
            btnRestau.setVisibility(View.VISIBLE);
            latitude=mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();
            address=getTextAdrress(latitude,longitude);
            addressTextView.setText(address);
            Log.d("toandz",address);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null)
        {
            Uri selectedImage = data.getData();
            img_uri=selectedImage.toString();
            Glide.with(AddEventActivity.this).load(selectedImage).into(imageView);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode==READ_FILE_PERMISSION_REQ) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFilePermission=true;
            }

        }
    }
}
