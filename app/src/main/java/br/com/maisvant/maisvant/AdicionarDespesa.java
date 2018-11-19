package br.com.maisvant.maisvant;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AdicionarDespesa extends AppCompatActivity {

    String item;
    ImageView thumb;
    private Executor executorThreadDoBanco;
    private Handler handlerThreadPrincipal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_despesa);
        Button adc = (Button) findViewById(R.id.btn);

        handlerThreadPrincipal = new Handler(Looper.getMainLooper());
        executorThreadDoBanco = Executors.newSingleThreadExecutor();

        Intent it = getIntent();
        final Spinner tv = (Spinner) findViewById(R.id.tipo);
        final Button gertipo = (Button) findViewById(R.id.gerenciar_tipo);
        final Button gerveiculo = (Button) findViewById(R.id.gerenciar_veiculo);
        final EditText descricao =  findViewById(R.id.cDescricao);
        final EditText valor =  findViewById(R.id.cValor);
        final Spinner veiculo =  (Spinner) findViewById(R.id.veiculo);
        final Spinner tipo =  (Spinner) findViewById(R.id.tipo);
        atualizar();
        gertipo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdicionarDespesa.this, GerenciarTipo.class);
                startActivity(intent);
            }
        });
        gerveiculo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AdicionarDespesa.this, GerenciarVeiculo.class);
                startActivity(intent);
            }
        });
        adc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Despesa despesa = new Despesa();
                despesa.descricao = descricao.getText().toString();
                Tipo tiposel = (Tipo) tipo.getSelectedItem();
                Veiculo veiculosel = (Veiculo) veiculo.getSelectedItem();

                despesa.veiculo = veiculosel;
                despesa.tipo = tiposel;
                despesa.valor = Float.parseFloat(valor.getText().toString());
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

    @Override
    protected void onResume() {
        super.onResume();
        atualizar();
    }

    public void atualizar(){
        rodarNaThreadDoBanco(
                new Runnable() {
                    @Override
                    public void run() {
                        BancoDeDespesas banco = BancoDeDespesas.obterInstanciaUnica(AdicionarDespesa.this);
                        VeiculoDao dao = banco.veiculoDao();
                        TipoDao dao1 = banco.tipoDao();
                        final Spinner veiculo =  (Spinner) findViewById(R.id.veiculo);
                        final Spinner tipo =  (Spinner) findViewById(R.id.tipo);
                        final List<Veiculo> veiculos = dao.listar();
                        final List<Tipo> tipos = dao1.listar();
                        rodarNaThreadPrincipal(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter adapter = new ArrayAdapter(AdicionarDespesa.this, android.R.layout.simple_spinner_item, veiculos);
                                ArrayAdapter adapter1 = new ArrayAdapter(AdicionarDespesa.this, android.R.layout.simple_spinner_item, tipos);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                tipo.setAdapter(adapter1);
                                veiculo.setAdapter(adapter);
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
