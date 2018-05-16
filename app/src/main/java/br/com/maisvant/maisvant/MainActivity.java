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
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;

    Intent i;
    private List<String> listaAtual;
    String[] opc = { "Combustivel", "Manutenção","Estacionamento","Outros" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();

        rodarNaThreadDoBanco(
                new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(MainActivity.this);
                        DespesaDao dao = banco.despesaDao();
                        rodarNaThreadPrincipal(new Runnable() {
                            @Override
                            public void run() {
                                atualizar();
                            }
                        });
                    }
                });


        final ListView listview = (ListView) findViewById(R.id.lista);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opc);

        ImageButton mais = (ImageButton) findViewById(R.id.buttonmais);
        listview.setAdapter(adapter);
        listview.setVisibility(View.INVISIBLE);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listview.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, AdicionarDespesa.class);
                intent.putExtra("item",listItem.toString());
                startActivityForResult(intent, 0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        atualizar();
    }

    private void atualizar() {
        rodarNaThreadDoBanco(new Runnable() {
            @Override
            public void run() {
                BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(MainActivity.this);
                DespesaDao dao = banco.despesaDao();
                final List<Despesa> despesas = dao.listar();
                rodarNaThreadPrincipal(new Runnable() {
                    @Override
                    public void run() {
                        ListView lista = findViewById(R.id.recentes);
                        ArrayAdapter<Despesa> adaptador = new ArrayAdapter<>(
                                MainActivity.this,
                                android.R.layout.simple_list_item_1,
                                despesas);
                    lista.setAdapter(adaptador);

                    }
                });
            }
        });
    }

    void rodarNaThreadPrincipal(Runnable acao) {
        handlerThreadPrincipal.post(acao);
    }

    void rodarNaThreadDoBanco(Runnable acao) {
        executorThreadDoBanco.execute(acao);
    }
}
