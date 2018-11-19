package br.com.maisvant.maisvant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdicionarTipo extends AppCompatActivity {

    private Executor executorThreadDoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tipo);
        Button adc = (Button) findViewById(R.id.btn);


        executorThreadDoBanco = Executors.newSingleThreadExecutor();


        adc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText nome =  findViewById(R.id.cNome);
                final Tipo tipo = new Tipo();
                tipo.nome = nome.getText().toString();
                rodarNaThreadDoBanco(new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(AdicionarTipo.this);
                        TipoDao dao = banco.tipoDao();
                        dao.inserir(tipo);
                        setResult(RESULT_OK);
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
