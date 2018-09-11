package ort.proyecto_final.mvdmart.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

import ort.proyecto_final.mvdmart.R;
import ort.proyecto_final.mvdmart.config.Config;
import ort.proyecto_final.mvdmart.helpers.StringWithTag;
import ort.proyecto_final.mvdmart.server_calls.ComenzarIdentificacionPartidaServerCall;
import ort.proyecto_final.mvdmart.server_calls.TraerTodasLasPartidasPendientesServerCall;
import ort.proyecto_final.mvdmart.server_calls.TraerTodosLosFrigorificosServerCall;

public class SelectAreaActivity extends ActivityMadre {

    private Button btnLogout, btnRegistroSeparacion, btnIdentificacionBolsa, btnRegistroMaterias;
    private TextView txtOperario;
    private int idPartidaSelecionada = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        spinnerLoader = findViewById(R.id.spinner_loader);
        new TraerTodosLosFrigorificosServerCall(this);
        inicializarVistas();
    }

    public void inicializarVistas() {
        txtOperario = findViewById(R.id.txtOperario);
        txtOperario.setText(Config.getNombreOperario(SelectAreaActivity.this) + " (" + Config.getNumeroOperario(SelectAreaActivity.this) + ")");
        txtOperario.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36);

        btnLogout = findViewById(R.id.btn_sa_Logout);
        btnLogout.setOnClickListener(this);
        btnRegistroMaterias = findViewById(R.id.btn_sa_RegistroMaterias);
        btnRegistroMaterias.setOnClickListener(this);
        btnIdentificacionBolsa = findViewById(R.id.btn_sa_IdentificacionBolsa);
        btnIdentificacionBolsa.setOnClickListener(this);
        btnRegistroSeparacion = findViewById(R.id.btn_sa_RegistroSeparacion);
        btnRegistroSeparacion.setOnClickListener(this);
    }

    //TODO en caso de reusar el sialogo con single chice, refactoring del metodo para poder rutilizarlo
    public void alertSelectPartida() {
        List<StringWithTag> partidas = null;
        try {
            JSONArray obj = new JSONArray(Config.getPartidasPendientes(this));
            partidas = StringWithTag.convertJSONArrayToAarrayPartidasPendientes(obj);
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + Config.getPartidasPendientes(this) + "\"");
        }
        final ListAdapter adaptador = new ArrayAdapter<StringWithTag>(this, android.R.layout.select_dialog_singlechoice, partidas);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SelectAreaActivity.this);
        mBuilder.setTitle("Seleccione una partida");
        if (adaptador.getCount() > 0) {
            mBuilder.setSingleChoiceItems(adaptador, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    StringWithTag partidaSeleccionada = (StringWithTag) ((AlertDialog) dialog).getListView().getItemAtPosition(which);
                    idPartidaSelecionada = partidaSeleccionada.tag;
                }
            });
        } else {
            mBuilder.setMessage("No hay partidas pendientes para identificar.");
        }
        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (adaptador.getCount() != 0 && idPartidaSelecionada != -1) {
                    new ComenzarIdentificacionPartidaServerCall(SelectAreaActivity.this, idPartidaSelecionada);
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    @Override
    public void customAlertFunction(Object object) {
        HashMap<String, String> hashMap = (HashMap<String, String>) object;
        if (hashMap.get("funcion") == "-1") {
            backButtonFunction();
        }
    }

    @Override
    public void backButtonFunction() {
        Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(goToNextActivity);
        finish();
    }

    @Override
    public void limpiarCampos() {

    }

    @Override
    public void customServerModelError(Object object) {

    }

    @Override
    public void onClick(final View v) {
        final Animation scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        v.startAnimation(scale);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (v.getId()) {
                    case R.id.btn_sa_Logout:
                        onBackPressed();
                        break;
                    case R.id.btn_sa_RegistroMaterias:
                        startActivity(new Intent(getApplicationContext(), RegistroMateriasPrimasActivity.class));
                        break;
                    case R.id.btn_sa_IdentificacionBolsa:
                        new TraerTodasLasPartidasPendientesServerCall(SelectAreaActivity.this);
                        break;
                    case R.id.btn_sa_RegistroSeparacion:
                        startActivity(new Intent(getApplicationContext(), SeparacionSueroActivity.class));
                        break;
                }
            }
        }, 300);
    }
}