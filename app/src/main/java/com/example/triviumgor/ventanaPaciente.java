package com.example.triviumgor;



import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.triviumgor.database.PacienteDBHelper;
import com.example.triviumgor.database.PacienteDataManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.net.Uri;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

public class ventanaPaciente extends AppCompatActivity {
    //solo para el edite
    private boolean existe = false;

    private int id = -1;
    //terminar
    private Button btnVerHistorico;
    private Button verList;
    private Button crearPac;
    private TextView nomSelPaciente;

    //atributos para ver lista
    private ListView vieLista;
    private RelativeLayout verLista;
    private Button btnBuscar;

    //editable
    private RelativeLayout grupoEditable; //puede que hagamos invisible el scrollview en vez de este layout, pero como vemos posivilidad de usar el scroll para otras cosas no lo cambiaremos
                                        //aunque podriamos cambiar de idea

    //atributos de la ventana para ver datos de paciente
    private RelativeLayout detallesPacienteLayout;
    private TextView tvDNI, tvCIC, tvNombreCompleto, tvPatologia, tvMedicacion, tvIntensidad, tvTiempo;

    private TextView tvIntensidad2, tvTiempo2; // para cuando esta conectado 2 dispositivos

    private Button btnEditar,btnBorrar, btnIniciarTratamiento;
    private int pacienteSeleccionadoId = -1;


    //atributos para insertar datos
    private EditText editDNI, editNombre, editApellido1, editApellido2;
    private EditText editPatologia, editMedicacion, editCIC;
    private EditText editIntensidad, editTiempo;

    private EditText editIntensidad2, editTiempo2; // para 1 paciente 2 dispositivos
    private Button btnGuardar;





    //el array
    String[] pacientesNombres = {"Paciente 1", "Paciente 2", "Paciente 3", "Paciente 4", "Paciente 5", "Paciente 6", "Paciente 7", "Paciente 8", "Paciente 9", "Paciente 10" };
    private int posicion;

    //atributos recividos de main
    String nomPacienteDado;
    String DNIpacienteDado;



    //base de datos
    private PacienteDataManager dataManager;
    private PacienteDBHelper dataHelper;

    //
    private boolean filtrado = false;

    private static final int STORAGE_PERMISSION_CODE = 101;


    //opcion dispositivo
    private int optionDis =  -1;

    //DNIdeotroDisp
    private String DNI_otroDisp;



