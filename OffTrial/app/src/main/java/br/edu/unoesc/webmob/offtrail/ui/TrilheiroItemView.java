package br.edu.unoesc.webmob.offtrail.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.GrupoTrilheiro;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;

@EViewGroup(R.layout.lista_trilheiros)
public class TrilheiroItemView extends LinearLayout {

    @Bean
    DatabaseHelper dh;

    @ViewById
    TextView txtNome;

    @ViewById
    TextView txtMoto;

    @ViewById
    ImageView imvFoto;

    Trilheiro trilheiro;


    public TrilheiroItemView(Context context) {
        super(context);
    }

    @Click(R.id.imvEditar)
    public void editar() {
        // criar uma intent para chmar a tela de cadastro
        // nesta intent pasar o objeto Trilheiro

        Intent itCadastrarTrilheiro = new Intent(getContext(), TrilheiroActivity_.class);
        itCadastrarTrilheiro.putExtra("trilheiro", trilheiro);
        getContext().startActivity(itCadastrarTrilheiro);

        Toast.makeText(getContext(), "Editar: " + trilheiro.getNome(), Toast.LENGTH_LONG).show();
    }

    @Click(R.id.imvExcluir)
    public void excluir() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());

        dialogo.setTitle("Exclusão");
        dialogo.setMessage("Deseja realmente excluir?");
        dialogo.setCancelable(false);
        dialogo.setNegativeButton("Não", null);
        dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    for (GrupoTrilheiro grupoTrilheiro : dh.getGrupoTrilheiroDao().queryForAll()) {
                        if (grupoTrilheiro.getTrilheiro().getCodigo().equals(trilheiro.getCodigo())) {
                            dh.getGrupoTrilheiroDao().delete(grupoTrilheiro);
                        }
                    }

                    dh.getTrilheiroDao().delete(trilheiro);
                    Toast.makeText(getContext(), "Trilheiro excluído com sucesso!",Toast.LENGTH_LONG).show();

                    Intent itlPrincipal = new Intent(getContext(), PrincipalActivity_.class);
                    itlPrincipal.

                    getContext().startActivity(itCadastrarTrilheiro);
                    itCadastrarTrilheiro.

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        dialogo.show();
    }

    public void bind(Trilheiro t) {
        trilheiro = t;
        txtNome.setText(t.getNome());
        txtMoto.setText(t.getMoto().getModelo() + " - " + t.getMoto().getCilindrada());
        imvFoto.setImageBitmap(BitmapFactory.decodeByteArray(t.getFoto(), 0, t.getFoto().length));
    }
}