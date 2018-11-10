package br.edu.unoesc.webmob.offtrail.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.Date;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.Grupo;
import br.edu.unoesc.webmob.offtrail.model.GrupoTrilheiro;
import br.edu.unoesc.webmob.offtrail.model.Moto;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;


@EActivity(R.layout.activity_trilheiro)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class TrilheiroActivity extends AppCompatActivity {

    @ViewById
    ImageView imvFoto;
    @ViewById
    EditText edtNomeTrilheiro;
    @ViewById
    EditText edtIdadeTrilheiro;
    @ViewById
    Spinner spnMotos;
    @ViewById
    Spinner spnGrupos;
    @Bean
    DatabaseHelper dh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trilheiro);
    }

    @AfterViews
    public void inicializar() {
        // cria o adapter
        ArrayAdapter<Moto> motos = null;
        try {
            motos = new ArrayAdapter<Moto>(this,
                    android.R.layout.simple_spinner_item,
                    dh.getMotoDao().queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // vincula o adaptar ao spinner
        spnMotos.setAdapter(motos);

        // cria o adapter
        ArrayAdapter<Grupo> grupos = null;
        try {
            grupos = new ArrayAdapter<Grupo>(this,
                    android.R.layout.simple_spinner_item,
                    dh.getGrupoDao().queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // vincula o adaptar ao spinner
        spnGrupos.setAdapter(grupos);
    }

    public void salvar(View v) {
        try {
            Trilheiro t = new Trilheiro();
            t.setNome(edtNomeTrilheiro.getText().toString());
            t.setIdade(Integer.parseInt(edtIdadeTrilheiro.getText().toString()));
            t.setMoto((Moto) spnMotos.getSelectedItem());
            Bitmap bitmap = ((BitmapDrawable) imvFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            t.setFoto(baos.toByteArray());

            dh.getTrilheiroDao().create(t);

            GrupoTrilheiro gt = new GrupoTrilheiro();
            gt.setTrilheiro(t);
            gt.setGrupo((Grupo) spnGrupos.getSelectedItem());
            gt.setDataCadastro(new Date());
            dh.getGrupoTrilheiroDao().create(gt);

            finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelar(View v) {
        finish();
    }

    @LongClick(R.id.imvFoto)
    public void capturarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 100);
        }
    }

    @OnActivityResult(100)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imvFoto.setImageBitmap(imageBitmap);
        }
    }
}
