package br.com.maisvant.maisvant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdicionarVeiculo extends AppCompatActivity {

    private Executor executorThreadDoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_veiculo);
        final EditText modelo =  findViewById(R.id.cModelo);
        final EditText marca =  findViewById(R.id.cMarca);
        final EditText placa =  findViewById(R.id.cPlaca);

        executorThreadDoBanco = Executors.newSingleThreadExecutor();


        Button adc = (Button) findViewById(R.id.btn);


        adc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Veiculo veiculo = new Veiculo();
                veiculo.modelo = modelo.getText().toString();
                veiculo.marca = marca.getText().toString();
                veiculo.placa = placa.getText().toString();

                rodarNaThreadDoBanco(new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(AdicionarVeiculo.this);
                        VeiculoDao dao = banco.veiculoDao();
                        dao.inserir(veiculo);
                        finish();
                    }
                });
            }
        });


    }


    void rodarNaThreadDoBanco(Runnable acao) {
        executorThreadDoBanco.execute(acao);
    }


}
