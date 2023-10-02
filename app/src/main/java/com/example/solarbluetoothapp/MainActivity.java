package com.example.solarbluetoothapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button buttonOn, buttonOff, buttonPaired;
    BluetoothAdapter myBluetoothAdapter;
    ListView listView;
    Intent btEnablingIntent;

    int requestCodeForeEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOn = (Button) findViewById(R.id.bluetoothOn);
        buttonOff = (Button) findViewById(R.id.bluetoothOff);
        buttonPaired = (Button) findViewById(R.id.bluetoothPaired);

        listView = (ListView) findViewById(R.id.listView);


        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForeEnable = 1;
        bluetoothOnMethod();
        bluetoothOffMethod();
        exeButton();
    }

    private void exeButton() {
        buttonPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                int index = 0;

                if(bt.size()>0)
                {
                    for (BluetoothDevice device :bt)
                    {
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });
    }

    private void bluetoothOffMethod() {
        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myBluetoothAdapter.isEnabled()) {
                    // Use the context from the outer class or activity
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    myBluetoothAdapter.disable();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==requestCodeForeEnable)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), "Bluetooth Enabling Cancelled.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void bluetoothOnMethod() {
        buttonOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myBluetoothAdapter==null)
                    {
                        Toast.makeText(getApplicationContext(), "Bluetooth does not support on this Device", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(!myBluetoothAdapter.isEnabled())
                        {
                            startActivityForResult(btEnablingIntent,requestCodeForeEnable);
                        }
                    }
                }
            }


        );
    }

}