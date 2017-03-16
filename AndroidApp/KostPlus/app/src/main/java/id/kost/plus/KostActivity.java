package id.kost.plus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;

import id.kost.plus.adapter.KostAdapter;
import id.kost.plus.setget.Kost;

import static id.kost.plus.DataURL.ROOT_URL;

public class KostActivity extends AppCompatActivity {



    ArrayList<Kost> kostList;

    KostAdapter adapter;

    ImageButton btnTopHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kost);



        kostList = new ArrayList<Kost>();
        new JSONAsyncTask().execute(ROOT_URL+"/kostplus/list_kost.php");

        ListView listview = (ListView)findViewById(R.id.lv_item);
        adapter = new KostAdapter(KostActivity.this, R.layout.lsv_item_kost, kostList);

        listview.setAdapter(adapter);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                // TODO Auto-generated method stub
                String idnya = String.valueOf(kostList.get(position).getId());
                Log.v("IDnya :", idnya);

                Intent k = new Intent(KostActivity.this, DetailKost.class);
                k.putExtra("id", idnya );
                startActivity(k);

            }
        });


        btnTopHome = (ImageButton)findViewById(R.id.btnTopHome);

        btnTopHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(KostActivity.this, MapsPencarian.class);
                startActivity(x);
            }
        });






    }





    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {


        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KostActivity.this);
            dialog.setMessage("Sedang Mengambil Data...");
//            dialog.setTitle("Connecting server");
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

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Kost kost = new Kost();
                        kost.setId(object.getString("id"));
                        kost.setNama(object.getString("nama"));
                        kost.setAlamat(object.getString("alamat"));
                        kost.setHarga(object.getString("harga"));
                        kost.setGambar(object.getString("gambar"));
                        kostList.add(kost);
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
            adapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(KostActivity.this, "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }


}
