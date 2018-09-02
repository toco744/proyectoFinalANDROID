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
import org.json.JSONException;
import org.json.JSONObject;

import ort.proyecto_final.mvdmart.activities.SeparacionSueroActivity;
import ort.proyecto_final.mvdmart.config.Config;
import ort.proyecto_final.mvdmart.config.Constants;
import ort.proyecto_final.mvdmart.helpers.HelpersFunctions;
import ort.proyecto_final.mvdmart.models.BotellaSuero;
import ort.proyecto_final.mvdmart.models.Item;

public class CambiarBotellaDeMezclaSeleccionadaServerCall {
    private SeparacionSueroActivity activity;
    private Context context;

    public CambiarBotellaDeMezclaSeleccionadaServerCall(final SeparacionSueroActivity activity, final Item botellaAnterior, final Item botellaNueva, boolean nueva) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        String url = Constants.DOMAIN + "/api/botellademezcla/cambiar";
        JSONObject sendObject = new JSONObject();
        try {
            sendObject.put("CodigoAnterior", botellaAnterior.getCodigo());
            sendObject.put("CodigoNueva", botellaNueva.getCodigo());
            sendObject.put("CodigoOperario", Config.getNumeroOperario(activity));
            sendObject.put("Nueva",nueva);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        activity.iniciarLoader();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, sendObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            activity.finalizarLoader();
                            if (response.getBoolean("suceso")) {
                                activity.cambiarBotellaDeMezcla();
                            } else {
                                activity.alert(activity, HelpersFunctions.errores(response.getJSONArray("mensajes")),null);
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