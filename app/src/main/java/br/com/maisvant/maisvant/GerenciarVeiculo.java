package br.com.maisvant.maisvant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GerenciarVeiculo extends AppCompatActivity {

    private Executor executorThreadDoBanco;
    private Handler handlerThreadPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_veiculo);
        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();
        atualizar();
        Button novo = (Button) findViewById(R.id.btn_novo);
        Button excluir = (Button) findViewById(R.id.btn_excluir);
        Button editar = (Button) findViewById(R.id.btn_editar);

        novo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GerenciarVeiculo.this, AdicionarVeiculo.class);
                intent.putExtra("veiculo", "0");
                startActivity(intent);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GerenciarVeiculo.this, EditarVeiculo.class);
                Spinner spinner = (Spinner) findViewById(R.id.veiculo);
                final Veiculo ve = (Veiculo) spinner.getSelectedItem();
                intent.putExtra("veiculo", String.valueOf(ve.id_veiculo));
                startActivity(intent);
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Spinner spinner = (Spinner) findViewById(R.id.veiculo);
                final Veiculo ve = (Veiculo) spinner.getSelectedItem();
                rodarNaThreadDoBanco(
                        new Runnable() {
                            @Override
                            public void run() {
                                BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(GerenciarVeiculo.this);
                                VeiculoDao dao = banco.veiculoDao();
                                dao.delete(ve);
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

    public void atualizar() {
        rodarNaThreadDoBanco(
                new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(GerenciarVeiculo.this);
                        VeiculoDao dao = banco.veiculoDao();
                        final List<Veiculo> veiculos = dao.listar();
                        rodarNaThreadPrincipal(new Runnable() {
                            @Override
                            public void run() {
                                final Spinner spinner = (Spinner) findViewById(R.id.veiculo);
                                ArrayAdapter adapter = new ArrayAdapter(GerenciarVeiculo.this, android.R.layout.simple_spinner_item, veiculos);
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
