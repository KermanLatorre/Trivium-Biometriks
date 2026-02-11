package com.example.triviumgor.database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.triviumgor.Sesion;
import com.example.triviumgor.Paciente;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PacienteDataManager {
        private SQLiteDatabase database;
        private final PacienteDBHelper dbHelper;

        public PacienteDataManager(Context context) {
            dbHelper = new PacienteDBHelper(context);
        }

    public boolean open() {
        try {
            database = dbHelper.getWritableDatabase();
            return true;
        } catch (SQLException e) {
            Log.e("ERROR", "Error SQL al abrir la base de datos: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e("ERROR", "Error al abrir la base de datos: " + e.getMessage());
            return false;
        }
    }

        public void close() {
            dbHelper.close();
        }

    // ======= MÉTODOS PARA PACIENTES =======

        public long nuevoPaciente(String dni, String nombre, String apellido1, String apellido2,
                                     String patologia,String medicacion, int intensidad, int tiempo, String cic) {
            ContentValues values = new ContentValues();
            values.put(PacienteDBHelper.COLUMN_DNI, dni);
            values.put(PacienteDBHelper.COLUMN_NOMBRE, nombre);
            values.put(PacienteDBHelper.COLUMN_APELLIDO1, apellido1);
            values.put(PacienteDBHelper.COLUMN_APELLIDO2, apellido2);
            values.put(PacienteDBHelper.COLUMN_PATOLOGIA, patologia);
            values.put(PacienteDBHelper.COLUMN_MEDICACIÓN, medicacion);
            values.put(PacienteDBHelper.COLUMN_INTENSIDAD, intensidad);
            values.put(PacienteDBHelper.COLUMN_TIEMPO, tiempo);
            values.put(PacienteDBHelper.COLUMN_CIC, cic);

            return database.insert(PacienteDBHelper.TABLE_PACIENTES, null, values);
        }

        // Obtener todos los pacientes
        public Cursor obtenerTodosPacientes() {

            return database.query(
                    PacienteDBHelper.TABLE_PACIENTES,
                    null,
                    null,
                    null,
                    null,
                    null,
                    PacienteDBHelper.COLUMN_NOMBRE // Ordenar por nombre
            );
        }
    public Cursor obtenerPacientePorId(int id) {
        return database.query(
                PacienteDBHelper.TABLE_PACIENTES,
                null,
                PacienteDBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
    }
    public int actualizarPaciente(int id, String dni, String nombre, String apellido1,
                                  String apellido2, String patologia, String medicacion, int intensidad,
                                  int tiempo, String cic) {
        try {
            ContentValues values = new ContentValues();
            values.put(PacienteDBHelper.COLUMN_DNI, dni);
            values.put(PacienteDBHelper.COLUMN_NOMBRE, nombre);
            values.put(PacienteDBHelper.COLUMN_APELLIDO1, apellido1);
            values.put(PacienteDBHelper.COLUMN_APELLIDO2, apellido2);
            values.put(PacienteDBHelper.COLUMN_PATOLOGIA, patologia);
            values.put(PacienteDBHelper.COLUMN_MEDICACIÓN, medicacion);
            values.put(PacienteDBHelper.COLUMN_INTENSIDAD, intensidad);
            values.put(PacienteDBHelper.COLUMN_TIEMPO, tiempo);
            values.put(PacienteDBHelper.COLUMN_CIC, cic);

            return database.update(
                    PacienteDBHelper.TABLE_PACIENTES,
                    values,
                    PacienteDBHelper.COLUMN_ID + " = ?",
                    new String[]{ String.valueOf(id) }
            );
        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar paciente: " + e.getMessage());
            return -1;
        }
    }

    //guardar configuracion
    public int guardarConfiguracion(String dni, int intensidad,
                                    int tiempo){
            try {
                ContentValues values = new ContentValues();
                values.put(PacienteDBHelper.COLUMN_INTENSIDAD, intensidad);
                values.put(PacienteDBHelper.COLUMN_TIEMPO, tiempo);

                return  database.update(
                        PacienteDBHelper.TABLE_PACIENTES,
                        values,
                        PacienteDBHelper.COLUMN_DNI + " =?",
                        new String[]{dni}
                );
            }catch(Exception e) {
            Log.e(TAG, "Error al actualizar paciente: " + e.getMessage());
            return -1;
        }

    }
    //para borrar
    public boolean reiniciarAutoIncrement() {
        try {
            if (database != null && database.isOpen()) {
                database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='" +
                        PacienteDBHelper.TABLE_PACIENTES + "'");
                return true;
            }
            return false;
        } catch (SQLException e) {
            Log.e("ERROR", "Error al reiniciar autoincrement: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina permanentemente un paciente de la base de datos
     * @param idPaciente ID del paciente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarPaciente(long idPaciente) {
        try {
            // Primero eliminar las sesiones asociadas al paciente
            database.delete(
                    PacienteDBHelper.TABLE_SESIONES,
                    PacienteDBHelper.COLUMN_PACIENTE_ID + " = ?",
                    new String[] { String.valueOf(idPaciente) }
            );

            // Luego eliminar el paciente
            return database.delete(
                    PacienteDBHelper.TABLE_PACIENTES,
                    PacienteDBHelper.COLUMN_ID + " = ?",
                    new String[] { String.valueOf(idPaciente) }
            ) > 0;
        } catch (SQLException e) {
            Log.e("ERROR", "Error al eliminar paciente: " + e.getMessage());
            return false;
        }
    }





    //para 1 paciente 2 dispositivos

    public long nuevoPaciente2disp(String dni, String nombre, String apellido1, String apellido2,
                              String patologia,String medicacion, int intensidad, int tiempo, int intensidad2, int tiempo2, String cic) {
        ContentValues values = new ContentValues();
        values.put(PacienteDBHelper.COLUMN_DNI, dni);
        values.put(PacienteDBHelper.COLUMN_NOMBRE, nombre);
        values.put(PacienteDBHelper.COLUMN_APELLIDO1, apellido1);
        values.put(PacienteDBHelper.COLUMN_APELLIDO2, apellido2);
        values.put(PacienteDBHelper.COLUMN_PATOLOGIA, patologia);
        values.put(PacienteDBHelper.COLUMN_MEDICACIÓN, medicacion);
        values.put(PacienteDBHelper.COLUMN_INTENSIDAD, intensidad);
        values.put(PacienteDBHelper.COLUMN_TIEMPO, tiempo);
        values.put(PacienteDBHelper.COLUMN_INTENSIDAD2, intensidad2);
        values.put(PacienteDBHelper.COLUMN_TIEMPO2, tiempo2);
        values.put(PacienteDBHelper.COLUMN_CIC, cic);

        return database.insert(PacienteDBHelper.TABLE_PACIENTES, null, values);
    }
    public int actualizarPaciente2disp(int id, String dni, String nombre, String apellido1,
                                  String apellido2, String patologia, String medicacion, int intensidad,
                                  int tiempo,int intensidad2, int tiempo2, String cic) {
        try {
            ContentValues values = new ContentValues();
            values.put(PacienteDBHelper.COLUMN_DNI, dni);
            values.put(PacienteDBHelper.COLUMN_NOMBRE, nombre);
            values.put(PacienteDBHelper.COLUMN_APELLIDO1, apellido1);
            values.put(PacienteDBHelper.COLUMN_APELLIDO2, apellido2);
            values.put(PacienteDBHelper.COLUMN_PATOLOGIA, patologia);
            values.put(PacienteDBHelper.COLUMN_MEDICACIÓN, medicacion);
            values.put(PacienteDBHelper.COLUMN_INTENSIDAD, intensidad);
            values.put(PacienteDBHelper.COLUMN_TIEMPO, tiempo);
            values.put(PacienteDBHelper.COLUMN_INTENSIDAD2, intensidad2);
            values.put(PacienteDBHelper.COLUMN_TIEMPO2, tiempo2);
            values.put(PacienteDBHelper.COLUMN_CIC, cic);

            return database.update(
                    PacienteDBHelper.TABLE_PACIENTES,
                    values,
                    PacienteDBHelper.COLUMN_ID + " = ?",
                    new String[]{ String.valueOf(id) }
            );
        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar paciente: " + e.getMessage());
            return -1;
        }
    }

    //guardar configuracion
    public int guardarConfiguracion2disp(String dni, int intensidad, int tiempo, int intensidad2, int tiempo2){
        try {
            ContentValues values = new ContentValues();
            values.put(PacienteDBHelper.COLUMN_INTENSIDAD, intensidad);
            values.put(PacienteDBHelper.COLUMN_TIEMPO, tiempo);
            values.put(PacienteDBHelper.COLUMN_INTENSIDAD2, intensidad2);
            values.put(PacienteDBHelper.COLUMN_TIEMPO2, tiempo2);

            return  database.update(
                    PacienteDBHelper.TABLE_PACIENTES,
                    values,
                    PacienteDBHelper.COLUMN_DNI + " =?",
                    new String[]{dni}
            );
        }catch(Exception e) {
            Log.e(TAG, "Error al actualizar paciente: " + e.getMessage());
            return -1;
        }

    }
    // ======= MÉTODOS PARA SESIONES =======

    /**
     * Registra una nueva sesión de tratamiento con notas
     * @param idPaciente ID del paciente
     * @param dispositivo Número de dispositivo (1, 2, o 3 para ambos)
     * @param intensidad Intensidad utilizada
     * @param tiempo Tiempo del tratamiento
     * @return ID de la sesión creada o -1 si hubo error
     */
    public long registrarSesion(int idPaciente, String dispositivo, int intensidad, int tiempo) {
        try {
            // Obtener fecha actual en formato ISO
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechaActual = sdf.format(new Date());

            ContentValues values = new ContentValues();
            values.put(PacienteDBHelper.COLUMN_PACIENTE_ID, idPaciente);
            values.put(PacienteDBHelper.COLUMN_DISPOSITIVO, dispositivo);
            values.put(PacienteDBHelper.COLUMN_FECHA, fechaActual);
            values.put(PacienteDBHelper.COLUMN_INTENSIDAD_SESION, intensidad);
            values.put(PacienteDBHelper.COLUMN_TIEMPO_SESION, tiempo);


            long idSesion = database.insert(PacienteDBHelper.TABLE_SESIONES, null, values);

            if (idSesion != -1) {
                Log.d("PacienteDataManager", "Sesión registrada con éxito, ID: " + idSesion);
            } else {
                Log.e("PacienteDataManager", "Error al registrar sesión");
            }

            return idSesion;
        } catch (Exception e) {
            Log.e("PacienteDataManager", "Error al registrar sesión: " + e.getMessage());
            return -1;
        }
    }
    /**
     * Obtiene todas las sesiones de un paciente
     * @param idPaciente ID del paciente
     * @return Lista de sesiones
     */
    public List<Sesion> obtenerSesionesPaciente(int idPaciente) {
        List<Sesion> sesiones = new ArrayList<>();

        Cursor cursor = database.query(
                PacienteDBHelper.TABLE_SESIONES,
                null,
                PacienteDBHelper.COLUMN_PACIENTE_ID + " = ?",
                new String[]{String.valueOf(idPaciente)},
                null,
                null,
                PacienteDBHelper.COLUMN_FECHA + " DESC" // Ordenar por fecha, más reciente primero
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_SESION_ID));
                String dispositivo = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_DISPOSITIVO));
                String fecha = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_FECHA));
                int intensidad = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_INTENSIDAD_SESION));
                int tiempo = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_TIEMPO_SESION));


                Sesion sesion = new Sesion(id, idPaciente, dispositivo, fecha, intensidad, tiempo);
                sesiones.add(sesion);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return sesiones;
    }

    /**
     * Obtiene una sesión específica por su ID
     * @param idSesion ID de la sesión
     * @return Objeto Sesion o null si no se encuentra
     */
    public Sesion obtenerSesion(int idSesion) {
        Cursor cursor = database.query(
                PacienteDBHelper.TABLE_SESIONES,
                null,
                PacienteDBHelper.COLUMN_SESION_ID + " = ?",
                new String[]{String.valueOf(idSesion)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_SESION_ID));
            int idPaciente = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_PACIENTE_ID));
            String dispositivo = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_DISPOSITIVO));
            String fecha = cursor.getString(cursor.getColumnIndex(PacienteDBHelper.COLUMN_FECHA));
            int intensidad = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_INTENSIDAD_SESION));
            int tiempo = cursor.getInt(cursor.getColumnIndex(PacienteDBHelper.COLUMN_TIEMPO_SESION));


            cursor.close();
            return new Sesion(id, idPaciente, dispositivo, fecha, intensidad, tiempo);
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    /**
     * Elimina una sesión de la base de datos
     * @param idSesion ID de la sesión a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarSesion(int idSesion) {
        try {
            return database.delete(
                    PacienteDBHelper.TABLE_SESIONES,
                    PacienteDBHelper.COLUMN_SESION_ID + " = ?",
                    new String[]{String.valueOf(idSesion)}
            ) > 0;
        } catch (Exception e) {
            Log.e("PacienteDataManager", "Error al eliminar sesión: " + e.getMessage());
            return false;
        }
    }


}

