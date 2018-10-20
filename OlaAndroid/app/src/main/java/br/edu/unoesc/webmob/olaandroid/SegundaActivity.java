package br.edu.unoesc.webmob.olaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SegundaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
    }

    public void fechar(View v){
        finish();
    }
}
