package ort.proyecto_final.mvdmart.models;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Partida implements Comparable<Partida> {

    private int localId, idFrigorifico, cantConservadoras, temperatura, peso, posFrigorifico, id;
    private String fecha, hora, numCote, nombreFrigorifico;
    private ArrayList<Bolsa> bolsas;
    private ArrayList<Item> items;

    //region Helper Getter's and Setter's


    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreFrigorifico() {
        return nombreFrigorifico;
    }

    public void setNombreFrigorifico(String nombreFrigorifico) {
        this.nombreFrigorifico = nombreFrigorifico;
    }

    public ArrayList<Bolsa> getBolsas() {
        return bolsas;
    }

    public void setBolsas(ArrayList<Bolsa> bolsas) {
        this.bolsas = bolsas;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getIdFrigorifico() {
        return idFrigorifico;
    }

    public void setIdFrigorifico(int idFrigorifico) {
        this.idFrigorifico = idFrigorifico;
    }

    public int getCantConservadoras() {
        return cantConservadoras;
    }

    public void setCantConservadoras(int cantConservadoras) {
        this.cantConservadoras = cantConservadoras;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNumCote() {
        return numCote;
    }

    public void setNumCote(String numCote) {
        this.numCote = numCote;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getPosFrigorifico() {
        return posFrigorifico;
    }

    public void setPosFrigorifico(int posFrigorifico) {
        this.posFrigorifico = posFrigorifico;
    }
    //endregion

    public Partida() {

    }

    public Partida(int idFrigorifico, int posFrigorifico, int cantConservadoras/*, int peso*/, int temperatura, String fecha, String pHora, String numCote) {
        this.idFrigorifico = idFrigorifico;
        this.cantConservadoras = cantConservadoras;
//        this.peso = peso;
        this.temperatura = temperatura;
        this.fecha = fecha;
        this.hora = pHora;
        this.numCote = numCote;
        this.posFrigorifico = posFrigorifico;
    }

    public Partida(int pId, String pNombreFrigorifico, String pNumeroCote, String pFecha, String pHora, ArrayList<Item> pBolsas) {
        this.id = pId;
        this.nombreFrigorifico = pNombreFrigorifico;
        this.items = pBolsas;
        this.numCote = pNumeroCote;
        this.fecha = pFecha;
        this.hora = pHora;
    }

    public static String[] validar(int cantConservadoras, int temperatura/*, int pesoTotal*/, String numeroCote, int idFrigorifico) {
        String camposIncorrectos = "Debe arreglar los siguientes campos:\n";
        int largoStringCampos = camposIncorrectos.length();
        if (idFrigorifico == -1)
            camposIncorrectos += "Frigorifico invalido.\n";
        if (cantConservadoras < 1 || cantConservadoras > 100)
            camposIncorrectos += "La cantidad de conservadoras debe estar entre 1 y 100.\n";
        if (temperatura < 0 || temperatura > 40)
            camposIncorrectos += "La temperatura debe estar entre 0 y 40.\n";
        if (numeroCote.length() > 50)
            camposIncorrectos += "El número de cote puede tener un máximo de 50 caracteres.\n";
//        if (pesoTotal < 0 || pesoTotal > 100)
//            camposIncorrectos += "El peso debe estar entre 0 y 100.\n";
        if (camposIncorrectos.length() == largoStringCampos) {
            camposIncorrectos = "Ok";
        }
        return new String[]{"DATOS INVÁLIDOS", camposIncorrectos};
    }

    public JSONObject toJSONObject() {
        JSONObject jsonBody = new JSONObject();
        try {
            String[] splitFecha = this.fecha.split("-");
            jsonBody.put("Fecha", splitFecha[2] + "-" + splitFecha[1] + "-" + splitFecha[0] + " " + this.hora + ":00");
            jsonBody.put("CodigoFrigorifico", this.idFrigorifico);
            jsonBody.put("NumeroDeCote", this.numCote);
            jsonBody.put("Temperatura", this.temperatura);
//            jsonBody.put("PesoTotal", this.peso);
            jsonBody.put("CantidadDeConservadoras", this.cantConservadoras);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonBody;
    }

    public static Partida fromJSONObject(JSONObject object) {
        Partida partida = new Partida();


        try {
            partida.nombreFrigorifico = object.getJSONObject("frigorifico").getString("nombre");
            String []fecha = object.getString("fecha").substring(0,10).split("-");
            partida.fecha = fecha[2] + "-" + fecha[1] + "-" + fecha[0];
            partida.id = object.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return partida;
    }


//    public static ArrayList<Partida> partidasParaSeparar(JSONArray partidasConBolsas) throws JSONException {
//        ArrayList<Partida> partidasParaSeparar = new ArrayList<>();
//        for (int i = 0; i < partidasConBolsas.length(); i++) {
//            JSONObject partida = partidasConBolsas.getJSONObject(i);
//            JSONArray bolsas = partida.getJSONArray("bolsaDeSangre");
//            ArrayList<Bolsa> bolsasDeSangre = new ArrayList<>();
//            for (int j = 0; j < bolsas.length(); j++) {
//                bolsasDeSangre.add(new Bolsa(bolsas.getJSONObject(j).getString("codigo")));
//            }
//            partidasParaSeparar.add(new Partida(partida.getInt("id"), partida.getJSONObject("frigorifico").getString("nombre"), partida.getString("numeroDeCote"),partida.getString("fechaCompleta"),bolsasDeSangre));
//        }
//        return partidasParaSeparar;
//    }

    public static HashMap<String, List<Item>> partidasParaSeparar(JSONArray partidasConBolsas) throws JSONException {
        HashMap<String, List<Item>> partidasParaSeparar = new HashMap<>();
        for (int i = 0; i < partidasConBolsas.length(); i++) {
            JSONObject partida = partidasConBolsas.getJSONObject(i);
            JSONArray bolsas = partida.getJSONArray("bolsaDeSangre");
            ArrayList<Item> bolsasDeSangre = new ArrayList<>();
            for (int j = 0; j < bolsas.length(); j++) {
                bolsasDeSangre.add(new Item(bolsas.getJSONObject(j).getString("codigo"), 0));
            }
            String llavePartida = partida.getJSONObject("frigorifico").getString("nombre") + " - " + partida.getString("fechaCompleta");
            partidasParaSeparar.put(llavePartida, bolsasDeSangre);
        }
        return partidasParaSeparar;
    }

    @Override
    public String toString() {
        return this.nombreFrigorifico + " - " + this.fecha;
    }

    @Override
    public int compareTo(@NonNull Partida p) {
        if (this.fecha.compareTo(p.fecha) != 0) {
            return this.fecha.compareTo(p.fecha);
        } else if (this.nombreFrigorifico.compareTo(p.nombreFrigorifico) != 0) {
            return this.hora.compareTo(p.hora);
        } else {
            return this.nombreFrigorifico.compareTo(p.nombreFrigorifico);
        }
    }
}
