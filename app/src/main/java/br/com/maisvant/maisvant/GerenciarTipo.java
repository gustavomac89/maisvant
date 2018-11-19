package br.com.maisvant.maisvant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GerenciarTipo extends AppCompatActivity {

    private Executor executorThreadDoBanco;
    private Handler handlerThreadPrincipal;
    final List<Tipo> tipos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_tipo);
        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();
        Button novo = (Button) findViewById(R.id.btn_novo);
        Button excluir = (Button) findViewById(R.id.btn_excluir);
        Button editar = (Button) findViewById(R.id.btn_editar);

        atualizar();

        novo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GerenciarTipo.this, AdicionarTipo.class);
                startActivityForResult(intent, 0);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GerenciarTipo.this, EditarTipo.class);
                Spinner spinner = (Spinner) findViewById(R.id.tipo);
                final Tipo tp = (Tipo) spinner.getSelectedItem();
                intent.putExtra("tipo", String.valueOf(tp.id_tipo));
                startActivity(intent);
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner spinner = (Spinner) findViewById(R.id.tipo);
                final Tipo tp = (Tipo) spinner.getSelectedItem();
                rodarNaThreadDoBanco(
                        new Runnable() {
                            @Override
                            public void run() {
                                BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(GerenciarTipo.this);
                                TipoDao dao = banco.tipoDao();
                                dao.delete(tp);
                            }
                        });
                atualizar();

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        atualizar();
    }


    private void atualizar() {
        rodarNaThreadDoBanco(
                new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(GerenciarTipo.this);
                        TipoDao dao = banco.tipoDao();
                        tipos.clear();
                        tipos.addAll(dao.listar());
                        rodarNaThreadPrincipal(new Runnable() {
                            @Override
                            public void run() {
                                final Spinner spinner = (Spinner) findViewById(R.id.tipo);
                                ArrayAdapter adapter = new ArrayAdapter(GerenciarTipo.this, android.R.layout.simple_spinner_item, tipos);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
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