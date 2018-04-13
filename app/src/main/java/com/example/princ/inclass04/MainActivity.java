package com.example.princ.inclass04;
/*Assignment# - InClass04
  Names : Sujanth Babu Guntupalli
          Mounika Yendluri
*/

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ExecutorService taskPool;
    Button threadButton,asyncButton;
    SeekBar countSB,lengthSB;
    TextView countDTV,lengthDTV,passwordDTV;
    Handler handler;
    ArrayList<String> passwords=new ArrayList<>();
    ProgressDialog pDialog;
    AlertDialog.Builder builder;

    String [] passwordsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("InClass4");

        threadButton=(Button) findViewById(R.id.threadButon);
        asyncButton=(Button) findViewById(R.id.asyncButton);
        countSB=(SeekBar) findViewById(R.id.sbCount);
        lengthSB=(SeekBar) findViewById(R.id.sbSPL);
        countDTV=(TextView) findViewById(R.id.tvdSPC);
        lengthDTV=(TextView) findViewById(R.id.tvdSPL);
        passwordDTV=(TextView) findViewById(R.id.tvdPassword);

        countDTV.setText(String.valueOf(1));
        lengthDTV.setText(String.valueOf(8));

        builder=new AlertDialog.Builder(this);

        threadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwords.clear();
                pDialog=new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Generating Passwords...");
                pDialog.setCancelable(false);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setMax(Integer.parseInt(countDTV.getText().toString()));
                pDialog.setProgress(0);
                pDialog.show();
                taskPool = Executors.newFixedThreadPool(2);
                for(int i=0;i<Integer.parseInt(countDTV.getText().toString());i++) {
                    taskPool.execute(new DoWork(Integer.parseInt(lengthDTV.getText().toString())));
                }
            }
        });

        asyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwords.clear();
                pDialog=new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Generating Passwords...");
                pDialog.setCancelable(false);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setMax(Integer.parseInt(countDTV.getText().toString()));
                pDialog.setProgress(0);
                new MyTask().execute(Integer.parseInt(lengthDTV.getText().toString()));
            }
        });

        countSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                countDTV.setText(String.valueOf(i+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        lengthSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lengthDTV.setText(String.valueOf(i+8));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        handler=new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message message) {
                passwords.add((String) message.obj);
                Log.d("demo", "handleMessage: "+passwords.size());
                pDialog.incrementProgressBy(1);
                if(passwords.size()==Integer.parseInt(countDTV.getText().toString())){
                    pDialog.dismiss();
                    passwordsArray = passwords.toArray(new String[passwords.size()]);
                    builder.setTitle("Passwords").setCancelable(false).setItems(passwordsArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            passwordDTV.setText(passwordsArray[i]);
                        }
                    }).create().show();
                }
                return true;
            }
        });
    }

    class DoWork implements Runnable{

        int pLength;

        public DoWork(int pLength) {
            this.pLength = pLength;
        }

        @Override
        public void run() {
            String password=Util.getPassword(pLength);
            Message message=new Message();
            message.obj=password;
            handler.sendMessage(message);
        }
    }

    class MyTask extends AsyncTask<Integer,Integer,ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            pDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {
            for(int i=0;i<Integer.parseInt(countDTV.getText().toString());i++) {
                passwords.add(Util.getPassword(integers[0]));
                publishProgress(i+1);
                Log.d("demo", "doInBackground: "+passwords.size());
            }
            return passwords;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
                pDialog.dismiss();
                final String [] passwordsArray = passwords.toArray(new String[passwords.size()]);
                builder.setTitle("Passwords").setCancelable(false).setItems(passwordsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        passwordDTV.setText(passwordsArray[i]);
                    }
                }).create().show();
        }


    }
}
