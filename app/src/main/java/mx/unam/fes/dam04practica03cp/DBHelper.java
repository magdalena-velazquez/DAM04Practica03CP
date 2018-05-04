package mx.unam.fes.dam04practica03cp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB = "mibasedatos.db";
    private static final String TABLA_CONTACTOS = "contactos";
    private static final String CREATE_TABLA_CONTACTOS = "CREATE TABLE "
            + TABLA_CONTACTOS +
            "(_id INT PRIMARY KEY, nombre TEXT, telefono INT, email TEXT)";


    public DBHelper(Context context) {
        super(context, DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLA_CONTACTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLA_CONTACTOS);
        onCreate(db);
    }
}