    //empieza el codigo
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ventana_paciente);

        // PRIMERO: Inicializa dataManager explícitamente y verifica que no sea null
        try {
            dataManager = new PacienteDataManager(this);
            boolean dbOpened = dataManager.open();
            if (!dbOpened) {
                Toast.makeText(this, "Error al abrir la base de datos", Toast.LENGTH_LONG).show();
                // Si no podemos abrir la base de datos, finalizamos la actividad
                finish();
                return;
            }
        } catch (Exception e) {
            Log.e("ventanaPaciente", "Error al inicializar dataManager: " + e.getMessage());
            Toast.makeText(this, "Error fatal: No se pudo inicializar la base de datos", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // SEGUNDO: Inicializa vistas básicas
        try {
            nomSelPaciente = findViewById(R.id.nombrePaciente);
            verLista = findViewById(R.id.listaLayout);
            vieLista = findViewById(R.id.listaPacientes);

            // Resto de inicializaciones básicas de vistas
            grupoEditable = findViewById(R.id.grupoCrearPac);
            crearPac = findViewById(R.id.crear_paciente);
        } catch (Exception e) {
            Log.e("ventanaPaciente", "Error al inicializar vistas: " + e.getMessage());
        }

        // TERCERO: Configura los listeners para botones
        try {
            verList = findViewById(R.id.boton_verLista);
            verList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actualizarListaPacientes();
                    detallesPacienteLayout.setVisibility(View.GONE);
                    verLista.setVisibility(View.VISIBLE);
                    grupoEditable.setVisibility(View.GONE);
                }
            });

            crearPac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    limpiarCampos();
                    nomSelPaciente.setText("");
                    detallesPacienteLayout.setVisibility(View.GONE);
                    verLista.setVisibility(View.GONE);
                    grupoEditable.setVisibility(View.VISIBLE);

                    // Código para mostrar/ocultar campos según optionDis
                    if (optionDis == 3){
                        editIntensidad2.setVisibility(View.VISIBLE);
                        editTiempo2.setVisibility(View.VISIBLE);
                    } else {
                        editIntensidad2.setVisibility(View.GONE);
                        editTiempo2.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("ventanaPaciente", "Error al configurar listeners: " + e.getMessage());
        }

        // CUARTO: Retrasa la inicialización de componentes que usan dataManager
        // Usamos un Handler para asegurar que la UI esté completamente lista
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    inicializarComponentesConDataManager();
                } catch (Exception e) {
                    Log.e("ventanaPaciente", "Error en inicialización diferida: " + e.getMessage());
                    Toast.makeText(ventanaPaciente.this,
                            "Error al cargar datos de pacientes",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //ver lista
    private void inicializarComponentesConDataManager() {
        // Verificación explícita de dataManager antes de usar
        if (dataManager == null) {
            dataManager = new PacienteDataManager(this);
            boolean dbOpened = dataManager.open();
            if (!dbOpened) {
                Toast.makeText(this, "Error al abrir la base de datos", Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Ahora inicializamos los componentes que requieren dataManager
        inicializarCampos();
        inicializarDetallesPaciente();

        // Inicializar la lista (que incluye actualizar lista de pacientes)
        btnBuscar = findViewById(R.id.botonBuscar);

        // Configurar el adaptador base sin datos
        pacientesNombres = new String[]{"Cargando pacientes..."};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ventanaPaciente.this,
                android.R.layout.simple_list_item_1,
                pacientesNombres
        );
        vieLista.setAdapter(adapter);

        // Configurar eventos de la lista
        vieLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Por ejemplo:
                if (!(pacientesNombres.length == 1 && pacientesNombres[0].equals("No hay pacientes registrados"))){
                    String nombrePac = vieLista.getAdapter().getItem(position).toString();
                    nombrePac = nombrePac.substring(nombrePac.indexOf("-")+1);

                    Toast.makeText(ventanaPaciente.this,
                            "Seleccionaste: " + nombrePac,
                            Toast.LENGTH_SHORT).show();
                    posicion = position;
                    nomSelPaciente.setText(nombrePac);
                    verLista.setVisibility(View.INVISIBLE);
                }

                // Si no hay pacientes, no hacemos nada
                if (pacientesNombres.length == 1 && pacientesNombres[0].equals("No hay pacientes registrados")) {
                    return;
                }

                // Obtenemos el ID del paciente seleccionado en la base de datos
                if (filtrado){
                    String nombrePacFiltrado = vieLista.getAdapter().getItem(position).toString();
                    actualizarListaPacientes();
                    int pos = -1;
                    for (int i = 0; i < pacientesNombres.length; i++) {
                        if (nombrePacFiltrado.equals(pacientesNombres[i])){
                            pos = i;
                        }
                        pacienteSeleccionadoId = obtenerIdPacientePorPosicion(pos);
                    }
                }else{
                    pacienteSeleccionadoId = obtenerIdPacientePorPosicion(position);
                }

                if (pacienteSeleccionadoId != -1) {
                    // Cargar y mostrar los detalles del paciente
                    cargarDetallesPaciente(pacienteSeleccionadoId);

                    // Mostrar el layout de detalles y ocultar otros
                    detallesPacienteLayout.setVisibility(View.VISIBLE);
                    verLista.setVisibility(View.GONE);
                    grupoEditable.setVisibility(View.GONE);
                }
            }
        });

        // Configurar botón de búsqueda
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoParaFiltrar();
            }
        });

        // Finalmente, actualizar la lista de pacientes
        actualizarListaPacientes();

        procesarExtras();
    }



    private void procesarExtras() {
        Bundle extras = getIntent().getExtras();
        boolean IsVista = false;
        if (extras != null) {
            // Obtener los valores enviados
            int intensidadPaciente = extras.getInt("INTENSIDAD", 0);
            int tiempoPaciente = extras.getInt("TIEMPO", 0);
            nomPacienteDado = extras.getString("NOMBRE_PACIENTE", "");
            DNIpacienteDado = extras.getString("DNI_PACIENTE", "");
            optionDis = extras.getInt("DISPOSITIVO_ELEC", 0);

            DNI_otroDisp = extras.getString("DNI_PAC_OTRODISP", "");
            IsVista = extras.getBoolean("verSoloLista", false);

        }
        int posicionDado = -1;
        if (nomPacienteDado != null && !nomPacienteDado.isEmpty() &&
                DNIpacienteDado != null && !DNIpacienteDado.isEmpty()) {
            for (int i = 0; i < pacientesNombres.length; i++) {
                if (pacientesNombres[i].contains(nomPacienteDado) && pacientesNombres[i].contains(DNIpacienteDado)) {
                    posicionDado = i;
                }
            }
            String nombrePacDado = pacientesNombres[posicionDado];
            nombrePacDado = nombrePacDado.substring(nombrePacDado.indexOf("-") + 1);
            nomSelPaciente.setText(nombrePacDado);
            pacienteSeleccionadoId = obtenerIdPacientePorPosicion(posicionDado);
            cargarDetallesPaciente(pacienteSeleccionadoId);

            // Mostrar el layout de detalles y ocultar otros
            detallesPacienteLayout.setVisibility(View.VISIBLE);
            verLista.setVisibility(View.GONE);
            grupoEditable.setVisibility(View.GONE);

        } else {
            detallesPacienteLayout.setVisibility(View.GONE);
            verLista.setVisibility(View.GONE);
            grupoEditable.setVisibility(View.GONE);
            nomSelPaciente.setText("No hay Paciente Seleccionado");
        }
        if (IsVista){
            btnIniciarTratamiento.setVisibility(View.GONE);
            detallesPacienteLayout.setVisibility(View.GONE);
            verLista.setVisibility(View.VISIBLE);
            grupoEditable.setVisibility(View.GONE);
        }else{
            btnIniciarTratamiento.setVisibility(View.VISIBLE);

        }

    }

    private void mostrarDialogoParaFiltrar() {

        String[] camposFiltro = {"Nombre", "Apellidos", "DNI", "CIC", "Patologia"}; //lista de filtros

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filtrar_dialog, null);

        // Configurar el spinner
        Spinner spinnerCampo = dialogView.findViewById(R.id.spinnerCampoFiltro);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, camposFiltro);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampo.setAdapter(adapterSpinner);

        EditText editText = dialogView.findViewById(R.id.editTextEntrada);

        //cambiar el modo del edit text
        spinnerCampo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (camposFiltro[i] == "CIC"){
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }else{
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrar lista");
        builder.setView(dialogView);
        builder.setPositiveButton("Filtrar", (dialog, which) -> {
            String textoFiltro = editText.getText().toString().toLowerCase().trim();
            String campoSeleccionado = spinnerCampo.getSelectedItem().toString();
            filtrarLista(textoFiltro, campoSeleccionado);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());



        builder.create().show();
    }

    /**
     * Filtra la lista según el texto ingresado
     * @param filtro Texto para filtrar la lista
     *
     */
    private void filtrarLista(String filtro, String campo) {
        List<String> listaFiltrada = new ArrayList<>();


        // Asumiendo que tienes una lista original llamada "listaOriginal"
        for (int i = 0; i < pacientesNombres.length;i++ ) {
            boolean coincide = false;
            int id = obtenerIdPacientePorPosicion(i);
            Paciente paci = obtenerPacientePorId(id);
            switch (campo) {
                case "Nombre": //Funciona mal
                    coincide = paci.getNombre().toLowerCase().contains(filtro);
                    break;
                case "DNI":
                    coincide = paci.getDNI().toLowerCase().contains(filtro);
                    break;
                case "Apellidos":
                    String apel = paci.getAp1() + " " + paci.getAp2();
                    coincide = apel.toLowerCase().contains(filtro);
                    break;
                case "CIC":
                    coincide = paci.getCIC().toLowerCase().contains(filtro);
                    break;
                case "Patologia":
                    coincide = paci.getPatologia().toLowerCase().contains(filtro);
                    break;
                default:
                    // Buscar en todos los campos
                    coincide = pacientesNombres[i].substring(pacientesNombres.toString().indexOf(":")+1).toLowerCase().contains(filtro);
                    break;
            }

            if (coincide || filtro.isEmpty()) {
                listaFiltrada.add(pacientesNombres[i]);
            }
        }

        // Actualiza el adaptador con la nueva lista filtrada
        ArrayAdapter<String> adaptadorFiltrado = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaFiltrada
        );

        // Asumiendo que tienes un ListView llamado "listView"

        vieLista.setAdapter(adaptadorFiltrado);
        filtrado = true;

        // Muestra un mensaje con la cantidad de resultados
        if (filtro.isEmpty()) {
            Toast.makeText(this, "Mostrando todos los elementos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Se encontraron " + listaFiltrada.size() + " resultados", Toast.LENGTH_SHORT).show();
        }
    }

    //inicializar campo
    private void inicializarCampos() {
        // Enlazar con los elementos del layout
        editDNI = findViewById(R.id.editDNI);
        editNombre = findViewById(R.id.editNombre);
        editApellido1 = findViewById(R.id.editApellido1);
        editApellido2 = findViewById(R.id.editApellido2);
        editPatologia = findViewById(R.id.editPatologia);
        editMedicacion = findViewById(R.id.editMedicacion);
        editCIC = findViewById(R.id.editCIC);
        editIntensidad = findViewById(R.id.editIntensidad);
        editTiempo = findViewById(R.id.editTiempo);
        btnGuardar = findViewById(R.id.btnGuardar);

        //para cuando 1 paciente tiene 2 dispositivo
        editIntensidad2 = findViewById(R.id.editIntensidad2);
        editTiempo2 = findViewById(R.id.editTiempo2);

        // Configurar evento de click para guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPaciente();
            }
        });

        InputFilter filtroSoloLetras = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isDigit(source.charAt(i))) {
                        return ""; // Rechaza la entrada si hay dígitos
                    }
                }
                return null; // Acepta la entrada
            }
        };

        editNombre.setFilters(new InputFilter[]{filtroSoloLetras});
        editApellido1.setFilters(new InputFilter[]{filtroSoloLetras});
        editApellido2.setFilters(new InputFilter[]{filtroSoloLetras});

    }
    //metodos relacionados con base de datos
    private void guardarPaciente() {
        // Obtener los valores de los campos
        String dni = editDNI.getText().toString().trim();
        String nombre = editNombre.getText().toString().trim();
        String apellido1 = editApellido1.getText().toString().trim();
        String apellido2 = editApellido2.getText().toString().trim();
        String patologia = editPatologia.getText().toString().trim();
        String medicacion = editMedicacion.getText().toString().trim();
        String cic = editCIC.getText().toString().trim();

        // Validar campos obligatorios
        if (dni.isEmpty() || nombre.isEmpty() || apellido1.isEmpty()) {
            Toast.makeText(this, "Por favor, rellena los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        //comprobar formato dni
        if (dni.length() != 9) {
            Toast.makeText(this, "El DNI debe tener 8 números y 1 letra", Toast.LENGTH_SHORT).show();
            return;
        } else {
            boolean dniValido = true;
            // Verificar que los primeros 8 caracteres son números
            for (int i = 0; i < 8; i++) {
                if (!Character.isDigit(dni.charAt(i))) {
                    dniValido = false;
                    break;
                }
            }
            // Verificar que el último carácter es una letra
            if (!Character.isLetter(dni.charAt(8))) {
                dniValido = false;
            }

            if (!dniValido) {
                Toast.makeText(this, "Formato de DNI incorrecto (8 números + 1 letra)", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Comprobar que el dni no se repite
        boolean existeDNI = true;
        for (String dniPosibles: pacientesNombres) {
            if (dniPosibles.contains(dni)) {
                existeDNI = false;
                break;
            }
        }
        if (!existeDNI && !existe){
            Toast.makeText(this, "Ya existe un paciente con ese dni", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir intensidad y tiempo a enteros
        int intensidad, intensidad2 = 0;
        int tiempo, tiempo2 = 0;
        try {
            intensidad = Integer.parseInt(editIntensidad.getText().toString().trim());
            tiempo = Integer.parseInt(editTiempo.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La intensidad y el tiempo deben ser números", Toast.LENGTH_SHORT).show();
            return;
        }

        if (optionDis == 3){
            try {
                intensidad2 = Integer.parseInt(editIntensidad2.getText().toString().trim());
                tiempo2 = Integer.parseInt(editTiempo2.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La intensidad2 y el tiempo2 deben ser números", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Guardar en la base de datos

        long resultado = -1;

        if(existe){
            if (optionDis == 3){
                resultado = dataManager.actualizarPaciente2disp(id,dni, nombre, apellido1, apellido2,
                        patologia,medicacion, intensidad, tiempo, intensidad2, tiempo2, cic);
            }else{
                resultado = dataManager.actualizarPaciente(id,dni, nombre, apellido1, apellido2,
                        patologia,medicacion, intensidad, tiempo, cic);
            }

        }else{
            if (optionDis == 3){
                resultado = dataManager.nuevoPaciente2disp(dni, nombre, apellido1, apellido2,
                        patologia, medicacion, intensidad, tiempo, intensidad2, tiempo2, cic);
            }else{
                resultado = dataManager.nuevoPaciente(dni, nombre, apellido1, apellido2,
                        patologia, medicacion, intensidad, tiempo, cic);
            }


        }


        if (resultado != -1) {
            Toast.makeText(this, "Paciente guardado correctamente", Toast.LENGTH_SHORT).show();
            // Crear un objeto Paciente y añadirlo al array si necesitas
            Paciente nuevoPaciente = new Paciente(cic, dni, nombre, apellido1, apellido2,
                    patologia, medicacion, intensidad, tiempo);

            // Limpiar campos
            limpiarCampos();
            existe = false;
            id = -1;

            // Actualizar la lista de pacientes
            actualizarListaPacientes();

            // Ocultar formulario y mostrar lista
            grupoEditable.setVisibility(View.INVISIBLE);
            verLista.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Error al guardar el paciente", Toast.LENGTH_SHORT).show();
        }
    }
    private void limpiarCampos() {
        editDNI.setText("");
        editNombre.setText("");
        editApellido1.setText("");
        editApellido2.setText("");
        editPatologia.setText("");
        editMedicacion.setText("");
        editCIC.setText("");
        editIntensidad.setText("");
        editTiempo.setText("");

        //para cuando 1 paciente tiene 2 dispositivos
        editIntensidad2.setText("");
        editTiempo2.setText("");

    }
    private void actualizarListaPacientes() {
        // Obtener todos los pacientes de la base de datos
        Cursor cursor = dataManager.obtenerTodosPacientes();

        if (cursor != null && cursor.getCount() > 0) {
            // Crear un array para almacenar los nombres con DNI
            pacientesNombres = new String[cursor.getCount()];
            int i = 0;

            // Recorrer el cursor y llenar el array
            while (cursor.moveToNext()) {
                String dni = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_DNI));
                String nombre = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_NOMBRE));
                String apellido1 = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_APELLIDO1));
                String apellido2 = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_APELLIDO2));

                // Formato: "DNI: 12345678X - Nombre Apellido1 Apellido2"
                pacientesNombres[i] = "DNI: " + dni + " - " + nombre + " " + apellido1;

                // Añadir el segundo apellido solo si existe
                if (apellido2 != null && !apellido2.isEmpty()) {
                    pacientesNombres[i] += " " + apellido2;
                }

                i++;
            }

            // Actualizar el adaptador
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    pacientesNombres
            );
            vieLista.setAdapter(adapter);

            cursor.close();
        } else {
            // Si no hay pacientes, mostrar un array vacío o un mensaje
            pacientesNombres = new String[]{"No hay pacientes registrados"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    pacientesNombres
            );
            vieLista.setAdapter(adapter);
        }
    }

    //ver paciente
    private void inicializarDetallesPaciente() {
        detallesPacienteLayout = findViewById(R.id.detallesPacienteLayout);
        tvDNI = findViewById(R.id.tvDNI);
        tvCIC = findViewById(R.id.tvCIC);
        tvNombreCompleto = findViewById(R.id.tvNombreCompleto);
        tvPatologia = findViewById(R.id.tvPatologia);
        tvMedicacion = findViewById(R.id.tvMedicacion);
        tvIntensidad = findViewById(R.id.tvIntensidad);
        tvTiempo = findViewById(R.id.tvTiempo);
        btnIniciarTratamiento = findViewById(R.id.btnIniciarTratamiento);
        btnEditar = findViewById(R.id.editarPaciente);
        btnVerHistorico = findViewById(R.id.btnVerHistorico);
        btnBorrar = findViewById(R.id.borrarPaciente);

        //para cuando 1 paciente esta conectado a 2 dispositivos
        tvIntensidad2 = findViewById(R.id.tvIntensidad2);
        tvTiempo2 = findViewById(R.id.tvTiempo2);


        btnIniciarTratamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DNI_otroDisp != null && !DNI_otroDisp.isEmpty()){
                    Paciente pac = obtenerPacientePorId(pacienteSeleccionadoId);
                    if (DNI_otroDisp.equals(pac.getDNI())){
                        mostrarDialogoAlertaMismoPaciente();
                    }else{
                        cerrarYenviarInfo();
                    }
                }else{
                    cerrarYenviarInfo();
                }
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pacienteSeleccionadoId != -1) {
                    // Obtener los datos del paciente seleccionado
                    Paciente pac = obtenerPacientePorId(pacienteSeleccionadoId);

                    detallesPacienteLayout.setVisibility(View.GONE);
                    verLista.setVisibility(View.GONE);
                    grupoEditable.setVisibility(View.VISIBLE);

                    existe = true;
                    id = pac.getID();

                    editDNI.setText(pac.getDNI());


                    editNombre.setText(pac.getNombre());

                    editApellido1.setText(pac.getAp1());

                    if (pac.getAp2() != null){
                        editApellido2.setText(pac.getAp2());
                    }
                    if (pac.getPatologia() != null){
                        editPatologia.setText(pac.getPatologia());
                    }
                    if (pac.getMedicacion() != null){
                        editMedicacion.setText(pac.getMedicacion());
                    }
                    if (pac.getCIC() != null){
                        editCIC.setText(pac.getCIC());
                    }
                    if (pac.getIntensidad() >= 0){
                        editIntensidad.setText(Integer.toString(pac.getIntensidad()));
                    }
                    if (pac.getTiempoM() >= 0){
                        editTiempo.setText(Integer.toString(pac.getTiempoM()));
                    }

                    if (optionDis == 3){
                        if (pac.getIntensidad() >= 0){
                            editIntensidad2.setText(Integer.toString(pac.getIntensidad2()));
                        }
                        if (pac.getTiempoM() >= 0){
                            editTiempo2.setText(Integer.toString(pac.getTiempoM2()));
                        }
                        editIntensidad2.setVisibility(View.VISIBLE);
                        editTiempo2.setVisibility(View.VISIBLE);
                    }else{
                        editIntensidad2.setVisibility(View.GONE);
                        editTiempo2.setVisibility(View.GONE);
                    }



                }
            }
        });

        btnVerHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pacienteSeleccionadoId != -1) {
                    Paciente paciente = obtenerPacientePorId(pacienteSeleccionadoId);
                    if (paciente != null) {
                        // Abrir la actividad de historial
                        Intent intent = new Intent(ventanaPaciente.this, HistorialSesionesActivity.class);
                        intent.putExtra("PACIENTE_ID", pacienteSeleccionadoId);
                        String nombreCompleto = paciente.getNombre() + " " + paciente.getAp1();
                        if (paciente.getAp2() != null && !paciente.getAp2().isEmpty()) {
                            nombreCompleto += " " + paciente.getAp2();
                        }
                        intent.putExtra("NOMBRE_PACIENTE", nombreCompleto);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(ventanaPaciente.this,
                            "Seleccione un paciente primero",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });





        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isErased = dataManager.eliminarPaciente(pacienteSeleccionadoId);
                boolean isRebooted = dataManager.reiniciarAutoIncrement();

                if (!isErased) {
                    Toast.makeText(ventanaPaciente.this,"Error al borrar el paciente", Toast.LENGTH_SHORT).show();

                } else if (!isRebooted) {
                    Toast.makeText(ventanaPaciente.this, "Error al Reiniciar ID", Toast.LENGTH_SHORT).show();
                }else{
                    actualizarListaPacientes();
                    nomSelPaciente.setText("");
                    detallesPacienteLayout.setVisibility(View.GONE);
                    verLista.setVisibility(View.VISIBLE);
                    grupoEditable.setVisibility(View.GONE);
                    Toast.makeText(ventanaPaciente.this, "Exito en Borrar el Paciente", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
    private int obtenerIdPacientePorPosicion(int position) {
        Cursor cursor = dataManager.obtenerTodosPacientes();

        if (cursor != null && cursor.moveToPosition(position)) {
            int id = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_ID));
            cursor.close();
            return id;
        }

        if (cursor != null) {
            cursor.close();
        }

        return -1;
    }
    private void cargarDetallesPaciente(int pacienteId) {
        // Obtener el paciente de la base de datos por su ID
        Paciente paciente = obtenerPacientePorId(pacienteId);

        if (paciente != null) {
            // Establecer los valores en los TextView
            tvDNI.setText("DNI: " + paciente.getDNI());
            tvCIC.setText("CIC: " + paciente.getCIC());
            tvNombreCompleto.setText("Nombre: " + paciente.getNombre() + " " +
                    paciente.getAp1() + " " + paciente.getAp2());
            tvPatologia.setText("Patología: " + paciente.getPatologia());
            tvMedicacion.setText("Medicación: " + paciente.getMedicacion());
            tvIntensidad.setText("Intensidad: " + paciente.getIntensidad());
            tvTiempo.setText("Tiempo (min): " + paciente.getTiempoM());
            if (optionDis == 3){
                tvIntensidad2.setText("Intensidad2: " + paciente.getIntensidad2());
                tvTiempo2.setText("Tiempo2 (min): " + paciente.getTiempoM2());

                tvIntensidad2.setVisibility(View.VISIBLE);
                tvTiempo2.setVisibility(View.VISIBLE);
            }else{
                tvIntensidad2.setVisibility(View.GONE);
                tvTiempo2.setVisibility(View.GONE);
            }
        }
    }
    private Paciente obtenerPacientePorId(int pacienteId) {
        // Consultamos la base de datos para obtener los datos del paciente
        Cursor cursor = dataManager.obtenerPacientePorId(pacienteId);

        if (cursor != null && cursor.moveToFirst()) {
            // Extraer datos del cursor
            String cic = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_CIC));
            String dni = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_DNI));
            String nombre = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_NOMBRE));
            String ap1 = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_APELLIDO1));
            String ap2 = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_APELLIDO2));
            String patologia = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_PATOLOGIA));
            String medicacion = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_MEDICACIÓN));
            int intensidad = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_INTENSIDAD));
            int tiempo = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_TIEMPO));

            if (optionDis == 3){ //para 2 dispositivos
                int intensidad2 = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_INTENSIDAD2));
                int tiempo2 = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_TIEMPO2));


                cursor.close();



                return new Paciente(pacienteId, cic, dni, nombre, ap1, ap2, patologia, medicacion, intensidad, tiempo, intensidad2, tiempo2);
            }


            cursor.close();
            // Crear y devolver el objeto Paciente
            return new Paciente(pacienteId, cic, dni, nombre, ap1, ap2, patologia, medicacion, intensidad, tiempo);
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

