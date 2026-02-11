package com.example.triviumgor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triviumgor.database.PacienteDataManager;
import com.example.triviumgor.database.PacienteDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistorialSesionesActivity extends AppCompatActivity {

    private TextView tvTitulo;
    private TextView tvNombrePaciente;
    private TextView tvInfoHistorial;
    private ListView listViewSesiones;
    private Button btnVolver;

    private PacienteDataManager dataManager;
    private List<Sesion> sesiones;
    private int pacienteId;
    private String nombrePaciente;
    private SesionAdapter adapter;

    //prueva
    private ArrayAdapter<String> listaSesionPorDia;
    private Map<String,int[]> mapaParaAyuda;
    private boolean preparadoParaDarDetalleS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_sesiones);

        // Inicializar vistas
        tvTitulo = findViewById(R.id.tvTitulo);
        tvNombrePaciente = findViewById(R.id.tvNombrePaciente);
        tvInfoHistorial = findViewById(R.id.tvInfoHistorial);
        listViewSesiones = findViewById(R.id.listViewSesiones);
        btnVolver = findViewById(R.id.btnVolver);


        // Obtener datos del intent
        Intent intent = getIntent();
        pacienteId = intent.getIntExtra("PACIENTE_ID", -1);
        nombrePaciente = intent.getStringExtra("NOMBRE_PACIENTE");

        if (pacienteId == -1 || nombrePaciente == null) {
            Toast.makeText(this, "Error: datos del paciente no encontrados", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitulo.setText("Histórico de Sesiones");
        tvNombrePaciente.setText("Paciente: " + nombrePaciente);

        // Inicializar dataManager
        dataManager = new PacienteDataManager(this);
        if (!dataManager.open()) {
            Toast.makeText(this, "Error al abrir la base de datos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar sesiones del paciente
        cargarSesiones();

        // Configurar evento al hacer clic en un elemento de la lista
        listViewSesiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Mostrar detalles de la sesión seleccionada
                if (sesiones != null && position < sesiones.size()) {
                    if (!preparadoParaDarDetalleS){
                        Toast.makeText(HistorialSesionesActivity.this, listaSesionPorDia.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                        int[] indexes = mapaParaAyuda.get(listaSesionPorDia.getItem(position).toString()).clone();

                        adapter = new SesionAdapter(HistorialSesionesActivity.this, sesiones.subList(indexes[0],indexes[1])); // el indice final esta exluido por eso al conseguir los inex, el del fianl debe ser 1 más y no el index del ultimo de ese dia
                        listViewSesiones.setAdapter(adapter);
                        preparadoParaDarDetalleS = true;
                    }else{

                        mostrarDetallesSesion((Sesion) listViewSesiones.getAdapter().getItem(position));
                    }

                    //mostrarDetallesSesion(sesiones.get(position));
                }
            }
        });

        listViewSesiones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (preparadoParaDarDetalleS){
                    Sesion sesionEliminar = (Sesion) listViewSesiones.getAdapter().getItem(i);
                    DialogDeBorrarSesion(sesionEliminar );
                    return true;
                }
                return false;
            }
        });

        // Configurar botón volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preparadoParaDarDetalleS){
                    listViewSesiones.setAdapter(listaSesionPorDia);
                    preparadoParaDarDetalleS = false;
                }else{
                    finish();
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataManager != null) {
            dataManager.close();
        }
    }

    private void cargarSesiones() {
        try {
            sesiones = dataManager.obtenerSesionesPaciente(pacienteId);
            tvInfoHistorial.setText("Sesiones registradas: " + sesiones.size());

            if (sesiones.isEmpty()) {
                ArrayList<String> mensajes = new ArrayList<>();
                mensajes.add("No hay sesiones registradas para este paciente");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, mensajes);
                listViewSesiones.setAdapter(adapter);
                return;
            }

            // Crear el adaptador personalizado

            //adapter = new SesionAdapter(this, sesiones);
            //listViewSesiones.setAdapter(adapter);
            agruparPorDiaLista();
            preparadoParaDarDetalleS = false;
            listViewSesiones.setAdapter(listaSesionPorDia);

        } catch (Exception e) {
            Log.e("HistorialSesiones", "Error al cargar sesiones: " + e.getMessage());
            Toast.makeText(this, "Error al cargar sesiones", Toast.LENGTH_SHORT).show();
        }
    }


    private void agruparPorDiaLista(){
        mapaParaAyuda = new HashMap<>();
        listaSesionPorDia = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        final SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fecha;

        if (sesiones.size() == 1){
            try {
                fecha = formatoEntrada.parse(sesiones.get(0).getFecha());
                String fechaString = formatoSalida.format(fecha);
                mapaParaAyuda.put(fechaString, new int[]{0,1});
                listaSesionPorDia.add(fechaString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
        int[] listaId = new int[2];
        for (int i = 1; i < sesiones.size(); i++) { //1 pq comparamos  desde atras
            Sesion ses = sesiones.get(i);
            Sesion seAnte = sesiones.get(i-1);

            if (i == 1){
                listaId[0] = 0; //puede que el id de sesiones sea mejor
                listaId[1] = 1;
            }
            try {
                fecha = formatoEntrada.parse(seAnte.getFecha());
                String fechaAnte = formatoSalida.format(fecha);
                fecha = formatoEntrada.parse(ses.getFecha());
                String fechaDespues = formatoSalida.format(fecha);

                if (!fechaDespues.contentEquals(fechaAnte)){
                    listaId[1] = i; // esto es pq usamos la sublist y el index final se excluye
                    mapaParaAyuda.put(fechaAnte,listaId.clone());
                    listaSesionPorDia.add(fechaAnte);

                    listaId[0] = i; //para que no haya errores al final
                    listaId[1] = i+1; // esto es pq usamos la sublist y el index final se excluye
                }

                if(!(i+1 < sesiones.size())){
                    listaId[1] = i+1;
                    mapaParaAyuda.put(fechaDespues,listaId);
                    listaSesionPorDia.add(fechaDespues);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }

    }
    // Método para mostrar los detalles de una sesión en un diálogo
    private void mostrarDetallesSesion(Sesion sesion) {
        // Crear el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.detalle_sesion, null);
        builder.setView(dialogView);

        // Referenciar elementos del diálogo
        TextView tvFechaCompleta = dialogView.findViewById(R.id.tvFechaCompleta);
        TextView tvHora = dialogView.findViewById(R.id.tvHora);
        TextView tvDispositivo = dialogView.findViewById(R.id.tvDispositivo);
        TextView tvIntensidad = dialogView.findViewById(R.id.tvIntensidad);
        TextView tvTiempo = dialogView.findViewById(R.id.tvTiempo);
        Button btnCerrar = dialogView.findViewById(R.id.btnCerrar);
        Button btnBorrarS = dialogView.findViewById(R.id.tvBTNBorrar);

        // Formatear fecha y hora
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        try {
            Date fecha = formatoEntrada.parse(sesion.getFecha());
            tvFechaCompleta.setText(formatoFecha.format(fecha));
            tvHora.setText(formatoHora.format(fecha));
        } catch (ParseException e) {
            tvFechaCompleta.setText(sesion.getFecha());
            tvHora.setText("--:--:--");
        }

        // Configurar resto de datos
        tvDispositivo.setText(sesion.getDispositivo());
        tvIntensidad.setText(String.valueOf(sesion.getIntensidad()));
        tvTiempo.setText(sesion.getTiempo() + " minutos");

        // Crear el diálogo
        final AlertDialog dialog = builder.create();

        // Configurar el botón para cerrar el diálogo
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnBorrarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDeBorrarSesion(sesion);
            }
        });

        // Mostrar el diálogo
        dialog.show();
    }

    public void DialogDeBorrarSesion(Sesion sesionEliminar){
        LayoutInflater inflater = getLayoutInflater();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(HistorialSesionesActivity.this);
        builder.setTitle("Desea Borrar este Historico?");



        builder.setMessage(sesionEliminar.getFecha());

        builder.setPositiveButton("Borrar", (dialog, which) -> {
            Boolean eliminado = dataManager.eliminarSesion(sesionEliminar.getId());
            //adapter.remove(sesionEliminar);
            //listViewSesiones.deferNotifyDataSetChanged();
            cargarSesiones();

            if (eliminado){
                Toast.makeText(HistorialSesionesActivity.this, "Borrado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(HistorialSesionesActivity.this, "No se a borrado el Historico " + sesionEliminar.getFecha(), Toast.LENGTH_SHORT).show();
            }

        });


        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());


        builder.create().show();
    }

    // Clase adaptadora para mostrar solo las fechas en el ListView
    private class SesionAdapter extends ArrayAdapter<Sesion> {

        private final SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        private final SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        public SesionAdapter(HistorialSesionesActivity context, List<Sesion> sesiones) {
            super(context, R.layout.sesion, sesiones);
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.sesion, parent, false);
            }

            Sesion sesion = getItem(position);
            TextView tvFechaSesion = convertView.findViewById(R.id.tvFechaSesion);
            /*
            try {
                // Formatear la fecha para mostrarla en formato más legible (solo fecha, sin hora)
                Date fecha = formatoEntrada.parse(sesion.getFecha());
                tvFechaSesion.setText(formatoSalida.format(fecha));


            } catch (ParseException e) {
                tvFechaSesion.setText(sesion.getFecha());
            }*/
            tvFechaSesion.setText(sesion.getFecha());

            return convertView;
        }
    }
}