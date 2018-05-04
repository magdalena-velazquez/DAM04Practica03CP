package mx.unam.fes.dam04practica03cp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ContactosContentProvider extends ContentProvider {

    private DBHelper MBD;
    SQLiteDatabase SQLDB;
    private static final String AUTHORITY = "mx.unam.fes.contactos";
    private static final String BASE_PATH = "contactos";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static final int CONTACTOS = 1;
    private static final int CONTACTOS_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH, CONTACTOS);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",CONTACTOS_ID);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public boolean onCreate() {
        MBD = new DBHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long registro = 0;
        try {
            if (uriMatcher.match(uri) == CONTACTOS) {
                SQLDB = MBD.getWritableDatabase();
                registro = SQLDB.insert(BASE_PATH, null, values);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        // Comprobar si se inserto bien el registro
        if (registro > 0) {
            Log.e("INSERT", "Registro creado correctamente");
        } else {
            Log.e("Error", "Al insertar registro: " + registro);
        }

        return Uri.parse(BASE_PATH + "/" + registro);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String id = "";
        try {
            if (uriMatcher.match(uri) == CONTACTOS_ID) {
                id = uri.getLastPathSegment();
                SQLDB = MBD.getWritableDatabase();
                SQLDB.update(BASE_PATH, values, "_id=" + id, selectionArgs);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return Integer.parseInt(id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int registro = 0;
        try {
            if (uriMatcher.match(uri) == CONTACTOS_ID) {
                String id = "_id=" + uri.getLastPathSegment();
                SQLDB = MBD.getWritableDatabase();
                registro = SQLDB.delete(BASE_PATH, id, null);
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return registro;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        try {
            switch (uriMatcher.match(uri)) {
                case CONTACTOS_ID:
                    String id = "_id=" + uri.getLastPathSegment();
                    SQLDB = MBD.getReadableDatabase();
                    c = SQLDB.query(BASE_PATH, projection, id, selectionArgs,
                            null, null, null, sortOrder);
                    break;
                case CONTACTOS:
                    SQLDB = MBD.getReadableDatabase();
                    c = SQLDB.query(BASE_PATH, projection, null, selectionArgs,
                            null, null, null, sortOrder);
                    break;
            }
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Argumento no admitido: " + e.toString());
        }

        return c;
    }
}