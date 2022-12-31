package com.example.qrcodescanner_21c2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //view object
    private Button buttonscanning;
    private TextView textViewname, textViewclass, textViewId;
    private IntentIntegrator qrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //view object
        buttonscanning = (Button) findViewById(R.id.buttonScan);
        textViewname = (TextView) findViewById(R.id.textViewNama);
        textViewclass = (TextView) findViewById(R.id.textViewKelas);
        textViewId = (TextView) findViewById(R.id.textViewNim);

        qrScan = new IntentIntegrator(this);
        buttonscanning.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestcode, resultcode, data);
        if (result != null) {
            //jika qr code tidak ada sama sekali
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil Scanning tidak  ada ", Toast.LENGTH_LONG).show();
            }else if(result.getContents().contains("tel:")){
                //Mendapat data kode telpon
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(result.getContents()));
                startActivity(intent);

            } else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    textViewname.setText(obj.getString("nama"));
                    textViewclass.setText(obj.getString("kelas"));
                    textViewId.setText(obj.getString("nim"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        }else{
            super.onActivityResult(requestcode, resultcode, data);
        }
    }
    @Override
    public void onClick(View v) {
        qrScan.initiateScan();

    }
}