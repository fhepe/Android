package br.edu.unoesc.webmob.olaandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class OlaAndroidActivity extends AppCompatActivity {

    private final String TAG = "OlaAndroid TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ola_android);
        Log.d(TAG, "Passando pelo onCreate!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Passando pelo onStart!");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "Passando pelo onRestart!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Passando pelo onResume!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Passando pelo onPause!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Passando pelo onStop!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Passando pelo onStop!");
    }

    // Método para exibir a mensagem
    public void exibirMensagem(View v) {
        Toast.makeText(this, "Teste de mensagem", Toast.LENGTH_LONG).show();
    }

    // Método para abrir um segunda tela
    public void abrirTela(View v){
        Intent itSegunda = new Intent(
                this,
                SegundaActivity.class
        );

        startActivity(itSegunda);
    }
}
