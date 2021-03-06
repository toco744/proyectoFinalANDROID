package ort.proyecto_final.mvdmart.server_calls;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import ort.proyecto_final.mvdmart.activities.Separacion;
import ort.proyecto_final.mvdmart.config.Config;
import ort.proyecto_final.mvdmart.config.Constants;
import ort.proyecto_final.mvdmart.helpers.HelpersFunctions;

public class CancelarSeparacionServerCall {

    private Separacion activity;
    private Context context;

    public CancelarSeparacionServerCall(final Separacion activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        String url = Constants.DOMAIN + "/api/separacion/cancelar/" + Config.getNumeroOperario(activity);
        activity.iniciarLoader();
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            activity.finalizarLoader();
                            if (response.getBoolean("suceso")) {
                                activity.finish();
                            } else {
                                activity.alert(activity, HelpersFunctions.errores(response.getJSONArray("mensajes")), null);
                            }
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.finalizarLoader();
                        String errorMensaje[] = new String[2];
                        errorMensaje[0] = "Error en el servidor";
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMensaje[1] = "No se pudo conectar con el servidor";
                        } else if (error.networkResponse != null) {
                            switch (error.networkResponse.statusCode) {
                                case 500:
                                    errorMensaje[1] = "Código 500 – Internal Server Error\n";
                                    break;
                                case 502:
                                    errorMensaje[1] = "Código 502 – Bad Gateway\n";
                                    break;
                            }
                        } else {
                            errorMensaje[1] = "Error en servidor\n";
                        }
                        activity.alert(activity, errorMensaje, null);
                    }
                });
        jsonObjectRequest.setRetryPolicy(Constants.mRetryPolicy);
        VolleyRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
