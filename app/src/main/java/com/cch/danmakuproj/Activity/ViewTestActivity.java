package com.cch.danmakuproj.Activity;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cch.danmakuproj.AndroidUtils.L;
import com.cch.danmakuproj.R;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewTestActivity extends ActionBarActivity {

    private View view_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        view_card = findViewById(R.id.view_card);
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        view_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetAsyncTask netAsyncTask1 = new NetAsyncTask(1);
                netAsyncTask1.executeOnExecutor(executorService);

                NetAsyncTask netAsyncTask2 = new NetAsyncTask(2);
                netAsyncTask2.executeOnExecutor(executorService);

                NetAsyncTask netAsyncTask3 = new NetAsyncTask(3);
                netAsyncTask3.executeOnExecutor(executorService);

                NetAsyncTask netAsyncTask4 = new NetAsyncTask(4);
                netAsyncTask4.executeOnExecutor(executorService);

                NetAsyncTask netAsyncTask5 = new NetAsyncTask(5);
                netAsyncTask5.executeOnExecutor(executorService);

                NetAsyncTask netAsyncTask6 = new NetAsyncTask(6);
                netAsyncTask6.executeOnExecutor(executorService);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_test, menu);
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

    class NetAsyncTask extends AsyncTask<Void,Void,Void>{

        private String tag;

        public NetAsyncTask(int id){
            this.tag = "异步任务"+id;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0 ; i < 100000 ; i++){
                L.e(tag+"执行到了"+i);
            }
            return null;
        }
    }
}
