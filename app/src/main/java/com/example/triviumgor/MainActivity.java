package com.example.triviumgor;
//En esta APP se manejan dos devices ambos con los mismos parametros excepto intensidad
//Los dos canales de cada device son comunes.
//El ancho de pulso es prefijado a 4ms y la frecuenca a 10 Hz (aunque en Gor puede haber Gor1 (10Hz) y Gor2 (20Hz)

import static android.graphics.Color.RED;
import static java.time.Instant.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
    import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
    import android.database.Cursor;
    import android.graphics.Color;
    import android.os.Build;
    import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.Spinner;
    import android.widget.Switch;
    import android.widget.TextView;
    import android.widget.Button;
    import android.bluetooth.BluetoothAdapter;
    import android.bluetooth.BluetoothDevice;
    import android.bluetooth.BluetoothSocket;

    import java.io.BufferedReader;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.io.OutputStream;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Set;
    import java.util.UUID;
    import java.io.IOException;

    import android.widget.Toast;
    import android.widget.EditText;
    import android.util.Log;
    import android.os.Bundle;

    import java.time.Instant;
    import java.time.ZoneId;
    import java.time.ZonedDateTime;
    import java.util.Calendar;

    //import Kerman y Aitor


    import android.view.LayoutInflater;

    import androidx.appcompat.app.AlertDialog;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;

    import com.example.triviumgor.database.PacienteDBHelper;
    import com.example.triviumgor.database.PacienteDataManager;

    import android.view.KeyEvent;
    import android.view.inputmethod.EditorInfo;
    import android.Manifest;
    import android.os.Environment;
    import android.provider.Settings;
    import android.net.Uri;

    //private int valor = 0; // Inicializar al declarar


 public class MainActivity extends AppCompatActivity {
    String[] DirMacs = new String[10];
    int indiceDirMACs = 0;
    private final String NombreFichero = "dir_macs.txt";
    private static final int[] miByteArrayWrite = new int[80];
    private static final int[] miByteArrayWrite2 = new int[30];
    int IndiceRec = 0;
    private static final byte[] miByteRec = new byte[58];
    int[] IniTablaAmplitud = new int[17];
    private BluetoothAdapter btAdapter;
    private BluetoothDevice btDevice;
    private BluetoothDevice btDevice2;
    private BluetoothDevice btDevice3;
    private BluetoothSocket btSocket;
    private BluetoothSocket btSocket2;
    private BluetoothSocket btSocket3;



    private static OutputStream outputStream = null;//ver si hace falta public static
    private static InputStream mInputStream = null;
    private static final OutputStream outputStream2 = null;//ver si hace falta public static
    private static final InputStream mInputStream2 = null;
    private static OutputStream outputStream3 = null;//ver si hace falta public static
    private static InputStream mInputStream3 = null;
    public static String toastText = "";
    private EditText Param1;
    private EditText Param2;
    private EditText Param3;
    private EditText Param4;
    private EditText Param5;
    private EditText Param6;
    private EditText Param7;
    private EditText Param8;
    private EditText Param9;
    private EditText Param10;
    private EditText Param11;
    private EditText Param12;
    private EditText Param13;
    private int NivelCarga = 0;
    private int ValorCarga = 0;
    private int ValorCargaAnt = 0;
    private int ValorCarga2 = 0;
    private int ValorCargaAnt2 = 0;
    private EditText NivelBatt;
    private EditText NivelBatt2;
    Switch MACSwitch;//
    //Switch SwitchSync ;//
    //Switch SwitchBurst ;
    int Intensidad, Intensidad2, Intensidad3, Intensidad4, AnchoPulso, Periodo_ms, Duracion_Min, Duracion_Min2;//Param1;
    private TextView Otrotexto1, Otrotexto2, Otrotexto3, Otrotexto4, OtroTexto5, OtroTexto6, OtroTexto7;
    //private TextView OtroTexto5;
    private Button Conexion;
    private Button InicioPulsos;
    private Button FinPulsos;
    private Button Disconnect;
    private Button AmpliInc1;
    //"@+id/buttonAmpliInc"
    private Button AmpliInc2;
    private Button AmpliDec1;
    private Button AmpliDec2;
    private Button Conexion2;
    private Button InicioPulsos2;
    private Button FinPulsos2;
    private Button Disconnect2;
    private Button AmpliInc3;
    //"@+id/buttonAmpliInc"
    private Button AmpliInc4;
    private Button AmpliDec3;
    private Button AmpliDec4;

    boolean IsConnected = false;
    boolean IsBattMon = false;
    boolean IsBattMonSent = false;
    boolean IsConnected2 = false;
    boolean IsBattMon2 = false;
    boolean IsBattMonSent2 = false;
    boolean IsConnected3 = false;
    boolean IsBattMon3 = false;
    boolean IsBattMonSent3 = false;
    int N_Deviceconnected1 = 20;
    int N_Deviceconnected2 = 20;
    int N_Bytes = 0;
    int N_Bytes2 = 0;
    int N_Bytes3 = 0;
    String NameDevice = "";
    String NameDevice2 = "";
    String NameDevice3 = "";
    String address2 = "";
    String address3 = "";
    String address = "";
    String writeMessage = "";
    int minutoAct = 0;
    int minutoAnt = 0;
    int minutotranscurrido = 0;
    int minutoAct2 = 0;
    int minutoAnt2 = 0;
    int minutotranscurrido2 = 0;
    boolean IsDualdeviceON = false;
    boolean IsClockStop = true;
    boolean IsClockStop2 = true;
    Instant now;
    ZonedDateTime zdt;
    int valor;
    Spinner spinnerMAC;
    final Handler handler = new Handler();
    /*
    //Spinner spinnerMAC; //
    //spinnerMAC= (Spinner)findViewById(R.id.MACs_spinner);
//<string-array name="MACs_Dir">
    // Create an ArrayAdapter using the string array and a default spinner layout.
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this,
            R.array.MACs_Dir,
            android.R.layout.simple_spinner_item
    );
// Specify the layout to use when the list of choices appears.
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
spinnerMAC.setAdapter(adapter);
    */
    /*
    static int i;
    String MAC_BT="";
    String MAC_BT2="";

    Boolean switchState;
    Boolean switchStateSync;
    Boolean switchStateBurst;
*/
    //private ConnectThread mConnectThread=null;
    private ConnectedThread mConnectedThread = null;
    private ConnectedThread2 mConnectedThread2 = null;
    final Handler mHandler = new Handler();


    //creado este cacho por KERMAN Y aITOR

    private Button ConectarPaciente;
    private Button ventanaPaciente;
    String nombrePaciente;
    String DNIpaciente;

    String nombrePaciente2;
    String DNIpaciente2;

    int opcionDispositivoInt = -1; // 1 = Dispositivo 1,  2 = Dispositivo 2,  3 = Todos los dispositivos

    private TextView nombreDispositivoPac1;
    private TextView nombreDispositivoPac2;

    private Button btnVerLista;

    private TextView dispBluetoothNom1;
    private TextView dispBluetoothNom2;

    private PacienteDataManager dataManager;


    private static final int PACIENTE_REQUEST_CODE = 100;
    private static final int BLUETOOTH_PERMISSION_REQUEST = 1;


    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int ALL_FILES_ACCESS_CODE = 102;
    private static final int BLUETOOTH_PERMISSION_CODE = 103;

     private SharedPreferences sharedPreferences;


    //TERMINA CAMBIO



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkStoragePermissions();


        // Inicializar el gestor de datos
        dataManager = new PacienteDataManager(this);
        boolean dbOpened = dataManager.open();
        if (!dbOpened) {
            Toast.makeText(this, "Error al abrir la base de datos", Toast.LENGTH_LONG).show();
        }

        /*
        spinnerMAC= (Spinner)findViewById(R.id.MACs_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.MACs_Dir,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinnerMAC.setAdapter(adapter);
        */
        //Otrotexto1 = (TextView) this.findViewById(R.id.textView_AnchoPulso);
        //Otrotexto2 = (TextView) this.findViewById(R.id.textView_Freq);
        Otrotexto3 = (TextView) this.findViewById(R.id.textView_Int);
        Otrotexto4 = (TextView) this.findViewById(R.id.textView_Duracion);
        OtroTexto5 = (TextView) this.findViewById(R.id.textView_tiemporestante);//OtroTexto5

        OtroTexto6 = (TextView) this.findViewById(R.id.textView_NivelBatt);//OtroTexto5
        OtroTexto7 = (TextView) this.findViewById(R.id.textView_NivelBatt2);//OtroTexto5
        //Freq= (TextView) this.findViewById(R.id.textView_Freq);
        //Param1 = (EditText) this.findViewById(R.id.SetAnchoPulso);
        //Param2 = (EditText) this.findViewById(R.id.SetFreq);
        Param3 = (EditText) this.findViewById(R.id.SetIntensidad);
        Param4 = (EditText) this.findViewById(R.id.SetDuracion);
        Param12 = (EditText) this.findViewById(R.id.SetDuracion2);

        Param5 = (EditText) this.findViewById(R.id.SetTiempoRestante);
        Param13 = (EditText) this.findViewById(R.id.SetTiempoRestante2);
        //Param6 = (EditText) this.findViewById(R.id.SetFreq3);
        //Param7 = (EditText) this.findViewById(R.id.SetFreq4);
        //Param8 = (EditText) this.findViewById(R.id.SetFreq2);
        //Param9 = (EditText) this.findViewById(R.id.SetIntensidad2);
        Param10 = (EditText) this.findViewById(R.id.SetIntensidad3);
        //Param11 = (EditText) this.findViewById(R.id.SetIntensidad4);
        NivelBatt = (EditText) this.findViewById(R.id.LevelBatt);
        NivelBatt2 = (EditText) this.findViewById(R.id.LevelBatt2);

        setupRangeValidation(Param3, 1, 19, "intensidad");
        setupRangeValidation(Param10, 1, 19, "intensidad");
        setupRangeValidation(Param4, 1, 60, "tiempo");
        setupRangeValidation(Param12, 1, 60, "tiempo");

        //MACSwitch= (Switch) findViewById(R.id.switchMAC);
        //SwitchSync= (Switch) findViewById(R.id.SwitchSync);
        //SwitchBurst= (Switch) findViewById(R.id.SwitchBursts);
        IniTablaAmplitud[0] = 10;//5V=42
        IniTablaAmplitud[1] = 13;//6V=48
        IniTablaAmplitud[2] = 17;//7V=52
        IniTablaAmplitud[3] = 21;//8V=56
        IniTablaAmplitud[4] = 24;//9V=60
        IniTablaAmplitud[5] = 28;//10V=61
        IniTablaAmplitud[6] = 32;//11V=63 -->/4=15
        IniTablaAmplitud[7] = 35;//12V=64 -->/4=16
        IniTablaAmplitud[8] = 38;//13V=65
        IniTablaAmplitud[9] = 40;//14V=66
        IniTablaAmplitud[10] = 42;//15V=67
        IniTablaAmplitud[11] = 45;//16V=68
        IniTablaAmplitud[12] = 48;//17V=69
        IniTablaAmplitud[13] = 50;//18V=70
        IniTablaAmplitud[14] = 52;//19V=71
        IniTablaAmplitud[15] = 55;//20V=72
        IniTablaAmplitud[16] = 57;//20V=72

        Duracion_Min = Integer.parseInt(Param4.getText().toString());
        Duracion_Min2 = Integer.parseInt(Param12.getText().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = now();
            zdt = now.atZone(ZoneId.systemDefault());
            minutoAct = zdt.getMinute();
            Log.d("APP", "Minuto Actual:" + minutoAct);
            minutoAnt = minutoAct;
            //!!!OJO SOLO DEBUG
            //IsDualdeviceON=true;
        } else {

            Calendar calendar = Calendar.getInstance();
            minutoAct = calendar.get(Calendar.MINUTE);
            //Date currentTime = Calendar.getInstance().getTime();
            String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
            //String strDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
            Log.d("APP", "Minuto Actual:" + minutoAct + ";" + strDate);
            minutoAnt = minutoAct;
            //!!!OJO SOLO DEBUG
            IsDualdeviceON = true;
        }
        Param5.setText(String.valueOf(Duracion_Min));
        //timer();

        dispBluetoothNom1 = findViewById(R.id.deviceBluetoothNom1);
        dispBluetoothNom2 = findViewById(R.id.deviceBluetoothNom2);


        Conexion = (Button) findViewById(R.id.button_Connect);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Conexion.setOnClickListener(new Button.OnClickListener() {

                                        public void onClick(View arg0) {
                                            Conexion.setBackgroundColor(0xFFFF5733);
                                            Conexion.setText("conectando");
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Do something after 5s = 5000ms

                                                    abrirfile();
                                                    // Verificar si el Bluetooth está activado
                                                    if (!btAdapter.isEnabled()) {
                                                        Toast.makeText(MainActivity.this, "Por favor, activa el Bluetooth", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }

                                                    // Comprobar permisos para Android 12 o superior
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT)
                                                                != PackageManager.PERMISSION_GRANTED) {
                                                            ActivityCompat.requestPermissions(MainActivity.this,
                                                                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                                                                    BLUETOOTH_PERMISSION_REQUEST);
                                                            return;
                                                        }
                                                    }else {
                                                        // Android 11 y anteriores
                                                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BLUETOOTH_PERMISSION_REQUEST); //puede ser permission_code
                                                        }
                                                    }

                                                    //cambios por Kerman y Aitor
                                                    mostrarDialogoListaBluetooth(1);
                                                    //connectToDevice();
                                                }
                                            }, 1000);
                                            //abrirfile();
                                            //connectToDevice();
                                            //mConnectThread.start();
                                        }
                                    }
        );

        Conexion2 = (Button) findViewById(R.id.button_Connect2);
        Conexion2.setOnClickListener(new Button.OnClickListener() {

                                         public void onClick(View arg0) {
                                             Conexion2.setBackgroundColor(0xFFFF5733);
                                             Conexion2.setText("conectando");
                                             handler.postDelayed(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     // Do something after 5s = 5000ms

                                                     abrirfile();
                                                     // Verificar si el Bluetooth está activado
                                                     if (!btAdapter.isEnabled()) {
                                                         Toast.makeText(MainActivity.this, "Por favor, activa el Bluetooth", Toast.LENGTH_SHORT).show();
                                                         return;
                                                     }

                                                     // Comprobar permisos para Android 12 o superior
                                                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                         if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT)
                                                                 != PackageManager.PERMISSION_GRANTED) {
                                                             ActivityCompat.requestPermissions(MainActivity.this,
                                                                     new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                                                                     BLUETOOTH_PERMISSION_REQUEST);
                                                             return;
                                                         }
                                                     }

                                                     //cambios por Kerman y Aitor
                                                     mostrarDialogoListaBluetooth(2);
                                                 }
                                             }, 1000);
                                         }
                                     }
        );
        AmpliInc1 = (Button) findViewById(R.id.buttonAmpliInc);

        AmpliInc1.setOnClickListener(new Button.OnClickListener() //boton +
                                     {

                                         public void onClick(View arg0) {
                                             Intensidad = Integer.parseInt(Param3.getText().toString());
                                             Intensidad++;
                                             if (Intensidad > 19)
                                                 Intensidad = 19;
                                             Param3.setText(String.valueOf(Intensidad));
                                         }
                                     }
        );

        AmpliDec1 = (Button) findViewById(R.id.buttonAmpliDec);

        AmpliDec1.setOnClickListener(new Button.OnClickListener() //boton -
                                     {

                                         public void onClick(View arg0) {
                                             Intensidad = Integer.parseInt(Param3.getText().toString());
                                             Intensidad--;
                                             if (Intensidad < 1)
                                                 Intensidad = 1;
                                             Param3.setText(String.valueOf(Intensidad));
                                         }
                                     }
        );
        /*
        AmpliInc2=(Button)findViewById(R.id.buttonAmpliInc2);

        AmpliInc2.setOnClickListener(new Button.OnClickListener()
                                     {

                                         public void onClick(View arg0)
                                         {
                                             Intensidad2=Integer.parseInt(Param9.getText().toString());
                                             Intensidad2++;
                                             if(Intensidad2>19)
                                                 Intensidad2=19;
                                             Param9.setText(String.valueOf(Intensidad2));
                                         }
                                     }
        );
        AmpliDec2=(Button)findViewById(R.id.buttonAmpliDec2);

        AmpliDec2.setOnClickListener(new Button.OnClickListener()
                                     {

                                         public void onClick(View arg0)
                                         {
                                             Intensidad2=Integer.parseInt(Param9.getText().toString());
                                             Intensidad2--;
                                             if(Intensidad2<1)
                                                 Intensidad2=1;
                                             Param9.setText(String.valueOf(Intensidad2));
                                         }
                                     }
        );
        */

        AmpliInc3 = (Button) findViewById(R.id.buttonAmpliInc3);

        AmpliInc3.setOnClickListener(new Button.OnClickListener() {

                                         public void onClick(View arg0) {
                                             Intensidad3 = Integer.parseInt(Param10.getText().toString());
                                             Intensidad3++;
                                             if (Intensidad3 > 19)
                                                 Intensidad3 = 19;
                                             Param10.setText(String.valueOf(Intensidad3));
                                         }
                                     }
        );
        AmpliDec3 = (Button) findViewById(R.id.buttonAmpliDec3);

        AmpliDec3.setOnClickListener(new Button.OnClickListener() {

                                         public void onClick(View arg0) {
                                             Intensidad3 = Integer.parseInt(Param10.getText().toString());
                                             Intensidad3--;
                                             if (Intensidad3 < 1)
                                                 Intensidad3 = 1;
                                             Param10.setText(String.valueOf(Intensidad3));
                                         }
                                     }
        );
        /*
        AmpliInc4=(Button)findViewById(R.id.buttonAmpliInc4);

        AmpliInc4.setOnClickListener(new Button.OnClickListener()
                                     {

                                         public void onClick(View arg0)
                                         {
                                             Intensidad4=Integer.parseInt(Param11.getText().toString());
                                             Intensidad4++;
                                             if(Intensidad4>19)
                                                 Intensidad4=19;
                                             Param11.setText(String.valueOf(Intensidad4));
                                         }
                                     }
        );
        AmpliDec4=(Button)findViewById(R.id.buttonAmpliDec4);

        AmpliDec4.setOnClickListener(new Button.OnClickListener()
                                     {

                                         public void onClick(View arg0)
                                         {
                                             Intensidad4=Integer.parseInt(Param11.getText().toString());
                                             Intensidad4--;
                                             if(Intensidad4<1)
                                                 Intensidad4=1;
                                             Param11.setText(String.valueOf(Intensidad4));
                                         }
                                     }
        );
        */
        InicioPulsos = (Button) findViewById(R.id.button_IniPulsos);
        InicioPulsos.setEnabled(false);
        InicioPulsos.setOnClickListener(new Button.OnClickListener() {
            int auxint1, auxint2, index = 0;

            public void onClick(View arg0) {
                try {
                    if (IsConnected3) {
                        InicioPulsos.setBackgroundColor(Color.GREEN);

                        Intensidad = Integer.parseInt(Param3.getText().toString());
                        Duracion_Min = Integer.parseInt(Param4.getText().toString());

                        // Detectar si hay una sesión activa
                        boolean sesionActiva = !IsClockStop;

                        if (sesionActiva) {
                            // HAY SESIÓN ACTIVA: Solo actualizar intensidad
                            Log.d("APP", "Actualizando intensidad en sesión activa");
                            Log.d("APP", "Tiempo transcurrido: " + minutotranscurrido + " min");

                            Toast.makeText(MainActivity.this,
                                    "Actualizando intensidad a " + Intensidad,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // NO HAY SESIÓN ACTIVA: Es una sesión nueva
                            Log.d("APP", "Iniciando nueva sesión");

                            // Registrar la sesión en la base de datos
                            if (DNIpaciente != null && !DNIpaciente.isEmpty()) {
                                Cursor cursor = dataManager.obtenerTodosPacientes();
                                int idPaciente = -1;

                                if (cursor != null && cursor.moveToFirst()) {
                                    do {
                                        String dni = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_DNI));
                                        if (dni.equals(DNIpaciente)) {
                                            idPaciente = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_ID));
                                            break;
                                        }
                                    } while (cursor.moveToNext());
                                    cursor.close();
                                }

                                if (idPaciente != -1) {
                                    long idSesion = -1;
                                    if (opcionDispositivoInt == 3 && IsConnected) {
                                        idSesion = dataManager.registrarSesion(
                                                idPaciente,
                                                dispBluetoothNom1.getText().toString(),
                                                Intensidad,
                                                Duracion_Min
                                        );
                                    } else {
                                        idSesion = dataManager.registrarSesion(
                                                idPaciente,
                                                dispBluetoothNom1.getText().toString(),
                                                Intensidad,
                                                Duracion_Min
                                        );
                                    }

                                    if (idSesion != -1) {
                                        Toast.makeText(MainActivity.this,
                                                "Sesión registrada correctamente",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("MainActivity", "Error al registrar la sesión");
                                    }
                                }
                            }

                            // RESETEAR temporizador SOLO si es sesión nueva
                            minutotranscurrido = 0;
                        }

                        // Preparar datos a enviar (común para ambos casos)
                        index = 0;
                        miByteArrayWrite[index] = 0x41;
                        index++;

                        // Ancho de pulso (4ms)
                        AnchoPulso = 4;
                        auxint1 = AnchoPulso / 1000;
                        auxint2 = AnchoPulso % 1000;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 100;
                        auxint2 = auxint2 % 100;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 10;
                        auxint2 = auxint2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Repetir ancho de pulso
                        AnchoPulso = 4;
                        auxint1 = AnchoPulso / 1000;
                        auxint2 = AnchoPulso % 1000;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 100;
                        auxint2 = auxint2 % 100;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 10;
                        auxint2 = auxint2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Periodo (100ms = 10Hz)
                        Periodo_ms = 100;
                        auxint1 = Periodo_ms / 1000;
                        auxint2 = Periodo_ms % 1000;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 100;
                        auxint2 = auxint2 % 100;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 10;
                        auxint2 = auxint2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Repetir periodo
                        auxint1 = Periodo_ms / 1000;
                        auxint2 = Periodo_ms % 1000;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 100;
                        auxint2 = auxint2 % 100;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 10;
                        auxint2 = auxint2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Intensidad (ACTUALIZADA)
                        auxint1 = Intensidad / 10;
                        auxint2 = Intensidad % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Repetir intensidad
                        auxint1 = Intensidad / 10;
                        auxint2 = Intensidad % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Duración
                        auxint1 = Duracion_Min / 10;
                        auxint2 = Duracion_Min % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        miByteArrayWrite[index] = 66; // "B"
                        index++;

                        // Enviar al dispositivo
                        for (int t = 0; t < index; t++) {
                            outputStream3.write(miByteArrayWrite[t]);
                            outputStream3.flush();
                            for (int s = 60000; s > 0; s--);
                        }

                        // Iniciar timer SOLO si es sesión nueva
                        if (!sesionActiva) {
                            IsBattMon3 = true;
                            IndiceRec = 0;
                            IsClockStop = false;
                            N_Bytes = 0;
                            TimeHandler.postDelayed(CheckTimer, 20000);
                            Log.d("APP", "InicioThread connected");
                            mConnectedThread = new ConnectedThread();
                            mConnectedThread.start();

                            //  CAMBIAR TEXTO DEL BOTÓN A "ACTUALIZAR"
                            InicioPulsos.setText("Actualizar");
                        }
                    }
                } catch (Exception createException) {
                    Log.d("APP", "Error: " + createException.getMessage());
                }
            }
        });
        InicioPulsos2 = (Button) findViewById(R.id.button_IniPulsos2);
        InicioPulsos2.setEnabled(false);
        InicioPulsos2.setOnClickListener(new Button.OnClickListener() {
            int auxint1, auxint2, index = 0;

            public void onClick(View arg0) {
                try {
                    if (IsConnected) {
                        InicioPulsos2.setBackgroundColor(Color.GREEN);

                        Intensidad = Integer.parseInt(Param10.getText().toString());
                        Duracion_Min2 = Integer.parseInt(Param12.getText().toString());

                        //  NUEVO: Detectar si hay una sesión activa
                        boolean sesionActiva = !IsClockStop2;

                        if (sesionActiva) {
                            // HAY SESIÓN ACTIVA: Solo actualizar intensidad
                            Log.d("APP", "Actualizando intensidad en sesión activa (Dispositivo 2)");
                            Log.d("APP", "Tiempo transcurrido: " + minutotranscurrido2 + " min");

                            Toast.makeText(MainActivity.this,
                                    "Actualizando intensidad a " + Intensidad,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // NO HAY SESIÓN ACTIVA: Es una sesión nueva
                            Log.d("APP", "Iniciando nueva sesión (Dispositivo 2)");

                            // Registrar la sesión en la base de datos
                            if (DNIpaciente2 != null && !DNIpaciente2.isEmpty()) {
                                Cursor cursor = dataManager.obtenerTodosPacientes();
                                int idPaciente = -1;

                                if (cursor != null && cursor.moveToFirst()) {
                                    do {
                                        String dni = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_DNI));
                                        if (dni.equals(DNIpaciente2)) {
                                            idPaciente = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_ID));
                                            break;
                                        }
                                    } while (cursor.moveToNext());
                                    cursor.close();
                                }

                                if (idPaciente != -1) {
                                    long idSesion = -1;
                                    if (opcionDispositivoInt == 3 && IsConnected3) {
                                        idSesion = dataManager.registrarSesion(
                                                idPaciente,
                                                dispBluetoothNom2.getText().toString(), // Dispositivo 2 con ambas conectadas
                                                Intensidad,
                                                Duracion_Min2
                                        );
                                    } else {
                                        idSesion = dataManager.registrarSesion(
                                                idPaciente,
                                                dispBluetoothNom2.getText().toString(), // Dispositivo 2
                                                Intensidad,
                                                Duracion_Min2
                                        );
                                    }

                                    if (idSesion != -1) {
                                        Toast.makeText(MainActivity.this,
                                                "Sesión registrada correctamente",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("MainActivity", "Error al registrar la sesión");
                                    }
                                }
                            }

                            //  RESETEAR temporizador SOLO si es sesión nueva
                            minutotranscurrido2 = 0;
                        }

                        // Preparar datos a enviar (común para ambos casos)
                        index = 0;
                        miByteArrayWrite[index] = 0x41;
                        index++;

                        // Ancho de pulso (4ms)
                        AnchoPulso = 4;
                        auxint1 = AnchoPulso / 1000;
                        auxint2 = AnchoPulso % 1000;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 100;
                        auxint2 = auxint2 % 100;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 10;
                        auxint2 = auxint2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Repetir ancho de pulso
                        auxint1 = AnchoPulso / 1000;
                        auxint2 = AnchoPulso % 1000;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 100;
                        auxint2 = auxint2 % 100;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 10;
                        auxint2 = auxint2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Periodo (100ms = 10Hz)
                        Periodo_ms = 100;
                        auxint1 = Periodo_ms / 1000;
                        auxint2 = Periodo_ms % 1000;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 100;
                        auxint2 = auxint2 % 100;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 10;
                        auxint2 = auxint2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Repetir periodo
                        auxint1 = Periodo_ms / 1000;
                        auxint2 = Periodo_ms % 1000;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 100;
                        auxint2 = auxint2 % 100;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 / 10;
                        auxint2 = auxint2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Intensidad (ACTUALIZADA)
                        auxint1 = Intensidad / 10;
                        auxint2 = Intensidad % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Repetir intensidad
                        auxint1 = Intensidad / 10;
                        auxint2 = Intensidad % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        // Duración
                        auxint1 = Duracion_Min2 / 10;
                        auxint2 = Duracion_Min2 % 10;
                        auxint1 = auxint1 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;
                        auxint1 = auxint2 + 0x30;
                        miByteArrayWrite[index] = auxint1;
                        index++;

                        miByteArrayWrite[index] = 66; // "B"
                        index++;

                        // Enviar al dispositivo
                        for (int t = 0; t < index; t++) {
                            outputStream.write(miByteArrayWrite[t]);
                            outputStream.flush();
                            for (int s = 60000; s > 0; s--);
                        }

                        // ⭐ Iniciar timer SOLO si es sesión nueva
                        if (!sesionActiva) {
                            IsBattMon = true;
                            IndiceRec = 0;
                            IsClockStop2 = false;
                            N_Bytes = 0;
                            TimeHandler2.postDelayed(CheckTimer2, 20000);
                            Log.d("APP", "InicioThread2 connected");
                            mConnectedThread2 = new ConnectedThread2();
                            mConnectedThread2.start();
                            InicioPulsos2.setText("Actualizar");
                        }
                    }
                } catch (Exception createException) {
                    Log.d("APP", "Error: " + createException.getMessage());
                }
            }
        });

        FinPulsos = (Button) findViewById(R.id.button_FinPulsos);
        FinPulsos.setOnClickListener(new Button.OnClickListener() {
                                         public void onClick(View arg0) {

                                             try {

                                                 if (IsConnected3) {
                                                     InicioPulsos.setBackgroundColor(Color.BLUE);
                                                     miByteArrayWrite[0] = 0x43;//"C"
                                                     outputStream3.write(miByteArrayWrite[0]);
                                                     outputStream3.flush();
                                                     IsClockStop = true;



                                                     //  Restaurar botón a estado inicial
                                                     InicioPulsos.setText("INICIAR");
                                                     InicioPulsos.setBackgroundColor(Color.BLUE);
                                                     FinPulsos.setBackgroundColor(Color.RED);
                                                 }

                                             }catch (Exception createException) {
                                                 Log.d("APP", "no hay datos de fecha");
                                             }

                                         }

                                     }
        );

        FinPulsos2 = (Button) findViewById(R.id.button_FinPulsos2);
        FinPulsos2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    if (IsConnected) {
                        miByteArrayWrite[0] = 0x43;//"C"
                        outputStream.write(miByteArrayWrite[0]);
                        outputStream.flush();

                        IsClockStop2 = true;

                        //  Restaurar botón a estado inicial
                        InicioPulsos2.setText("Iniciar");
                        InicioPulsos2.setBackgroundColor(Color.BLUE);

                        FinPulsos2.setBackgroundColor(Color.RED);
                    }
                } catch (Exception createException) {
                    Log.d("APP", "no hay datos de fecha");
                }
            }
        });

        Disconnect = (Button) findViewById(R.id.buttonDisconnect);
        Disconnect.setOnClickListener(new Button.OnClickListener() {
                                          public void onClick(View arg0) {
                                              try {
                                                  //onDestroy();
                                                  if (IsConnected || IsConnected3){
                                                      mostrarDialogoParaDesconectar();
                                                  }else{
                                                      Toast.makeText(MainActivity.this, "No hay ningun Dispositivo conectado", Toast.LENGTH_SHORT).show();
                                                  }


                                              } catch (Exception createException) {
                                                  Log.d("APP", "no hay datos de fecha");
                                              }
                                          }

                                      }
        );
        //timer();
        //TimeHandler.postDelayed(CheckTimer, 20000);//IntervaloRefresco=progress;


        //Cambio Kerman y Aitor

        ConectarPaciente = (Button) findViewById(R.id.btnGuardarConf);
        ConectarPaciente.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opcionDispositivoInt == 3) {
                    mostrarDialogoParaGuardar2dispositivos();
                } else {
                    if (nombrePaciente != null && !nombrePaciente.isEmpty() && DNIpaciente != null && !DNIpaciente.isEmpty()||
                    nombrePaciente2 != null && !nombrePaciente2.isEmpty() && DNIpaciente2 != null && !DNIpaciente2.isEmpty()){
                        mostrarDialogoParaGuardar();
                    }else{
                        Toast.makeText(MainActivity.this, "No hay ningun paciente conectado a los dispositivos", Toast.LENGTH_SHORT);
                    }


                }
            }
        });

        nombreDispositivoPac1 = findViewById(R.id.pacienteDispo1);
        nombreDispositivoPac2 = findViewById(R.id.pacienteDispo2);


        ventanaPaciente = (Button) findViewById(R.id.ir_ven_Paciente);
        ventanaPaciente.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ventanaPaciente.class);
                mostrarDialogoSeleccionarPacienteADispositivo(intent);

                /*
                //Es mejor poner la opcion de guardar inicie despues del conectar, ya que el alert dalog que te deja intercatuar antes es el ultimo en salir
                if (opcionDispositivoInt == 3) {
                    mostrarDialogoParaGuardar2dispositivos();
                } else {
                    if (nombrePaciente != null && !nombrePaciente.isEmpty() && DNIpaciente != null && !DNIpaciente.isEmpty()||
                            nombrePaciente2 != null && !nombrePaciente2.isEmpty() && DNIpaciente2 != null && !DNIpaciente2.isEmpty()){
                        mostrarDialogoParaGuardar();
                    }else{
                        Toast.makeText(MainActivity.this, "No hay ningun paciente conectado a los dispositivos", Toast.LENGTH_SHORT);
                    }


                }*/

            }
        });

        btnVerLista = findViewById(R.id.verLista);
        btnVerLista.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if (opcionDispositivoInt == 3) {
                    mostrarDialogoParaGuardar2dispositivos();
                } else {
                    if (nombrePaciente != null && !nombrePaciente.isEmpty() && DNIpaciente != null && !DNIpaciente.isEmpty()||
                            nombrePaciente2 != null && !nombrePaciente2.isEmpty() && DNIpaciente2 != null && !DNIpaciente2.isEmpty()){
                        mostrarDialogoParaGuardar();
                    }else{
                        Toast.makeText(MainActivity.this, "No hay ningun paciente conectado a los dispositivos", Toast.LENGTH_SHORT);
                    }


                }*/

                Intent intent = new Intent(MainActivity.this, ventanaPaciente.class);

                intent.putExtra("verSoloLista", true);
                startActivityForResult(intent, PACIENTE_REQUEST_CODE);
            }
        });


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bluetoothDisconnectReceiver, filter);



        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
    }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Agregar opción de cerrar sesión en el menú
         menu.add(0, 1, 0, "Cerrar Sesión");
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         if (item.getItemId() == 1) {
             logout();
             return true;
         }
         return super.onOptionsItemSelected(item);
     }

     private void logout() {
         // Limpiar estado de login
         SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putBoolean("isLoggedIn", false);
         editor.apply();

         // Volver a LoginActivity
         Intent intent = new Intent(MainActivity.this, LoginActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);
         finish();
     }

    private void checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 o superior - necesita permiso "All files access"
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, ALL_FILES_ACCESS_CODE);
                    Toast.makeText(this, "Por favor, concede permiso para gestionar todos los archivos", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, ALL_FILES_ACCESS_CODE);
                }
            }
        } else {
            // Android 10 o anterior
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de almacenamiento concedido (Android 10 o anterior)
                Toast.makeText(this, "Permiso de almacenamiento concedido", Toast.LENGTH_SHORT).show();
                // Continuar con la inicialización normal de la app
            } else {
                Toast.makeText(this,
                        "Se requiere permiso para acceder al almacenamiento",
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == BLUETOOTH_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos Bluetooth concedidos", Toast.LENGTH_SHORT).show();
                // Puedes inicializar Bluetooth aquí
            } else {
                Toast.makeText(this,
                        "Se requieren permisos Bluetooth para conectar con dispositivos",
                        Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ALL_FILES_ACCESS_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Permiso concedido en Android 11+
                    Toast.makeText(this, "Permiso concedido para gestionar archivos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "Se requiere permiso para acceder a todos los archivos",
                            Toast.LENGTH_LONG).show();
                }
            }
        }


        // Verificar que es el resultado de ventanaPaciente
        if (requestCode == PACIENTE_REQUEST_CODE) {
            // Solo procesar si el resultado fue OK (RESULT_OK)
            if (resultCode == RESULT_OK && data != null) {
                // Extraer datos del intent
                int intensidadPaciente = data.getIntExtra("INTENSIDAD", 0);
                int tiempoPaciente = data.getIntExtra("TIEMPO", 0);
                String nombrePacienteNuevo = data.getStringExtra("NOMBRE_PACIENTE");
                String dniPacienteNuevo = data.getStringExtra("DNI_PACIENTE");
                int opcionDispositivoNuevo = data.getIntExtra("DISPOSITIVO_ELEC", 0);

                int intensidadPaciente2 = data.getIntExtra("INTENSIDAD2", 0);
                int tiempoPaciente2 = data.getIntExtra("TIEMPO2", 0);

                // Actualizar campos en MainActivity
                if (intensidadPaciente > 0) {
                    if (Param3 != null && opcionDispositivoNuevo == 1) {
                        Param3.setText(String.valueOf(intensidadPaciente));
                        opcionDispositivoInt = opcionDispositivoNuevo;
                    } else if (Param10 != null && opcionDispositivoNuevo == 2) {
                        Param10.setText(String.valueOf(intensidadPaciente));
                        opcionDispositivoInt = opcionDispositivoNuevo;

                    } else if (Param3 != null && Param10 != null && opcionDispositivoNuevo == 3) {
                        Param3.setText(String.valueOf(intensidadPaciente));
                        Param10.setText(String.valueOf(intensidadPaciente2));
                        opcionDispositivoInt = opcionDispositivoNuevo;

                    }
                }

                if (tiempoPaciente > 0) {
                    if (Param4 != null && opcionDispositivoNuevo == 1) {
                        Param4.setText(String.valueOf(tiempoPaciente));
                        Param5.setText(String.valueOf(tiempoPaciente)); // Tiempo restante
                    }
                    if (Param12 != null && opcionDispositivoNuevo == 2) {
                        Param12.setText(String.valueOf(tiempoPaciente));
                        Param13.setText(String.valueOf(tiempoPaciente)); // Tiempo restante
                    }
                    if (Param4 != null && Param12 != null && opcionDispositivoNuevo == 3) {
                        Param4.setText(String.valueOf(tiempoPaciente));
                        Param5.setText(String.valueOf(tiempoPaciente)); // Tiempo restante

                        Param12.setText(String.valueOf(tiempoPaciente2));
                        Param13.setText(String.valueOf(tiempoPaciente2)); // Tiempo restante

                    }
                }

                // Actualizar nombre y DNI del paciente
                if (nombrePacienteNuevo != null && !nombrePacienteNuevo.isEmpty()) {

                    //pacienteSeleccionado.setText(nombrePaciente);

                    if (opcionDispositivoNuevo == 1 || opcionDispositivoNuevo == 3) {
                        nombrePaciente = nombrePacienteNuevo;
                        nombreDispositivoPac1.setText(nombrePacienteNuevo);
                        nombreDispositivoPac1.setVisibility(View.VISIBLE);
                    }
                    if (opcionDispositivoNuevo == 2 || opcionDispositivoNuevo == 3) {
                        nombrePaciente2 = nombrePacienteNuevo;
                        nombreDispositivoPac2.setText(nombrePacienteNuevo);
                        nombreDispositivoPac2.setVisibility(View.VISIBLE);
                    }
                }

                if (dniPacienteNuevo != null && !dniPacienteNuevo.isEmpty()) {

                    if (opcionDispositivoNuevo == 1 || opcionDispositivoNuevo == 3) {
                        DNIpaciente = dniPacienteNuevo;
                    }
                    if (opcionDispositivoNuevo == 2 || opcionDispositivoNuevo == 3) {
                        DNIpaciente2 = dniPacienteNuevo;
                    }

                    // Mostrar el botón guardar configuración si hay DNI
                    ConectarPaciente.setVisibility(View.VISIBLE);
                }

                // Actualizar opción de dispositivo
                if (opcionDispositivoNuevo > 0) {
                    opcionDispositivoInt = opcionDispositivoNuevo;
                }

                // Mostrar mensaje de éxito
                Toast.makeText(MainActivity.this,
                        "Información del paciente actualizada",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        // Opcional: puedes enviar de vuelta los datos aunque no se haya seleccionado un paciente
        // o simplemente cancelar
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
//hecho Kerman y Aitor

    private void mostrarDialogoSeleccionarPacienteADispositivo(Intent intent) {
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("A que dispositivo conectar el Paciente?");
        builder.setPositiveButton("Dispositivo1", (dialog, which) -> {
            if (opcionDispositivoInt == 3) {
                DNIpaciente2 = "";
                nombreDispositivoPac2.setVisibility(View.GONE);
                nombrePaciente2 = "";
                Param10.setText("10");
                Param12.setText("30");
                Param13.setText("30");
            }

            //opcionDispositivoInt = 1;
            if (Param3 != null) {
                intent.putExtra("INTENSIDAD", Param3.getText());
            }
            if (Param4 != null) {
                intent.putExtra("TIEMPO", Param4.getText());
            }


            intent.putExtra("NOMBRE_PACIENTE", nombrePaciente);
            intent.putExtra("DNI_PACIENTE", DNIpaciente);
            intent.putExtra("DISPOSITIVO_ELEC", 1);

            if (DNIpaciente2 != null && !DNIpaciente2.isEmpty()) {
                intent.putExtra("DNI_PAC_OTRODISP", DNIpaciente2);
            }

            // Iniciar actividad esperando resultado
            startActivityForResult(intent, PACIENTE_REQUEST_CODE);
        });

        builder.setNegativeButton("Dispositivo2", (dialog, which) -> {
            if (opcionDispositivoInt == 3) {
                DNIpaciente = "";
                nombreDispositivoPac1.setVisibility(View.GONE);
                nombrePaciente = "";
                Param3.setText("10");
                Param4.setText("30");
                Param5.setText("30");
            }
            //opcionDispositivoInt = 2;
            if (Param10 != null) {
                intent.putExtra("INTENSIDAD", Param10.getText());
            }
            if (Param12 != null) {
                intent.putExtra("TIEMPO", Param12.getText());
            }


            intent.putExtra("NOMBRE_PACIENTE", nombrePaciente2);
            intent.putExtra("DNI_PACIENTE", DNIpaciente2);
            intent.putExtra("DISPOSITIVO_ELEC", 2);


            if (DNIpaciente != null && !DNIpaciente.isEmpty()) {
                intent.putExtra("DNI_PAC_OTRODISP", DNIpaciente);
            }

            // Iniciar actividad esperando resultado
            startActivityForResult(intent, PACIENTE_REQUEST_CODE);

        });
        builder.setNeutralButton("Todos los dispositivos", (dialog, which) -> {
            //opcionDispositivoInt = 3; //esto despues causa problemas

            if (Param3 != null) {
                intent.putExtra("INTENSIDAD", Param3.getText());
            }
            if (Param4 != null) {
                intent.putExtra("TIEMPO", Param4.getText());
            }

            if (nombrePaciente != null && !nombrePaciente.isEmpty() &&
                    DNIpaciente != null && !DNIpaciente.isEmpty()) {
                intent.putExtra("NOMBRE_PACIENTE", nombrePaciente);
                intent.putExtra("DNI_PACIENTE", DNIpaciente);
            }

            if (nombrePaciente2 != null && !nombrePaciente2.isEmpty() &&
                    DNIpaciente2 != null && !DNIpaciente2.isEmpty()) {
                intent.putExtra("NOMBRE_PACIENTE", nombrePaciente2);
                intent.putExtra("DNI_PACIENTE", DNIpaciente2);


            }
            intent.putExtra("DISPOSITIVO_ELEC", 3);
            // Iniciar actividad esperando resultado
            startActivityForResult(intent, PACIENTE_REQUEST_CODE);
        });

        builder.create().show();
    }


    private void mostrarDialogoParaGuardar() {
        String[] opcionDispositivo = new String[2];

        if (nombrePaciente != null && !nombrePaciente.isEmpty() && DNIpaciente != null && !DNIpaciente.isEmpty()
                && nombrePaciente2 != null && !nombrePaciente2.isEmpty() && DNIpaciente2 != null && !DNIpaciente2.isEmpty()){
            opcionDispositivo[0] = "Dispositivo 1";
            opcionDispositivo[1] = "Dispositivo 2";
        }else if (nombrePaciente != null && !nombrePaciente.isEmpty() && DNIpaciente != null && !DNIpaciente.isEmpty()){
            opcionDispositivo = new String[1];
            opcionDispositivo[0] = "Dispositivo 1";
        }else if (nombrePaciente2 != null && !nombrePaciente2.isEmpty() && DNIpaciente2 != null && !DNIpaciente2.isEmpty()){
            opcionDispositivo = new String[1];
            opcionDispositivo[0] = "Dispositivo 2";
        }

        LayoutInflater inflater = getLayoutInflater();


        View dialogView = inflater.inflate(R.layout.dialog_spinner, null);

        //configurar Spinner

        Spinner spinnerDispositivo = dialogView.findViewById(R.id.spinnerOpciones);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, opcionDispositivo);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDispositivo.setAdapter(adapterSpinner);

        spinnerDispositivo.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Desea Guardar la nueva configuracion?");

        builder.setView(dialogView); //si no quieres que aparezca el spinner sin tener que desaparecer el spinnerDispositivo, solo desaparece este cacho

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            String opcion = spinnerDispositivo.getSelectedItem().toString();
            guardarConfigDispositivo(opcion);


        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());


        builder.create().show();
    }

    private void mostrarDialogoParaDesconectar() {
        String[] opcionDispositivo = new String[2];

        if (IsConnected3 && IsConnected){
            opcionDispositivo[0] = "Dispositivo 1";
            opcionDispositivo[1] = "Dispositivo 2";
        }else if (IsConnected3){
            opcionDispositivo = new String[1];
            opcionDispositivo[0] = "Dispositivo 1";
        }else if (IsConnected){
            opcionDispositivo = new String[1];
            opcionDispositivo[0] = "Dispositivo 2";
        }

        LayoutInflater inflater = getLayoutInflater();


        View dialogView = inflater.inflate(R.layout.dialog_spinner, null);

        //configurar Spinner

        Spinner spinnerDispositivo = dialogView.findViewById(R.id.spinnerOpciones);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, opcionDispositivo);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDispositivo.setAdapter(adapterSpinner);

        spinnerDispositivo.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Que dispositivo desea desconectar?");

        builder.setView(dialogView); //si no quieres que aparezca el spinner sin tener que desaparecer el spinnerDispositivo, solo desaparece este cacho

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            String opcion = spinnerDispositivo.getSelectedItem().toString();

            switch (opcion){
                case "Dispositivo 1":
                    try {
                        if (IsConnected3) {
                            if (!IsClockStop) {
                                InicioPulsos.setBackgroundColor(Color.LTGRAY);
                                miByteArrayWrite[0] = 0x43;
                                outputStream3.write(miByteArrayWrite[0]);
                                outputStream3.flush();
                                IsClockStop = true;
                            }

                            btSocket3.close();
                            dispBluetoothNom1.setText("");
                            dispBluetoothNom1.setVisibility(View.GONE);

                            IsConnected3 = false;

                            Conexion.setBackgroundColor(Color.BLUE);
                            Conexion.setTextColor(Color.WHITE);
                            Conexion.setText("Conectar");


                            InicioPulsos.setEnabled(false);
                            InicioPulsos.setBackgroundColor(Color.LTGRAY);
                            InicioPulsos.setText("Iniciar");
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "Error al Intentar Desconectar", Toast.LENGTH_LONG).show();
                    }


                    break;

                case "Dispositivo 2":
                    try {
                        if (IsConnected) {
                            if (!IsClockStop2) {
                                InicioPulsos2.setBackgroundColor(Color.LTGRAY);
                                miByteArrayWrite[0] = 0x43;
                                outputStream.write(miByteArrayWrite[0]);
                                outputStream.flush();
                                IsClockStop2 = true;
                                //mConnectedThread=null;
                            }

                            btSocket.close();
                            dispBluetoothNom2.setText("");
                            dispBluetoothNom2.setVisibility(View.GONE);

                            IsConnected = false;

                            Conexion2.setBackgroundColor(Color.BLUE);
                            Conexion2.setTextColor(Color.WHITE);
                            Conexion2.setText("Conectar");

                            InicioPulsos2.setEnabled(false);
                            InicioPulsos2.setBackgroundColor(Color.LTGRAY);
                            InicioPulsos2.setText("Iniciar");

                        }
                    }
                    catch(IOException closeException)
                    {
                        Toast.makeText(this,"Error al Intentar Desconectar",Toast.LENGTH_LONG).show();
                    }
                    break;

                default:
                    try
                    {
                        if(IsConnected)
                            btSocket.close();
                        if(IsConnected2)
                            btSocket2.close();
                        if(IsConnected3)
                            btSocket3.close();
                    }
                    catch(IOException closeException)
                    {

                    }
                    break;
            }

        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());


        builder.create().show();
    }

    private void guardarConfigDispositivo(String opcion) {

        //base de datos
        PacienteDataManager dataManager;
        //Inicializar el gestor de datos
        dataManager = new PacienteDataManager(this);
        boolean dbOpened = dataManager.open();
        if (!dbOpened) {
            Toast.makeText(this, "Error al abrir la base de datos", Toast.LENGTH_LONG).show();
        }

        int intensidad = 0;
        int tiempo = 0;


        int resultado = -1;
        switch (opcion) {
            case "Dispositivo 1":
                try {
                    intensidad = Integer.parseInt(Param3.getText().toString().trim());
                    if (Param4 != null) {
                        tiempo = Integer.parseInt(Param4.getText().toString().trim());
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(this, "La intensidad y el tiempo deben ser números", Toast.LENGTH_SHORT).show();
                    return;
                } finally {
                    resultado = dataManager.guardarConfiguracion(DNIpaciente, intensidad, tiempo);
                    if (dataManager != null) {
                        dataManager.close();
                    }
                }

                break;

            case "Dispositivo 2":
                try {
                    intensidad = Integer.parseInt(Param10.getText().toString().trim());
                    if (Param12 != null) {
                        tiempo = Integer.parseInt(Param12.getText().toString().trim());
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(this, "La intensidad y el tiempo deben ser números", Toast.LENGTH_SHORT).show();
                    return;
                } finally {
                    resultado = dataManager.guardarConfiguracion(DNIpaciente2, intensidad, tiempo);
                    if (dataManager != null) {
                        dataManager.close();
                    }
                }

                break;
            default:

                break;
        }


        if (resultado != -1) {
            Toast.makeText(MainActivity.this, "Se ha Guardado La nueva configuracion", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Error Al guardar La configuracion", Toast.LENGTH_SHORT).show();
        }

        dataManager.close();
    }


    private void mostrarDialogoParaGuardar2dispositivos() {
        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desea Guardar la nueva configuracion?");

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            //base de datos
            PacienteDataManager dataManager;
            //Inicializar el gestor de datos
            dataManager = new PacienteDataManager(this);
            boolean dbOpened = dataManager.open();
            if (!dbOpened) {
                Toast.makeText(this, "Error al abrir la base de datos", Toast.LENGTH_LONG).show();
            }

            int intensidad, intensidad2 = 0;
            int tiempo = 0;
            int tiempo2 = 0;

            try {
                intensidad = Integer.parseInt(Param3.getText().toString().trim());
                intensidad2 = Integer.parseInt(Param10.getText().toString().trim());
                if (Param4 != null) {
                    tiempo = Integer.parseInt(Param4.getText().toString().trim());
                }
                if (Param12 != null) {
                    tiempo2 = Integer.parseInt(Param12.getText().toString().trim());
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "La intensidad y el tiempo deben ser números", Toast.LENGTH_SHORT).show();
                return;
            }
            int resultado = dataManager.guardarConfiguracion2disp(DNIpaciente, intensidad, tiempo, intensidad2, tiempo2);
            if (resultado != -1) {

                Toast.makeText(MainActivity.this, "Se ha Guardado La nueva configuracion", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Error Al guardar La configuracion", Toast.LENGTH_SHORT).show();
            }

            dataManager.close();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());


        builder.create().show();

    }

    private void mostrarDialogoListaBluetooth(int dispositivoNum) {

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        ArrayList<String> deviceList = new ArrayList<>();
        ArrayList<BluetoothDevice> listaDevice = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            // Hay dispositivos vinculados
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                if(dispositivoNum == 1 && IsConnected){
                    if (!btDevice.equals(device)){
                        deviceList.add(deviceName + "\n" + deviceAddress);
                        listaDevice.add(device);
                    }
                }else if(dispositivoNum == 2 && IsConnected3){
                    if (!btDevice3.equals(device)){
                        deviceList.add(deviceName + "\n" + deviceAddress);
                        listaDevice.add(device);
                    }
                }else if (!IsConnected && !IsConnected3){
                    deviceList.add(deviceName + "\n" + deviceAddress);
                    listaDevice.add(device);
                }

            }
        } else {
            deviceList.add("No hay dispositivos vinculados");
        }

        // Inflar la vista del diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bluetoothoption_dialog, null);
        ListView bluetoothDevicesList = dialogView.findViewById(R.id.listaBluetotoh);

        // Crear adaptador para el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                deviceList
        );

        // Asignar adaptador al ListView
        bluetoothDevicesList.setAdapter(adapter);

        // Crear y mostrar el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Dispositivos Bluetooth");

        AlertDialog dialogInterface = builder.setView(dialogView)
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();


        bluetoothDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Cerrar el diálogo
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }

                conectarDevice(listaDevice.get(i), dispositivoNum);

            }
        });

        dialogInterface.show();


    }

    private void conectarDevice(BluetoothDevice device, int dispositivoNum){ // dispositivoNum solo puede ser 1 (dispositibo 1) o 2 (dispositivo2)

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        if (dispositivoNum == 1){
            if (!IsConnected3) {
                btDevice3 = device;

                try {
                    btSocket3 = btDevice3.createRfcommSocketToServiceRecord(uuid);
                } catch (Exception createException) {
                    toastText = "Socket create failed: " + createException.getMessage();
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                }
                try {
                    address3 = btDevice3.getAddress();
                    NameDevice3 = btDevice3.getName();
                    btSocket3.connect();
                    IsConnected3 = true;
                    IsBattMon3 = true;
                    //N_Deviceconnected1=ndevices;
                    Conexion.setBackgroundColor(Color.YELLOW);
                    Conexion.setTextColor(Color.BLACK);
                    Conexion.setText("conectado");
                    InicioPulsos.setEnabled(true);

                    String nomBlueDis = device.getName() + "(" + device.getAddress().substring(device.getAddress().length() -5, device.getAddress().length()) +")";

                    dispBluetoothNom1.setText(nomBlueDis);
                    dispBluetoothNom1.setVisibility(View.VISIBLE);

                    //ndevices=indiceDirMACs;
                    outputStream3 = btSocket3.getOutputStream();
                    mInputStream3 = btSocket3.getInputStream();

                } catch (Exception createException) {
                    Conexion.setBackgroundColor(ventanaPaciente.getHighlightColor());
                    Conexion.setTextColor(Color.WHITE);
                    Conexion.setText("Conectar");


                    Toast.makeText(MainActivity.this, "Fallo al conectarse", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (dispositivoNum == 2){
            if(!IsConnected)
            {
                btDevice = device;

                try {
                    btSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                } catch (Exception createException) {
                    toastText = "Socket create failed: " + createException.getMessage();
                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                }
                try {
                    address = btDevice.getAddress();
                    NameDevice = btDevice.getName();
                    btSocket.connect();
                    IsConnected = true;
                    IsBattMon = true;
                    Conexion2.setBackgroundColor(Color.YELLOW);
                    Conexion2.setTextColor(Color.BLACK);
                    Conexion2.setText("conectado");
                    InicioPulsos2.setEnabled(true);

                    String nomBlueDis2 = device.getName() + "(" + device.getAddress().substring(device.getAddress().length() -5, device.getAddress().length()) +")";

                    dispBluetoothNom2.setText(nomBlueDis2);
                    dispBluetoothNom2.setVisibility(View.VISIBLE);

                    //ndevices=indiceDirMACs;
                    outputStream = btSocket.getOutputStream();
                    mInputStream = btSocket.getInputStream();

                } catch (Exception createException) {

                    Conexion2.setBackgroundColor(ventanaPaciente.getHighlightColor());
                    Conexion2.setTextColor(Color.WHITE);
                    Conexion2.setText("Conectar");
                    Toast.makeText(MainActivity.this, "Fallo al conectarse", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

// Método controlar valor  campos de intensidad

    private void setupRangeValidation(final EditText editText, final int min, final int max, final String fieldName) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateField(editText, min, max, fieldName);
                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_NEXT ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    validateField(editText, min, max, fieldName);
                    return false; // Dejar que el sistema maneje la acción (siguiente campo, etc.)
                }
                return false;
            }
        });
    }


    private void validateField(EditText editText, int min, int max, String fieldName) {
        String text = editText.getText().toString().trim();

        if (text.isEmpty()) {
            editText.setText(String.valueOf(min));
            Toast.makeText(MainActivity.this,
                    "La " + fieldName + " debe estar entre " + min + " y " + max,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int value = Integer.parseInt(text);

            if (value < min) {
                editText.setText(String.valueOf(min));
                Toast.makeText(MainActivity.this,
                        "La " + fieldName + " mínima es " + min,
                        Toast.LENGTH_SHORT).show();
            } else if (value > max) {
                editText.setText(String.valueOf(max));
                Toast.makeText(MainActivity.this,
                        "La " + fieldName + " máxima es " + max,
                        Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            editText.setText(String.valueOf(min));
            Toast.makeText(MainActivity.this,
                    "Valor no válido, se ha establecido a " + min,
                    Toast.LENGTH_SHORT).show();
        }
    }











    //termina

    public void abrirfile( )
    {

        //

        try
        {

            InputStream fileinputStream = getResources().openRawResource(R.raw.dir_macs);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileinputStream));
            String texto = reader.readLine();
            Log.d("APP","linea leida :"+texto);
            indiceDirMACs=0;
            while (texto != null) {
                // Procesa el texto aquí
                DirMacs[indiceDirMACs]=texto;
                indiceDirMACs++;
                texto = reader.readLine();
                Log.d("APP","linea leida :"+texto);
            }
            Log.d("APP","Fin lectura :");
            reader.close();
            fileinputStream.close();
        }
        catch (IOException e){
            Log.d("APP","Archivo NO leido :");
        }
    }


    public Handler TimeHandler = new Handler();
    public Handler TimeHandler2 = new Handler();

    public Runnable CheckTimer=new Runnable() {
        @Override
        public void run()
        {
            Calendar calendar = Calendar.getInstance();
            minutoAct = calendar.get(Calendar.MINUTE);
            Log.d("APP", "Minuto Actual:"+minutoAct);
            if(minutoAct!=minutoAnt)
            {
                if(!IsClockStop)
                {
                    minutotranscurrido++;
                    valor=Duracion_Min-minutotranscurrido;
                    Param5.setText(String.valueOf(valor));
                    if(minutotranscurrido%5==0)
                    {
                        miByteArrayWrite[0]=0x46;//"C"
                        try
                        {
                            if(IsConnected3)
                            {
                                outputStream3.write(miByteArrayWrite[0]);
                                outputStream3.flush();
                                IsBattMon3=true;
                            }
                            IsClockStop=false;
                            N_Bytes=0;
                            mConnectedThread=new ConnectedThread();
                            mConnectedThread.start();
                        }
                        catch(Exception createException)
                        {
                            Log.d("APP","no hay conexion");
                        }


                    }
                    if(minutotranscurrido==Duracion_Min)
                        IsClockStop=true;
                }
            }
            minutoAnt=minutoAct;
            TimeHandler.postDelayed(CheckTimer, 20000);

        }
    };


    public Runnable CheckTimer2=new Runnable() {
        @Override
        public void run()
        {
            Calendar calendar = Calendar.getInstance();
            minutoAct2 = calendar.get(Calendar.MINUTE);
            Log.d("APP", "Minuto Actual2:"+minutoAct2);
            if(minutoAct2!=minutoAnt2)
            {
                if(!IsClockStop2)
                {
                    minutotranscurrido2++;
                    valor=Duracion_Min2-minutotranscurrido2;
                    Param13.setText(String.valueOf(valor));

                    if(minutotranscurrido2%5==0)
                    {
                        miByteArrayWrite[0]=0x46;//"C"
                        try
                        {
                            if(IsConnected)
                            {
                                outputStream.write(miByteArrayWrite[0]);
                                outputStream.flush();
                                IsBattMon=true;
                            }

                            IsClockStop2=false;
                            N_Bytes=0;
                            mConnectedThread2=new ConnectedThread2();
                            mConnectedThread2.start();
                        }
                        catch(Exception createException)
                        {
                            Log.d("APP","no hay conexion");
                        }


                    }
                    if(minutotranscurrido2==Duracion_Min2)
                        IsClockStop2=true;
                }
            }
            minutoAnt2=minutoAct2;
            TimeHandler2.postDelayed(CheckTimer2, 20000);

        }
    };


    /*
     public void timer()
     {

         new Thread(new Runnable()
         {
             @Override
             public void run()
             {
                 while (IsDualdeviceON)
                 {
                     try
                     {
                         Thread.sleep(20000);
                         mHandler.post(new Runnable()
                         {
                             @Override
                             public void run()
                             {
                                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                 {
                                     now = now();
                                     zdt = now.atZone(ZoneId.systemDefault());
                                     minutoAct = zdt.getMinute();
                                     Log.d("APP", "Minuto Actual:"+minutoAct);
                                     if(minutoAct!=minutoAnt &&IsDualdeviceON==true)
                                     {
                                         minutotranscurrido++;
                                         valor=Duracion_Min-minutotranscurrido;
                                         Param5.setText(String.valueOf(valor));
                                         if(minutotranscurrido==Duracion_Min)
                                             IsDualdeviceON=false;
                                         Log.d("APP", "Minuto Transcurrido:"+minutotranscurrido);
                                     }
                                     minutoAnt=minutoAct;
                                 }
                                 else
                                 {
                                     //Date currentTime = Calendar.getInstance().getTime();

                                     Calendar calendar = Calendar.getInstance();
                                     minutoAct = calendar.get(Calendar.MINUTE);
                                     //Date currentTime = Calendar.getInstance().getTime();
                                     //String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                                     Log.d("APP", "Minuto Actual:"+minutoAct);
                                     if(minutoAct!=minutoAnt &&IsDualdeviceON==true)
                                     {
                                         if(IsClockStop==false)
                                         {
                                             minutotranscurrido++;
                                             valor=Duracion_Min-minutotranscurrido;
                                             Param5.setText(String.valueOf(valor));
                                             if(minutotranscurrido==Duracion_Min)
                                                 IsClockStop=false;

                                         }

                                         Log.d("APP", "Minuto Transcurrido:"+minutotranscurrido);
                                     }
                                     minutoAnt=minutoAct;
                                 }
                             }

                         });

                         }
                     catch (Exception e)
                     {

                     }
                 }

             }
         }).start();
     }
 */
    @Override
    public void onDestroy()
    {
        try {
            unregisterReceiver(bluetoothDisconnectReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try
        {
            if(IsConnected)
                btSocket.close();
            if(IsConnected2)
                btSocket2.close();
            if(IsConnected3)
                btSocket3.close();

            if (dataManager != null) {
                dataManager.close();
            }

        }

        catch(IOException closeException)

        {

        }
        this.finish();
        super.onDestroy();


    }
    @SuppressLint("MissingPermission")
    public void connectToDevice() {
        int ndevices;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        //MAC_BT="20:19:07:08:06:45";// "20:19:07:08:06:45" es el MAC del stim8 (1)
        /*
        if(Param5.getText().toString()!="")
            MAC_BT=Param5.getText().toString();//20:17:09:14:25:90
        if(Param6.getText().toString()!="")
            MAC_BT2=Param6.getText().toString();//20:17:09:14:25:90
        */
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice pairedDevice : pairedDevices) {
                //if (pairedDevice.getAddress().equals(MAC_BT))//dual ohiana "20:17:09:14:23:31"
                //trivium 1 : "20:19:06:20:04:90" opcion || .equals(MAC_BT))
                //Test20:19:06:20:04:87//Ohiana: 2 98:D3:31:20:7D:D8//casa "20:17:09:14:23:31"/gorka trivium: "20:19:07:08:06:58"
                //"20:17:09:14:23:31 es el dual no Trivium de Gorka ( OJO no arranca bien y cambio)
                Log.d("APP", "Vinculados:"+ indiceDirMACs);

                for(ndevices=0;ndevices<indiceDirMACs;ndevices++)
                {
                    if(ndevices!=N_Deviceconnected2)
                    {
                        Log.d("APP", "direcciones MAC:"+ DirMacs[ndevices]);
                        if (pairedDevice.getAddress().equals(DirMacs[ndevices]))
                        {
                            if (!IsConnected3) {
                                btDevice3 = pairedDevice;
                                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                                try {
                                    btSocket3 = btDevice3.createRfcommSocketToServiceRecord(uuid);
                                } catch (Exception createException) {
                                    toastText = "Socket create failed: " + createException.getMessage();
                                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    address3 = btDevice3.getAddress();
                                    NameDevice3 = btDevice3.getName();
                                    btSocket3.connect();
                                    IsConnected3 = true;
                                    IsBattMon3 = true;
                                    N_Deviceconnected1=ndevices;
                                    Conexion.setBackgroundColor(Color.YELLOW);
                                    Conexion.setTextColor(Color.BLACK);
                                    Conexion.setText("conectado");
                                    ndevices=indiceDirMACs;
                                    outputStream3 = btSocket3.getOutputStream();
                                    mInputStream3 = btSocket3.getInputStream();
                                    break;
                                } catch (Exception createException) {

                                    toastText = "Introduzca MAC y reintentalo ";
                                    //Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }
            //if (pairedDevice.getAddress().equals("20:17:09:14:22:99"))//Test98:D3:32:10:A4:18//Ohiana 1: 20:19:07:08:06:93//casa: "20:17:09:14:22:99"
            //if (pairedDevice.getAddress().equals(MAC_BT2))//"98:D3:32:10:A4:18"=CaeSIM...
            //if (pairedDevice.getAddress().equals("98:D3:31:20:7D:D8"))//Test20:19:06:20:04:87//Ohiana: 2 98:D3:31:20:7D:D8//casa Gomaeva: 98:D3:32:10:A4:18

            if(IsConnected3)
            {
                toastText = "Conectado a Trivium1: "+address3;
            }

            else if(!IsConnected3)
            {
                toastText = "NO ha habido conexion ";
            }

            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
        }

    }
    @SuppressLint("MissingPermission")
    public void connectToDevice2() {
        int ndevices;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        /*
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TO ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        */

        @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        //MAC_BT="20:19:07:08:06:45";// "20:19:07:08:06:45" es el MAC del stim8 (1)
        /*
        if(Param5.getText().toString()!="")
            MAC_BT=Param5.getText().toString();//20:17:09:14:25:90
        if(Param6.getText().toString()!="")
            MAC_BT2=Param6.getText().toString();//20:17:09:14:25:90
        */
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice pairedDevice : pairedDevices) {
                //if (pairedDevice.getAddress().equals(MAC_BT))//dual ohiana "20:17:09:14:23:31"
                //trivium 1 : "20:19:06:20:04:90" opcion || .equals(MAC_BT))
                //Test20:19:06:20:04:87//Ohiana: 2 98:D3:31:20:7D:D8//casa "20:17:09:14:23:31"/gorka trivium: "20:19:07:08:06:58"
                //"20:17:09:14:23:31 es el dual no Trivium de Gorka ( OJO no arranca bien y cambio)

                Log.d("APP", "Vinculados:"+ indiceDirMACs);

                for(ndevices=0;ndevices<indiceDirMACs;ndevices++)
                {
                    if(ndevices!=N_Deviceconnected1)
                    {
                        Log.d("APP", "direcciones MAC:"+ DirMacs[ndevices]);
                        if (pairedDevice.getAddress().equals(DirMacs[ndevices]))
                        {
                            if(!IsConnected)
                            {
                                btDevice = pairedDevice;
                                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                                try {
                                    btSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                                } catch (Exception createException) {
                                    toastText = "Socket create failed: " + createException.getMessage();
                                    Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    address = btDevice.getAddress();
                                    NameDevice = btDevice.getName();
                                    btSocket.connect();
                                    IsConnected = true;
                                    IsBattMon = true;
                                    Conexion2.setBackgroundColor(Color.YELLOW);
                                    Conexion2.setTextColor(Color.BLACK);
                                    Conexion2.setText("conectado");
                                    ndevices=indiceDirMACs;
                                    outputStream = btSocket.getOutputStream();
                                    mInputStream = btSocket.getInputStream();
                                    break;
                                } catch (Exception createException) {

                                    toastText = "Introduzca MAC y reintentalo ";
                                    //Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                }
            }
            //if (pairedDevice.getAddress().equals("20:17:09:14:22:99"))//Test98:D3:32:10:A4:18//Ohiana 1: 20:19:07:08:06:93//casa: "20:17:09:14:22:99"
            //if (pairedDevice.getAddress().equals(MAC_BT2))//"98:D3:32:10:A4:18"=CaeSIM...
            //if (pairedDevice.getAddress().equals("98:D3:31:20:7D:D8"))//Test20:19:06:20:04:87//Ohiana: 2 98:D3:31:20:7D:D8//casa Gomaeva: 98:D3:32:10:A4:18

            if(IsConnected)
            {
                toastText = "Conectado a Trivium_2: "+address;
            }

            else if(!IsConnected)
            {
                toastText = "NO ha habido conexion ";
            }

            Toast.makeText(MainActivity.this, toastText, Toast.LENGTH_SHORT).show();
        }

    }

     private final BroadcastReceiver bluetoothDisconnectReceiver = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             String action = intent.getAction();
             if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                 if (device != null) {
                     String mac = device.getAddress();

                     // Dispositivo 1 (usa address3 y btSocket3)
                     if (mac.equals(address3)) {
                         if (mConnectedThread != null) {
                             mConnectedThread.cancel();
                             mConnectedThread = null;

                         }
                         IsConnected3 = false;
                         runOnUiThread(() -> {
                             Conexion.setBackgroundColor(ventanaPaciente.getHighlightColor());
                             Conexion.setTextColor(Color.WHITE);
                             Conexion.setText("Conectar");
                             dispBluetoothNom1.setText("");
                             dispBluetoothNom1.setVisibility(View.GONE);
                             InicioPulsos.setEnabled(false);

                         });
                     }
                     // Dispositivo 2 (usa address y btSocket)
                     else if (mac.equals(address)) {
                         if (mConnectedThread2 != null) {
                             mConnectedThread2.cancel();
                             mConnectedThread2 = null;
                         }
                         try {
                             if (btSocket != null) btSocket.close();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         IsConnected = false;
                         runOnUiThread(() -> {
                             Conexion2.setBackgroundColor(ventanaPaciente.getHighlightColor());
                             Conexion2.setTextColor(Color.WHITE);
                             Conexion2.setText("Conectar");
                             dispBluetoothNom2.setText("");
                             dispBluetoothNom2.setVisibility(View.GONE);
                             InicioPulsos2.setEnabled(false);
                         });
                     }
                 }
             }
         }
     };
    private class ConnectedThread extends Thread
    {
        public void cancel() {
            try {
                IsBattMon3 = false;
                if (mInputStream3 != null) {
                    mInputStream3.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run()
        {
            byte[] buffer = new byte[8];
            int bytes=0;
            int j=0;
            int auxint=0;

            while(IsBattMon3)
            {
                bytes=0;
                try {
                    bytes = mInputStream3.read(buffer);
                    for(int i=0;i<bytes;i++)
                    {
                        miByteRec[IndiceRec]=buffer[i];
                        IndiceRec++;
                    }

                    N_Bytes=N_Bytes+bytes;
                    while(N_Bytes>=4)
                    {
                        j=0;
                        //mInputStream3.
                        while(miByteRec[j]<2 || miByteRec[j]>3)
                            j++;
                        IndiceRec=0;
                        //IsBattMon3=false;
                        Log.d("APP","Recibidos Total Bytes:"+ N_Bytes);
                        Log.d("APP","Recibidos Bytes:"+ miByteRec[0]+";"+ miByteRec[1]+";"+ miByteRec[2]+";"+ miByteRec[3]+";"+ miByteRec[4]);
                        N_Bytes=0;
                        if(miByteRec[j+1]<0)
                        {
                            auxint=miByteRec[j+1]+1;
                            auxint=255+auxint;
                        }
                        else
                            auxint=miByteRec[j+1];
                        auxint=auxint+miByteRec[j]*256;
                        ValorCarga=auxint;
                        if((ValorCarga!=ValorCargaAnt)&&(ValorCarga>780))
                        {
                            ValorCargaAnt=ValorCarga;
                            IsBattMon3=false;
                        }
                        miByteRec[0]=0;
                        miByteRec[1]=0;
                        miByteRec[2]=0;
                        miByteRec[3]=0;
                        Log.d("APP","Carga Bateria device 3:"+ auxint);
                        //IsBattMonSent3 =true;
                        IsDualdeviceON=true;

                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if(ValorCargaAnt>=890)
                                {
                                    NivelCarga=3;
                                    writeMessage="ALTA";
                                    NivelBatt.setTextColor(Color.GREEN);
                                }

                                else if(ValorCargaAnt>=840 && ValorCargaAnt<890)
                                {
                                    NivelCarga=2;
                                    writeMessage="MEDIA";
                                    NivelBatt.setTextColor(Color.YELLOW);
                                }

                                else if(ValorCargaAnt<840)
                                {
                                    writeMessage="BAJA";
                                    NivelCarga=1;
                                    NivelBatt.setTextColor(Color.parseColor("#FF0000"));
                                }
                                NivelBatt.setText(writeMessage);//NivelNivelBatt.setTextColor(Color.GREEN);Batt
                                writeMessage = String.valueOf(ValorCargaAnt);//ValorCargaAnt
                                OtroTexto6.setText(writeMessage);
                                writeMessage="";
                            }
                        });

                    }

                }
                catch (IOException e)
                {
                    break;

                }
            }

        }

    }

    private class ConnectedThread2 extends Thread
    {
        public void cancel() {
            try {
                IsBattMon = false;
                if (mInputStream != null) {
                    mInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            byte[] buffer = new byte[28];
            int bytes=0;
            int j=0;
            int auxint=0;

            while(IsBattMon)
            {
                bytes=0;
                try {
                    bytes = mInputStream.read(buffer);
                    for(int i=0;i<bytes;i++)
                    {
                        miByteRec[IndiceRec]=buffer[i];
                        IndiceRec++;
                    }

                    N_Bytes=N_Bytes+bytes;
                    while(N_Bytes>=4)
                    {
                        j=0;
                        //mInputStream3.
                        while(miByteRec[j]<2 || miByteRec[j]>3)
                            j++;
                        IndiceRec=0;
                        //IsBattMon3=false;
                        Log.d("APP","Recibidos Total Bytes:"+ N_Bytes);
                        Log.d("APP","Recibidos Bytes:"+ miByteRec[0]+";"+ miByteRec[1]+";"+ miByteRec[2]+";"+ miByteRec[3]+";"+ miByteRec[4]);
                        N_Bytes=0;
                        if(miByteRec[j+1]<0)
                        {
                            auxint=miByteRec[j+1]+1;
                            auxint=255+auxint;
                        }
                        else
                            auxint=miByteRec[j+1];
                        auxint=auxint+miByteRec[j]*256;
                        ValorCarga2=auxint;
                        if((ValorCarga2!=ValorCargaAnt2)&&(ValorCarga2>780))
                        {
                            ValorCargaAnt2=ValorCarga2;
                            IsBattMon=false;
                            Log.d("APP","Carga Bateria device 2:"+ auxint);
                        }
                        miByteRec[0]=0;
                        miByteRec[1]=0;
                        miByteRec[2]=0;
                        miByteRec[3]=0;

                        //IsBattMonSent3 =true;
                        IsDualdeviceON=true;

                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if(ValorCargaAnt2>=890)
                                {
                                    NivelCarga=3;
                                    writeMessage="ALTA";
                                    NivelBatt2.setTextColor(Color.GREEN);
                                }

                                else if(ValorCargaAnt2>=840 && ValorCargaAnt2<890)
                                {
                                    NivelCarga=2;
                                    writeMessage="MEDIA";
                                    NivelBatt2.setTextColor(Color.YELLOW);
                                }

                                else if(ValorCargaAnt2<840)
                                {
                                    writeMessage="BAJA";
                                    NivelCarga=1;
                                    NivelBatt2.setTextColor(Color.parseColor("#FF0000"));
                                }
                                NivelBatt2.setText(writeMessage);//NivelNivelBatt.setTextColor(Color.GREEN);Batt
                                writeMessage = String.valueOf(ValorCargaAnt2);//ValorCargaAnt
                                OtroTexto7.setText(writeMessage);
                                writeMessage="";
                            }
                        });


                    }

                }
                catch (IOException e)
                {
                    break;

                }
            }

        }

    }
}