package br.edu.unoesc.webmob.offtrail.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.sql.SQLDataException;
import java.sql.SQLException;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;

@EActivity(R.layout.activity_splash)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class SplashActivity extends AppCompatActivity {
    @AfterViews
    @Background(delay = 3000)
    public void abrirLogin() {
        Intent itLogin = new Intent(
                this,
                LoginActivity_.class
        );
        startActivity(itLogin);
        finish();
    }
}
