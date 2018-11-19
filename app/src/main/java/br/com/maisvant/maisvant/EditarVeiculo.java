package br.com.maisvant.maisvant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditarVeiculo extends AppCompatActivity {

    private Executor executorThreadDoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_veiculo);
        final EditText modelo =  findViewById(R.id.cModelo);
        final EditText marca =  findViewById(R.id.cMarca);
        final EditText placa =  findViewById(R.id.cPlaca);
        final Veiculo veiculoTransicao = new Veiculo();

        executorThreadDoBanco = Executors.newSingleThreadExecutor();


        Button adc = (Button) findViewById(R.id.btn);
        adc.setText("SALVAR");
        Intent intent = getIntent();
        final String v = intent.getStringExtra("veiculo");

        rodarNaThreadDoBanco(new Runnable() {
            @Override
            public void run() {
                BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(EditarVeiculo.this);
                VeiculoDao dao = banco.veiculoDao();
                Veiculo veiculo = dao.veiculoId(v);
                veiculoTransicao.id_veiculo = veiculo.id_veiculo;
                veiculoTransicao.modelo = veiculo.modelo;
                veiculoTransicao.marca = veiculo.marca;
                veiculoTransicao.placa = veiculo.placa;
                modelo.setText(veiculo.modelo);
                marca.setText(veiculo.marca);
                placa.setText(veiculo.placa);
            }
        });


        adc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                veiculoTransicao.modelo = modelo.getText().toString();
                veiculoTransicao.marca = marca.getText().toString();
                veiculoTransicao.placa = placa.getText().toString();

                rodarNaThreadDoBanco(new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(EditarVeiculo.this);
                        VeiculoDao dao = banco.veiculoDao();
                        dao.update(veiculoTransicao);
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
