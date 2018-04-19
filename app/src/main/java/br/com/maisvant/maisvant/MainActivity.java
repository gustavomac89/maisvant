package br.com.maisvant.maisvant;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // lock
    static Object tranca = new Object();

    private static BancoDeDespesas banco;

    static BancoDeDespesas bancoDeDespesas(Context context) {
        synchronized (tranca) {
            if (banco == null)
                banco = Room.databaseBuilder(context,
                        BancoDeDespesas.class, "base-1").build();
            return banco;
        }
    }

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;
    private BancoDeDespesas base;
    Intent i;
    private List<String> listaAtual;
    String[] opc = { "Combustivel", "Manutenção","Estacionamento","Outros" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();
        base = bancoDeDespesas(getApplicationContext());

        rodarNaThreadDoBanco(
                new Runnable() {
                    @Override
                    public void run() {
                        DespesaDao dao = base.despesaDao();
                        List<Despesa> despesas = dao.listar();
                        listaAtual = new ArrayList<>();
                        for (Despesa i: despesas) {
                            listaAtual.add(i.descricao);
                        }
                        rodarNaThreadPrincipal(new Runnable() {
                            @Override
                            public void run() {
                                preencherConteúdo();
                            }
                        });
                    }
                });


        final ListView listview = (ListView) findViewById(R.id.lista);
        final ListView rec = (ListView) findViewById(R.id.recentes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opc);

        Button mais = (Button) findViewById(R.id.buttonmais);
        listview.setAdapter(adapter);
        listview.setVisibility(View.INVISIBLE);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listview.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, AdicionarDespesa.class);
                intent.putExtra("item",listItem.toString());
                startActivity(intent);
            }
        });

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

    private void preencherConteúdo() {
        final ListView listview = (ListView) findViewById(R.id.recentes);

        if (listaAtual == null) {
            listview.setAdapter(null);
            return;
        }

        ArrayAdapter<String> adRecente = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaAtual);
        listview.setAdapter(adRecente);
    }

    void rodarNaThreadPrincipal(Runnable acao) {
        handlerThreadPrincipal.post(acao);
    }

    void rodarNaThreadDoBanco(Runnable acao) {
        executorThreadDoBanco.execute(acao);
    }
}
