package com.ahmadrosid.customrequestretrofit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ahmadrosid.customrequestretrofit.api.ApiServices;
import com.ahmadrosid.customrequestretrofit.api.HttpLoggingInterceptor;
import com.ahmadrosid.customrequestretrofit.api.ResponseModel;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar pLoader;
    private LinearLayout content;
    private EditText name1;
    private EditText name2;
    private EditText address1;
    private EditText address2;
    private Button send;
    private String REQUIRED = "Required";

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pLoader = (ProgressBar) findViewById(R.id.pLoader);
        content = (LinearLayout) findViewById(R.id.content);
        name1 = (EditText) findViewById(R.id.name1);
        address1 = (EditText) findViewById(R.id.name2);
        name2 = (EditText) findViewById(R.id.alamat1);
        address2 = (EditText) findViewById(R.id.alamat2);
        send = (Button) findViewById(R.id.send);

        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send) {
            if (validate()) {
                sendDataToServer();
            }
        }
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            content.setVisibility(View.GONE);
            pLoader.setVisibility(View.VISIBLE);
            pLoader.setIndeterminate(true);
        } else {
            pLoader.setIndeterminate(false);
            pLoader.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }
    }

    private void sendDataToServer() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override public void log(String message) {
                Log.d("Intercept_LOG", "log: "+message);
            }
        });
        httpClient.addInterceptor(logging);

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ocit-tutorial.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        ApiServices apiServices = retrofit.create(ApiServices.class);

        MultipartBody.Builder mutipartBuilder = new MultipartBody.Builder();
        mutipartBuilder.setType(MultipartBody.FORM);
        mutipartBuilder.addFormDataPart("title", "Square Logo");
        mutipartBuilder.addFormDataPart("id", "1");
        mutipartBuilder.addFormDataPart("key[0][nama]", name1.getText().toString());
        mutipartBuilder.addFormDataPart("key[0][alamat]", address1.getText().toString());
        mutipartBuilder.addFormDataPart("key[1][nama]", name2.getText().toString());
        mutipartBuilder.addFormDataPart("key[1][alamat]", address2.getText().toString());
        MultipartBody multipartBody = mutipartBuilder.build();

        setLoading(true);
        Disposable subscribe = apiServices.sendData(multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseModel>() {
                    @Override
                    public void accept(ResponseModel responseModel) throws Exception {
                        setLoading(false);
                        alert("Berhasil\n\n"+new Gson().toJson(responseModel));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR_TAG", "accept: ", throwable);
                        setLoading(false);
                        alert("Gagal");
                    }
                });
        disposable.add(subscribe);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    public void alert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    private boolean validate() {
        if (TextUtils.isEmpty(name1.getText().toString())) {
            name1.setError(REQUIRED);
        } else if (TextUtils.isEmpty(address1.getText().toString())) {
            address1.setError(REQUIRED);
        }else if (TextUtils.isEmpty(name2.getText().toString())) {
            name2.setError(REQUIRED);
        } else if (TextUtils.isEmpty(address2.getText().toString())) {
            address2.setError(REQUIRED);
        }else{
            return true;
        }
        return false;
    }
}
