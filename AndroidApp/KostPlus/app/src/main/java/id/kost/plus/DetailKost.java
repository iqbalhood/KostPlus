package id.kost.plus;

import android.app.ProgressDialog;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
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

import static id.kost.plus.DataURL.ROOT_URL;

public class DetailKost extends AppCompatActivity implements OnMapReadyCallback {

    String nama         = "";
    String alamat       = "";
    String harga        = "";
    String telepon      = "";
    String gambar       = "";
    String latitude     = "";
    String logitude     = "";
    String keterangan   = "";



    private GoogleMap mGoogleMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kost);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new JSONAsyncTask().execute(ROOT_URL+"/kostplus/detail_kost.php?id_kost=1");
    }


    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }


    public void initMap() {
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(DetailKost.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            if (googleMap != null) {
               // Double lati = Double.parseDouble("3.5807956");
               // Double logi = Double.parseDouble("98.6556153");
                Double lati = Double.parseDouble(latitude);
                Double logi = Double.parseDouble(logitude);
                LatLng Medan = new LatLng(lati, logi);
                MarkerOptions marker = new MarkerOptions().position(new LatLng(lati, logi)).title(nama);
                mGoogleMap = googleMap;
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Medan,15));
                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                googleMap.addMarker(marker);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "GOOGLE MAPS NOT LOADED");
        }
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DetailKost.this);
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
                    JSONArray jarray = jsono.getJSONArray("kost");

                    System.out.println("DETAIL JSON " + data);


                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        nama            = object.getString("nama");
                        alamat          = object.getString("alamat");
                        harga           = object.getString("harga");
                        telepon         = object.getString("telepon");
                        gambar          = object.getString("gambar");
                        latitude        = object.getString("lat");
                        logitude        = object.getString("log");
                        keterangan      = object.getString("keterangan");


                        System.out.println("LATITUDE" + object.getString("lat"));





                    }
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

            TextView   txtNama = (TextView)findViewById(R.id.txtNamaKost);
            TextView   txtAlamat = (TextView)findViewById(R.id.txtAlamatKost);
            TextView   txtHarga = (TextView)findViewById(R.id.txtHargaKost);
            TextView   txtKeterangan = (TextView)findViewById(R.id.txtKeteranganKost);
            ImageView  imgKost = (ImageView)findViewById(R.id.imgDetailKost);



            txtNama.setText(nama);
            txtAlamat.setText(alamat);
            txtHarga.setText(harga);
            txtKeterangan.setText(keterangan);


            Glide.with(DetailKost.this).load(gambar).into(imgKost);

            if(result == false)
                Toast.makeText(DetailKost.this, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }




}
