package com.example.stevenexcellentproject;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
    Button connectButton;
    EditText IPAddressField, portField;
    public Socket onlySocket;
    public PrintWriter out;
    public BufferedReader in;


    public  TextView tv;


    private Runnable receivedWriter()//receives Strings from server, and passes them into public void execute(String command)
    {
        return new Runnable() {
            public void run() {
                try{
                    String received;
                    while(true)
                    {
                        received = in.readLine();
                        execute(received);
                    }
                }catch(Exception e){System.out.println("Lost connection to the server :: ");e.printStackTrace();System.exit(1);};


            }};
    }
    //for receiving strings from server.
    public void execute(String command)
    {



    }
    private void tryConnectionThing(String IP, String port)
    {
        onlySocket = null;
        try {
            Intent i = new Intent(this, SecondaryActivity.class);
            tv = (TextView)findViewById(R.id.theFeedTho);
            startActivity(i);

            onlySocket = new Socket();
            onlySocket.connect(new InetSocketAddress(IP,Integer.parseInt(port)), 3500);
            out = new PrintWriter(onlySocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(onlySocket.getInputStream()));
            new Thread(receivedWriter()).start();

            //test... send string to other guy
            out.println("hello world tho");






        }catch(Exception e)
        {
            e.printStackTrace();


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        connectButton = (Button)findViewById(R.id.ConnectButton);
        IPAddressField = (EditText)findViewById(R.id.IPAddressField);
        portField = (EditText)findViewById(R.id.PortField);

        IPAddressField.setHint("Enter IP Address");
        portField.setHint("Enter integer port number");

        connectButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String a = IPAddressField.getText().toString();
                        String b = portField.getText().toString();
                        String IP = a + ":" + b;
                        System.out.println("Clicked on the button tho");
                        IPAddressField.setText("");
                        portField.setText("");


                        //attempt to connect to the socket
                        tryConnectionThing(a,b);

                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
