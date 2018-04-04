package br.com.maisvant.maisvant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    Intent i;
    String[] dados = new String[] { "Combustivel","Estacionamento","Manutenção","Imposto", "Outros" };
    String[] recentes = new String[] { "10,00","20,00","30,00","800,00", "250,00" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mais = (Button) findViewById(R.id.buttonmais);
        i = new Intent(this, ListaActivity.class);
        final ListView listview = (ListView) findViewById(R.id.lista);
        final ListView rec = (ListView) findViewById(R.id.recentes);
        ArrayAdapter<String> adRecente = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recentes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dados);

        listview.setAdapter(adapter);
        rec.setAdapter(adRecente);
        mais.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listview.getVisibility() == View.VISIBLE) {
                    listview.setVisibility(View.INVISIBLE);
                }else{
                    listview.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
