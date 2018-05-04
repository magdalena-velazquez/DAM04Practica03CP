package mx.unam.fes.dam04practica03cp;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView texto = (TextView) findViewById(R.id.contactos);

        Cursor c = managedQuery(ContactsContract.Contacts.CONTENT_URI,
                new String[] {ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts.IN_VISIBLE_GROUP,
                null,
                ContactsContract.Contacts.DISPLAY_NAME);

        texto.setText("");
        while (c.moveToNext()) {
            String contactos = c.getString(c
                    .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

            texto.append(contactos);
            texto.append("\n");
        }
    }

    public void useContentResolver(View v){
        Intent i = new Intent(this, ContentResolverActivity.class);
        startActivity(i);
    }
}
