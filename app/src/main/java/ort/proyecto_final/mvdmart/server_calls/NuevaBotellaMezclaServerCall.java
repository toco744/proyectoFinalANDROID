package ort.proyecto_final.mvdmart.server_calls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import ort.proyecto_final.mvdmart.activities.SeparacionSueroActivity;
import ort.proyecto_final.mvdmart.config.Config;
import ort.proyecto_final.mvdmart.config.Constants;
import ort.proyecto_final.mvdmart.models.BotellaSuero;
import ort.proyecto_final.mvdmart.models.Item;

public class NuevaBotellaMezclaServerCall {

    private SeparacionSueroActivity activity;
    private Context context;

    public NuevaBotellaMezclaServerCall(final SeparacionSueroActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        String url = Constants.DOMAIN + "/api/botellademezcla/nueva/" + Config.getNumeroOperario(activity);
        activity.iniciarLoader();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            activity.finalizarLoader();
                            if (response.getBoolean("suceso")) {
                                activity.setNuevaBotellaDeMezclaSeleccionada(new Item(response.getString("retorno"), 1));
                                activity.botellaDeMezclaSeleccionada();
                            } else {
                                JSONArray errorArray = response.getJSONArray("mensajes");
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle(errorArray.getString(0));
                                builder.setMessage(errorArray.getString(1));
                                //builder.setIcon(R.drawable.ic_launcher_foreground);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.finalizarLoader();
                        if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(context, "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show();
                        } else if (error.networkResponse != null) {
                            switch (error.networkResponse.statusCode) {
                                case 502:
                                    Toast.makeText(context, "Error servidor 502", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        jsonObjectRequest.setRetryPolicy(Constants.mRetryPolicy);
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
