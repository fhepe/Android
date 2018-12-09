package br.edu.unoesc.webmob.offtrail.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;

@EActivity(R.layout.activity_preferencias)
public class PreferenciasActivity extends AppCompatActivity {
    @ViewById
    Spinner spnCores;

    @Pref
    Configuracao_ configuracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
    }


    @AfterViews
    public void inicializar() {
        List<String> cores = new ArrayList<String>();
        cores.add("Vermelho");
        cores.add("Azul");

        ArrayAdapter<String> adapterCores = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, cores);
        spnCores.setAdapter(adapterCores);
    }

    public void Salvar(View v) {

        switch (spnCores.getSelectedItem().toString()) {
            case "Vermelho":
                configuracao.edit().cor().put(Color.RED).apply();
                Toast.makeText(this, "Cor alterada para vermelho", Toast.LENGTH_LONG).show();
                break;
            case "Azul":
                configuracao.edit().cor().put(Color.BLUE).apply();
                Toast.makeText(this, "Cor alterada para azul", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        Intent i = new Intent();
        i.putExtra("cor", configuracao.cor().get());
        setResult(1, i);
    }

    public void cancelar(View v) {
        finish();
    }
}