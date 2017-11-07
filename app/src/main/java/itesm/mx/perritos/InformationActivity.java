package itesm.mx.perritos;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import java.util.Locale;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton jorgeMail;
    ImageButton jorgeLinkedin;
    ImageButton santiagoMail;
    ImageButton santiagoLinkedin;
    ImageButton gerardoMail;
    ImageButton gerardoLinkedin;
    ImageButton alexWeb;
    ImageButton alexLinkedin;
    ImageButton marthaMail;
    ImageButton marthaLinkedin;
    ImageButton culturaGitHub;
    ImageButton culturaPlayStore;
    ImageButton itesmLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton jorgeMail = (ImageButton) findViewById(R.id.jorge_mail);
        ImageButton jorgeLinkedin =  (ImageButton) findViewById(R.id.jorge_linkedin);
        ImageButton santiagoMail = (ImageButton) findViewById(R.id.santiago_mail);
        ImageButton santiagoLinkedin = (ImageButton) findViewById(R.id.santiago_linkedin);

        ImageButton gerardoMail = (ImageButton) findViewById(R.id.gera_mail);
        ImageButton gerardoLinkedin = (ImageButton) findViewById(R.id.gera_linkedin);

        ImageButton alexWeb = (ImageButton) findViewById(R.id.alex_web);
        ImageButton alexLinkedin = (ImageButton) findViewById(R.id.alex_linkedin);

        ImageButton marthaMail = (ImageButton) findViewById(R.id.martha_mail);
        ImageButton marthaLinkedin = (ImageButton) findViewById(R.id.martha_linkedin);

        ImageButton culturaGitHub = (ImageButton) findViewById(R.id.cultura_git);
        ImageButton culturaPlayStore = (ImageButton) findViewById(R.id.cultura_playstore);

        ImageButton itesmLocation = (ImageButton) findViewById(R.id.itesm_location);


        jorgeMail.setOnClickListener(this);
        jorgeLinkedin.setOnClickListener(this);

        santiagoMail.setOnClickListener(this);
        santiagoLinkedin.setOnClickListener(this);

        gerardoMail.setOnClickListener(this);
        gerardoLinkedin.setOnClickListener(this);

        alexWeb.setOnClickListener(this);
        alexLinkedin.setOnClickListener(this);

        marthaMail.setOnClickListener(this);
        marthaLinkedin.setOnClickListener(this);

        culturaGitHub.setOnClickListener(this);
        culturaPlayStore.setOnClickListener(this);

        itesmLocation.setOnClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jorge_mail:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","A01196160@itesm.mx", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Cultura Perrona Información");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Texto ...");
                startActivity(Intent.createChooser(emailIntent, "Enviando mail..."));
                break;
            case  R.id.jorge_linkedin:
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/jorge-armando-v%C3%A1zquez-ortiz-a05261a9/"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.santiago_mail:
                Intent emailIntent2 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","santiago_st_95@hotmail.com", null));
                emailIntent2.putExtra(Intent.EXTRA_SUBJECT, "Cultura Perrona Información");
                emailIntent2.putExtra(Intent.EXTRA_TEXT, "Texto ...");
                startActivity(Intent.createChooser(emailIntent2, "Enviando mail..."));
                break;
            case  R.id.santiago_linkedin:
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/santiago-sandoval-6986ba94/"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.gera_mail:
                Intent emailIntent3 = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","gerasuarezm27@gmail.com", null));
                emailIntent3.putExtra(Intent.EXTRA_SUBJECT, "Cultura Perrona Información");
                emailIntent3.putExtra(Intent.EXTRA_TEXT, "Texto ...");
                startActivity(Intent.createChooser(emailIntent3, "Enviando mail..."));
                break;
            case  R.id.gera_linkedin:
                Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.alex_web:
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://alextrujillo4.com"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case  R.id.alex_linkedin:
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/alexandro-trujillo-garcia-400369a5/"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.martha_mail:
                Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case  R.id.martha_linkedin:
                Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                break;
            case R.id.cultura_git:
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jvazquez96/Perritos-app"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case  R.id.cultura_playstore:
                Snackbar.make(v, "No es posible realizar esta acción", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.itesm_location:
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 25.651187, -100.289626);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                this.startActivity(intent);
                break;

        }
    }
}
