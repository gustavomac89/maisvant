package br.com.maisvant.maisvant;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();
        ImageButton mais = (ImageButton) findViewById(R.id.buttonmais);
        atualizar();
        mais.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdicionarDespesa.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                            AdapterDespesas adaptador = new AdapterDespesas(despesas,
                                    MainActivity.this);
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
