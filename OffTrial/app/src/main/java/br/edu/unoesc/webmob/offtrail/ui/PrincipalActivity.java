package br.edu.unoesc.webmob.offtrail.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import java.sql.SQLException;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.adapter.TrilheiroAdapter;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.Cidade;
import br.edu.unoesc.webmob.offtrail.model.Usuario;
import br.edu.unoesc.webmob.offtrail.rest.CidadeClient;
import br.edu.unoesc.webmob.offtrail.rest.Endereco;

@EActivity(R.layout.activity_principal)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @ViewById
    ListView lstTrilheiros;
    @Bean
    TrilheiroAdapter trilheiroAdapter;
    @Bean
    DatabaseHelper dh;
    @Pref
    Configuracao_ configuracao;
    @RestService
    CidadeClient cidadeClient;

    ProgressDialog pd;

    @AfterViews
    public void inicializar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itCadastrarTrilheiro = new Intent(PrincipalActivity.this, TrilheiroActivity_.class);
                startActivity(itCadastrarTrilheiro);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // recuperar dados do usuario
        Usuario u = (Usuario) getIntent().getSerializableExtra("usuario");
        Toast.makeText(this, "Seja bem-vindo " + u.getEmail() + "!", Toast.LENGTH_LONG).show();

        View v = toolbar.getRootView();
        v.setBackgroundColor(configuracao.cor().get());
        Toast.makeText(this, configuracao.parametro().get(), Toast.LENGTH_LONG).show();

        configuracao.edit().cor().put(Color.BLUE).apply();

        lstTrilheiros.setAdapter(trilheiroAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizaListaTrilheiros();
    }

    public void atualizaListaTrilheiros() {
        TrilheiroAdapter trilheiro = (TrilheiroAdapter) lstTrilheiros.getAdapter();
        trilheiro.ordenarLista();
        trilheiro.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
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
            Intent itSobre = new Intent(this, SobreActivity_.class);
            startActivity(itSobre);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sincronizar) {
            pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setTitle("Aguarde, consultando..");
            pd.setIndeterminate(true);
            pd.show();
            consultarCidadePorNome();
        } else if (id == R.id.nav_preferencias) {
            // TODO: (0,50) Implementar tela pra salvar preferências
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @UiThread
    public void mostrarResultado(String resultado) {
        pd.dismiss();
        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
    }

    @Background(delay = 2000)
    public void consultarCidadePorNome() {
        List<Endereco> listaEndereco = cidadeClient.getEndereco("São");

        try {
            dh.getCidadeDao().delete(dh.getCidadeDao().queryForAll());
        } catch (SQLException E) {
            E.printStackTrace();
        }

        for (Endereco endereco : listaEndereco) {
            Cidade cidade = new Cidade();
            cidade.setNome(endereco.toString());

            try {
                dh.getCidadeDao().create(cidade);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (listaEndereco != null && listaEndereco.size() > 0) {
            mostrarResultado("Sincronizado " + listaEndereco.size() + " cidades.");
            pd.dismiss();
        }
    }
}
