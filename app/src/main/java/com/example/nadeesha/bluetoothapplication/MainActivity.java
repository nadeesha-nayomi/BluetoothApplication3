package com.example.nadeesha.bluetoothapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.app.VoiceInteractor.*;

public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PAIRED_DEVICE = 2;

    Button btnListPairedDevices;
    TextView stateBluetooth;
    BluetoothAdapter bluetoothAdapter;

    private Button button;
    private EditText time;
    private  TextView finalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListPairedDevices = (Button)findViewById(R.id.btnlistpaireddevices);
        stateBluetooth = (TextView)findViewById(R.id.tvbluetoothstate);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        CheckBlueTothState();

        btnListPairedDevices.setOnClickListener(btnListPairedDevicesOnClickListener);

        time = (EditText)findViewById(R.id.in_time);
        button = (Button)findViewById(R.id.btn_run);
        finalResult = (TextView)findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = time.getText().toString();
                runner.execute(sleepTime);
            }
        });
    }

    private  class  AsyncTaskRunner extends  AsyncTask<String, String, String>{
        private  String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "ProgressDialog", "Wait for " + time.getText().toString() + " seconds");

        }

        @Override
        protected void onProgressUpdate(String... text) {
            //super.onProgressUpdate(values);
            finalResult.setText(text[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);
            progressDialog.dismiss();
            finalResult.setText(result);
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping...");
            try{
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e){
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e){
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }
    }

    private void CheckBlueTothState() {
        if (bluetoothAdapter == null){
            stateBluetooth.setText("Bluetooth Not Supported.");
        }
        else {
            if (bluetoothAdapter.isEnabled()){
                if (bluetoothAdapter.isDiscovering()){
                    stateBluetooth.setText("Bluetooth is currently in device discovery process.");
                }
                else {
                    stateBluetooth.setText("Bluetooth is Enabled.");
                    btnListPairedDevices.setEnabled(true);
                }
            }
            else {
                stateBluetooth.setText("Bluetooth is not enabled!");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private  Button.OnClickListener btnListPairedDevicesOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ListPairedDevicesActivity.class);
            startActivityForResult(intent, REQUEST_PAIRED_DEVICE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT){
            CheckBlueTothState();
        } if (requestCode == REQUEST_PAIRED_DEVICE){
            if (resultCode == RESULT_OK){

            }
        }
    }

    private  void TestUpload(){
        int i = 1 + 1;
    }

}
