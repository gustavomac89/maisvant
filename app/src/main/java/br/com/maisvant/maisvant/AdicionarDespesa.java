package br.com.maisvant.maisvant;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdicionarDespesa extends AppCompatActivity {

    String item;
    private Handler handlerThreadPrincipal;
    private Executor executorThreadDoBanco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_despesa);
        Button adc = (Button) findViewById(R.id.btn);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();

        Intent it = getIntent();
        item = it.getStringExtra("item");
        TextView tv = (TextView) findViewById(R.id.tipo);
        tv.setText(item);


        adc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                EditText descricao =  findViewById(R.id.cDescricao);
                EditText valor =  findViewById(R.id.cValor);
                final Despesa despesa = new Despesa();

                despesa.descricao = descricao.getText().toString();
                despesa.valor = valor.getText().toString();
                despesa.converterItem(item);
                Date hoje = new Date();
                despesa.dia = hoje.toString();
                rodarNaThreadDoBanco(new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(AdicionarDespesa.this);
                        DespesaDao dao = banco.despesaDao();
                        dao.inserir(despesa);
                        finish();
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
