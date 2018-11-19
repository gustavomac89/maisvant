package br.com.maisvant.maisvant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditarTipo extends AppCompatActivity {

    private Executor executorThreadDoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tipo);
        final EditText nome =  findViewById(R.id.cNome);
        final Tipo tipoTransicao = new Tipo();

        executorThreadDoBanco = Executors.newSingleThreadExecutor();


        Button adc = (Button) findViewById(R.id.btn);
        adc.setText("SALVAR");
        Intent intent = getIntent();
        final String t = intent.getStringExtra("tipo");

        rodarNaThreadDoBanco(new Runnable() {
            @Override
            public void run() {
                BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(EditarTipo.this);
                TipoDao dao = banco.tipoDao();
                Tipo tipo = dao.tipoId(t);
                tipoTransicao.id_tipo = tipo.id_tipo;
                tipoTransicao.nome = tipo.nome;
                nome.setText(tipo.nome);
            }
        });


        adc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                tipoTransicao.nome = nome.getText().toString();

                rodarNaThreadDoBanco(new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(EditarTipo.this);
                        TipoDao dao = banco.tipoDao();
                        dao.update(tipoTransicao);
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
