package br.com.maisvant.maisvant;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterDespesas extends BaseAdapter {

    private final List<Despesa> despesas;
    private final AppCompatActivity activity;

    public AdapterDespesas(List<Despesa> despesas, AppCompatActivity activity) {
        this.despesas = despesas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return despesas.size();
    }

    @Override
    public Object getItem(int i) {
        return despesas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return despesas.get(i).id_despesa;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View v = activity.getLayoutInflater()
                .inflate(R.layout.lista_despesa, viewGroup, false);

        Despesa despesa = despesas.get(i);
        if(despesas.size()>0) {
            TextView descricao = (TextView) v.findViewById(R.id.lista_despesa_descricao);
            TextView valor = (TextView) v.findViewById(R.id.lista_despesa_valor);
            TextView tipo = (TextView) v.findViewById(R.id.lista_despesa_tipo);
            tipo.setText(""+despesa.tipo.nome);
            valor.setText("R$ " + despesa.valor);
            descricao.setText(despesa.descricao);
        }

        return v;
    }
}
