package br.edu.unoesc.webmob.offtrail.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    Trilheiro trilheiro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trilheiro);
    }

    @AfterViews
    public void inicializar() {
        try {
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

            trilheiro = (Trilheiro) getIntent().getSerializableExtra("trilheiro");
            if (trilheiro != null) {
                edtNomeTrilheiro.setText(trilheiro.getNome());
                edtIdadeTrilheiro.setText(trilheiro.getIdade().toString());

                for (int i = 0; i < spnMotos.getCount(); i++) {
                    if (spnMotos.getItemAtPosition(i).toString().equals(trilheiro.getMoto().toString())) {
                        spnMotos.setSelection(i);
                    }
                }

                for (GrupoTrilheiro grupo : dh.getGrupoTrilheiroDao().queryForAll()) {
                    if (grupo.getTrilheiro().getCodigo().equals(trilheiro.getCodigo())) {
                        for (int i = 0; i < spnGrupos.getCount(); i++) {
                            if (spnGrupos.getItemAtPosition(i).toString().equals(grupo.getGrupo().toString())) {
                                spnGrupos.setSelection(i);
                            }
                        }
                    }
                }
                byte[] myImage = trilheiro.getFoto();

                Bitmap bmp = BitmapFactory.decodeByteArray(myImage, 0, myImage.length);
                imvFoto.setImageBitmap(bmp);
            }

        } catch (SQLException e) {
            Log.e(Trilheiro.class.getName(), "Erro ao ler dados de Moto ou Grupo");
            e.printStackTrace();
        }
    }

    public void salvar(View v) {
        Trilheiro trilheiro = (Trilheiro) getIntent().getSerializableExtra("trilheiro");
        if (trilheiro == null) {
            inserir();
        } else {
            alterar();
        }
        finish();
    }

    public void inserir() {
        try {
            Trilheiro trilheiro = new Trilheiro();
            trilheiro.setNome(edtNomeTrilheiro.getText().toString());
            trilheiro.setIdade(Integer.parseInt(edtIdadeTrilheiro.getText().toString()));
            trilheiro.setMoto((Moto) spnMotos.getSelectedItem());
            Bitmap bitmap = ((BitmapDrawable) imvFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            trilheiro.setFoto(baos.toByteArray());

            dh.getTrilheiroDao().create(trilheiro);

            GrupoTrilheiro gt = new GrupoTrilheiro();
            gt.setTrilheiro(trilheiro);
            gt.setGrupo((Grupo) spnGrupos.getSelectedItem());
            gt.setDataCadastro(new Date());
            dh.getGrupoTrilheiroDao().create(gt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void alterar() {
        try {
            Trilheiro trilheiro = (Trilheiro) getIntent().getSerializableExtra("trilheiro");
            trilheiro.setNome(edtNomeTrilheiro.getText().toString());
            trilheiro.setIdade(Integer.parseInt(edtIdadeTrilheiro.getText().toString()));
            trilheiro.setMoto((Moto) spnMotos.getSelectedItem());

            Bitmap bitmap = ((BitmapDrawable) imvFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            trilheiro.setFoto(baos.toByteArray());

            dh.getTrilheiroDao().update(trilheiro);

            GrupoTrilheiro gt = new GrupoTrilheiro();
            gt.setTrilheiro(trilheiro);
            gt.setGrupo((Grupo) spnGrupos.getSelectedItem());
            gt.setDataCadastro(new Date());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Trilheiro trilheiro = (Trilheiro) getIntent().getSerializableExtra("fechar");
        if (trilheiro != null) {
            finish();
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
