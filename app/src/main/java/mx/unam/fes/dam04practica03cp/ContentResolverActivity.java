package mx.unam.fes.dam04practica03cp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ContentResolverActivity extends AppCompatActivity
        implements View.OnClickListener{

    private static final String ID_CP
            = "content://mx.unam.fes.contactos/contactos";
    private static final Uri URI_CP = Uri.parse(ID_CP);

    String[] COLUMNAS = {"_id", "nombre", "telefono", "email"};

    private Uri uri;
    private Cursor c;
    ContentResolver cr;

    private int id, ai;
    private String nombre;
    private int telefono;
    private String email;

    boolean insertado = false;

    private String dominio = "@fes.unam.mx";

    ImageButton bInicio, bAnterior, bInsertar, bGuardar, bBorrar, bSiguiente
            , bUltimo;
    EditText etNom, etTel, etEmail;
    TextView tvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_resolver);

        cr = getContentResolver();

        tvId = (TextView) findViewById(R.id.tvId);
        etNom = (EditText) findViewById(R.id.editTextNom);
        etTel = (EditText) findViewById(R.id.editTextTel);
        etEmail = (EditText) findViewById(R.id.editTextEmail);

        bInicio = (ImageButton) findViewById(R.id.btn_inicio);
        bAnterior = (ImageButton) findViewById(R.id.btn_anterior);
        bInsertar = (ImageButton) findViewById(R.id.btn_insertar);
        bGuardar = (ImageButton) findViewById(R.id.btn_guardar);
        bBorrar = (ImageButton) findViewById(R.id.btn_borrar);
        bSiguiente = (ImageButton) findViewById(R.id.btn_siguiente);
        bUltimo = (ImageButton) findViewById(R.id.btn_ultimo);

        bInicio.setOnClickListener(this);
        bAnterior.setOnClickListener(this);
        bInsertar.setOnClickListener(this);
        bGuardar.setOnClickListener(this);
        bBorrar.setOnClickListener(this);
        bSiguiente.setOnClickListener(this);
        bUltimo.setOnClickListener(this);

        select();
        ai = c.getCount() + 1;
        muestraCampos();
    }

    @Override
    public void onClick(View view) {
        int item = view.getId();
        switch (item){
            case R.id.btn_inicio:
                c.moveToFirst();
                muestraCampos();
                break;
            case R.id.btn_anterior:
                c.moveToPrevious();
                muestraCampos();
                break;
            case R.id.btn_insertar:
                insertar();
                break;
            case R.id.btn_guardar:
                actualizar();
                break;
            case R.id.btn_borrar:
                eliminar();
                break;
            case R.id.btn_siguiente:
                c.moveToNext();
                muestraCampos();
                break;
            case R.id.btn_ultimo:
                c.moveToLast();
                muestraCampos();
                break;

        }
    }

    private void insertar(){
        if(!insertado){
            tvId.setText(ai+"");
            etNom.setText("");
            etTel.setText("");
            etEmail.setText("");
            bInsertar.setImageResource(android.R.drawable.ic_menu_add);
            insertado = true;
        }else{
            uri = cr.insert(URI_CP, setValores(ai
                    , etNom.getText().toString()
                    , Integer.parseInt(etTel.getText().toString())
                    , etEmail.getText().toString()));
            select();
            ai++;
            insertado = false;
            bInsertar.setImageResource(android.R.drawable.ic_input_add);
            Log.d("REGISTRO INSERTADO", uri.toString());
        }
    }

    private void actualizar(){
        id = Integer.parseInt(tvId.getText().toString());
        uri = Uri.parse(ID_CP + "/" + id);
        cr.update(uri, setValores(id
                , etNom.getText().toString()
                , Integer.parseInt(etTel.getText().toString())
                , etEmail.getText().toString()),
                null, null);
        select();
    }

    private void eliminar(){
        id = Integer.parseInt(tvId.getText().toString());
        uri = Uri.parse(ID_CP + "/" +id);
        cr.delete(uri, null, null);
        select();
    }
    private void select(){
        // Recuperar todos los registros de la tabla
        c = cr.query(URI_CP, COLUMNAS, null, null, null);
        Log.d("CURSOR", c.toString());
        c.moveToFirst();
        do {
            id = c.getInt(0);
            nombre = c.getString(1);
            telefono = c.getInt(2);
            email = c.getString(3);
            Log.d("QUERY", "" +id+ ", " +nombre+ ", " +telefono+ ", " +email);
        } while (c.moveToNext());
        c.moveToFirst();
    }

    private void muestraCampos(){
        tvId.setText(c.getInt(0)+"");
        etNom.setText(c.getString(1));
        etTel.setText(c.getInt(2)+"");
        etEmail.setText(c.getString(3));

    }
    private ContentValues setValores(int id, String nom, int tel, String email) {
        ContentValues valores = new ContentValues();
        valores.put("_id", id);
        valores.put("nombre", nom);
        valores.put("telefono", tel);
        valores.put("email", email);
        return valores;
    }
}
