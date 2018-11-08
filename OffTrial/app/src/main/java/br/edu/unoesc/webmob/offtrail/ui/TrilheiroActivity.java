package br.edu.unoesc.webmob.offtrail.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import br.edu.unoesc.webmob.offtrail.R;


@EActivity(R.layout.activity_trilheiro)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class TrilheiroActivity extends AppCompatActivity {

    @ViewById
    EditText edtNomeTrilheiro;
    @ViewById
    EditText edtIdadeTrilheiro;
    @ViewById
    Spinner spnMoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trilheiro);
    }

    public void salvar(View v){
        Toast.makeText(this, "Salvando dados...", Toast.LENGTH_SHORT).show();
    }

    public void cancelar(View v){
        edtNomeTrilheiro.setText("");
        edtIdadeTrilheiro.setText("");
        spnMoto.setSelection(0);
        edtNomeTrilheiro.requestFocus();
    }
}
