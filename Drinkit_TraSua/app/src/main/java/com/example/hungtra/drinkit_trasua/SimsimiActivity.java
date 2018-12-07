package com.example.hungtra.drinkit_trasua;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.hungtra.drinkit_trasua.Adapter.SimsimiAdapter;
import com.example.hungtra.drinkit_trasua.Helper.HttpDataHandler;
import com.example.hungtra.drinkit_trasua.Model.ChatModel;
import com.example.hungtra.drinkit_trasua.Model.SimsimiModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SimsimiActivity extends AppCompatActivity {

    ListView simsimiListView;
    EditText simsimiEditText;
    List<ChatModel> list_chat = new ArrayList<>();
    ImageView btn_send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simsimi);

        simsimiListView = (ListView)findViewById(R.id.list_of_message);
        simsimiEditText = (EditText)findViewById(R.id.user_message);
        btn_send_message = (ImageView) findViewById(R.id.fabSimsimi);

        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = simsimiEditText.getText().toString();
                ChatModel model = new ChatModel(text,true); //nguoi dung gui Message
                list_chat.add(model);

                new SimsimiAPI().execute(list_chat);

                //nguoi dung xoa mesage
                simsimiEditText.setText("");
            }
        });
    }

    private class SimsimiAPI extends AsyncTask<List<ChatModel>,Void,String> {
        String stream = null;
        List<ChatModel> models;
        String text = simsimiEditText.getText().toString();
        @Override
        protected String doInBackground(List<ChatModel>... lists) {
            String url = String.format("http://sandbox.api.simsimi.com/request.p?key=%s&lc=en&ft=1.0&text=%s",getString(R.string.simsimi_api),text);
            models = lists[0];
            HttpDataHandler httpDataHandler = new HttpDataHandler();
            stream = httpDataHandler.GetHTTPData(url);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            SimsimiModel response = gson.fromJson(s,SimsimiModel.class);

            ChatModel chatModel = new ChatModel(response.getResponse(),false);//get response từ Símimi
            models.add(chatModel);
            SimsimiAdapter adapter = new SimsimiAdapter(models,getApplicationContext());
            simsimiListView.setAdapter(adapter);
        }
    }
}
