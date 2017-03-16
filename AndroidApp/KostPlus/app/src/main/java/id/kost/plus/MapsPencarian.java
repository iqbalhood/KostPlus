package id.kost.plus;

/**
 * Created by iqbalhood on 13/01/16.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import id.kost.plus.setget.Kost;

import static id.kost.plus.DataURL.ROOT_URL;


public class MapsPencarian extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{

    ArrayList<Kost> eventList;
    private static final int REQUEST_CODE_PERMISSION = 2;

    //List of type books this list will store type Book which is our data model
    private List<Kost> mapEvents;

    private GoogleMap mGoogleMap;


    Toolbar toolbar;

    Double latGlobal = 3.567208 ;
    Double logGlobal = 98.6556153 ;

    int pos = 1;

    Marker previousMarker = null;



    String URLUSE = ROOT_URL+"/kostplus/list_kost_map.php";



    AutoCompleteTextView av1;
    ArrayList<String> pid  = new ArrayList<String>();
    ArrayList<String> nama  = new ArrayList<String>();
    ArrayList<String> gambar = new ArrayList<String>();
    ArrayList<String> tanggal = new ArrayList<String>();
    ArrayList<String> hashtag = new ArrayList<String>();

    ImageButton btnPrev;
    ImageButton btnNext;


    String Penanggalan = "";


    //MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    Marker medan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_pencarian);
        setTheme(android.R.style.Theme_Holo);



        LatLng Medan = new LatLng(3.567208, 98.654804);




       // getMarker();


        eventList = new ArrayList<Kost>();
        new JSONAsyncTask().execute(URLUSE);




        RelativeLayout social_lowbar = (RelativeLayout) findViewById(R.id.social_lowbar);
        social_lowbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapsPencarian.this, DetailKost.class);
                startActivity(intent);

            }
        });


    }





    public void initMap() {
        GetKordinat();
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(MapsPencarian.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mGoogleMap = googleMap;

        System.out.println("Dieksekusi");
        try {
            if (googleMap != null) {

                System.out.println("Dieksekusi 2");

                LatLng Medan = new LatLng(latGlobal, logGlobal);

                System.out.println("SIZE LIST BBBB " +  eventList.size());

                //String array to store all the book names
                String[] items = new String[eventList.size()];

                //Traversing through the whole list to get all the names
                for(int i=0; i<eventList.size(); i++){
                    //Storing names to string array
                    items[i] = eventList.get(i).getNama();
                    String idMarker = String.valueOf(eventList.get(i).getId());
                    String namaMarker = String.valueOf(eventList.get(i).getNama());


                    pid.add(idMarker);
                   /// System.out.println(" DATA ID "+  String.valueOf(eventList.get(i).getId()) );
                    nama.add(namaMarker);
                    System.out.println("Nama Event " + eventList.get(i).getNama() );
                    gambar.add(eventList.get(i).getGambar());
                    tanggal.add(eventList.get(i).getAlamat());
                    hashtag.add(eventList.get(i).getHarga());
                    double lat = Double.parseDouble(eventList.get(i).getLatitude());
                    double log = Double.parseDouble(eventList.get(i).getLogitude());

                    System.out.println("Latitude BBB :"+lat);
                    System.out.println("Logitude :"+log);



                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, log))
                            .title(idMarker)
                            .snippet(namaMarker)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .alpha(0.7f)

                    );
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Medan,13));
                    System.out.println("Sudah di eksekusi");
                }

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Medan,13));


            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "GOOGLE MAPS NOT LOADED");
        }

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {


            @Override
            public boolean onMarkerClick(Marker marker) {
                RelativeLayout social_lowbar = (RelativeLayout) findViewById(R.id.social_lowbar);
                TextView  namaEvent        = (TextView)findViewById(R.id.namaEvent);
                TextView  txtIDID          = (TextView)findViewById(R.id.txtIDID);
                ImageView imgEvent         = (ImageView) findViewById(R.id.imgEvent);
                TextView tvTanggal         = (TextView)findViewById(R.id.tvTanggal);
                TextView tvHashtag         = (TextView)findViewById(R.id.tvHashtag);

                social_lowbar.setVisibility(View.VISIBLE);

                String i = (marker.getId()).substring(1);
                pos = Integer.parseInt(i);
                int id = Integer.parseInt(i);
                String eventID = pid.get(id);
                String namal   = nama.get(id);
                String gambar1 = gambar.get(id);
                String tanggall = tanggal.get(id);
                String hashtagl = hashtag.get(id);
                txtIDID.setText(eventID);
                namaEvent.setText(namal);
                Glide.with(MapsPencarian.this).load(gambar1).into(imgEvent);
                tvTanggal.setText(tanggall);
                tvHashtag.setText(hashtagl);


                if(previousMarker!=null){
                    previousMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                previousMarker=marker;

                return true;
            }

        });
    }


    //jika back button di klik
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        finish();
        return true;
    }


    private void GetKordinat() {


        latGlobal = 3.5644315;
        logGlobal = 98.6539081;


    }

    @Override
    public void onClick(View view) {

    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MapsPencarian.this);
            dialog.setMessage("Sedang Mengambil Data...");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);

                    System.out.println(" KOST " + data);



                    JSONArray jarray = jsono.getJSONArray("kost");


                   // bts = Integer.parseInt(batas);

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Kost kost = new Kost();
                        kost.setId(object.getString("id"));
                        kost.setNama(object.getString("nama"));
                        kost.setGambar(object.getString("gambar"));
                        kost.setAlamat(object.getString("alamat"));
                        kost.setHarga(object.getString("harga"));
                        kost.setLatitude(object.getString("latitude"));
                        kost.setLogitude(object.getString("logitude"));

                        eventList.add(kost);

                        System.out.println("Koordinat Map" + object.getString("latitude"));


                    }

                    //mapEvents = eventList;
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            initMap();



           /// adapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(MapsPencarian.this, "Data Kosong", Toast.LENGTH_LONG).show();

        }
    }






}
