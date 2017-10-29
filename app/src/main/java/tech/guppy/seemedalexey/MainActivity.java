package tech.guppy.seemedalexey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final int RETRY_TIME = 1000;
    public static final int COLUMNS_COUNT = 2;

    /**
     * Action that is used to notify MainActivity that the list should be updated
     */
    public static final String ACTION_UPDATE_LIST = "ACTION_UPDATE_LIST";

    public static Gson gson = new GsonBuilder().create();
    private SerialsAdapter serialsAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMNS_COUNT, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        serialsAdapter = new SerialsAdapter(this);

        recyclerView.setAdapter(serialsAdapter);

        IntentFilter intentFilter = new IntentFilter(ACTION_UPDATE_LIST);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateList();
            }
        }, intentFilter);

        updateList();


    }

    /**
     * Shows the progressbar and updates the list of serials
     * Send local broadcast with action MainActivity.ACTION_UPDATE_LIST to call this method from
     * other parts of the application
     */
    private void updateList() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("https://see.medalexey.ru/index.json")
                        .build();
                SerialsListResponse serialsListResponse = null;
                while (serialsListResponse == null) {
                    try {
                        Response response = okHttpClient.newCall(request).execute();
                        serialsListResponse = gson.fromJson(response.body().string(), SerialsListResponse.class);
                    } catch (IOException | NullPointerException exception) {
                        exception.printStackTrace();
                        try {
                            Thread.sleep(RETRY_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                final SerialsListResponse finalSerialsListResponse = serialsListResponse;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finalSerialsListResponse.getSerials().add(0, null);
                        serialsAdapter.setSerials(finalSerialsListResponse.getSerials());
                        serialsAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setHasFixedSize(true);
                    }
                });
            }
        }).start();
    }
}