//esto se encarga de hacer una base de datos fuera de la aplicacion
    private void checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
        } else {
        // Permisos concedidos, inicializar la base de datos
            initializeDatabase();
        }
    }
    private void initializeDatabase() {
        dataManager = new PacienteDataManager(this);
        boolean dbOpened = dataManager.open();
        if (!dbOpened) {
            Toast.makeText(this, "Error al abrir la base de datos", Toast.LENGTH_LONG).show();
        }

        // Continúa con la inicialización de tu app
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database connection when activity is destroyed
        if (dataManager != null) {
            dataManager.close();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, inicializar la base de datos
                initializeDatabase();
            } else {
                // Permiso denegado
                Toast.makeText(this,
                        "Esta aplicación requiere acceso al almacenamiento para funcionar correctamente",
                        Toast.LENGTH_LONG).show();
                // Puedes cerrar la app o proporcionar funcionalidad limitada
            }
        }



    }


    private void mostrarDialogoAlertaMismoPaciente(){
        LayoutInflater inflater = getLayoutInflater();
        /*
        View dialogView = inflater.inflate(R.layout.dialog_spinner, null);
        //hacer invisible el spinner pq no lo necesitamos
        Spinner spinnerDispositivo = dialogView.findViewById(R.id.spinnerOpciones);
        spinnerDispositivo.setVisibility(View.GONE);
        */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ya Esta Inicializado");
        builder.setMessage("Hemos descubierto que este Paciente ya esta Inicializado en el otro Dispositivo.\n" +
                "*Los cambios que hayas hecho en el otro dispositivo se reiniciaran al conectar ambos dispositivos");

        builder.setPositiveButton("Elegir otro Paciente", (dialog,which)->{
            detallesPacienteLayout.setVisibility(View.GONE);
            verLista.setVisibility(View.VISIBLE);
            grupoEditable.setVisibility(View.GONE);
            nomSelPaciente.setText("");
            dialog.dismiss();
        } );

        builder.setNegativeButton("Conectar ambos dispositivos", (dialog, which)->{
           optionDis = 3;
           cerrarYenviarInfo();
        });
        builder.create().show();
    }

    private void cerrarYenviarInfo(){
        if (pacienteSeleccionadoId != -1) {
            // Obtener los datos del paciente seleccionado
            Paciente paciente = obtenerPacientePorId(pacienteSeleccionadoId);


            // Aquí puedes iniciar una nueva actividad o pasar los datos a MainActivity
            // para configurar el tratamiento con los parámetros del paciente
            Intent resultIntent = new Intent(ventanaPaciente.this, MainActivity.class);
            resultIntent.putExtra("INTENSIDAD", paciente.getIntensidad());
            resultIntent.putExtra("TIEMPO", paciente.getTiempoM());
            resultIntent.putExtra("NOMBRE_PACIENTE", paciente.getNombre() + " " + paciente.getAp1());
            resultIntent.putExtra("DNI_PACIENTE", paciente.getDNI());
            resultIntent.putExtra("DISPOSITIVO_ELEC", optionDis);

            //para 1 paciente 2 dispositivos
            resultIntent.putExtra("INTENSIDAD2", paciente.getIntensidad2()); //no error?
            resultIntent.putExtra("TIEMPO2", paciente.getTiempoM2());

            // Establecer resultado como OK y adjuntar los datos



            setResult(RESULT_OK, resultIntent);
            finish(); // Opcional: cerrar esta actividad


        }
    }


}
