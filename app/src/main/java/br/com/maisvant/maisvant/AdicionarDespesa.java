package br.com.maisvant.maisvant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AdicionarDespesa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_despesa);
        Intent it = getIntent();
        String item = it.getStringExtra("item");
        TextView tv = (TextView) findViewById(R.id.tipo);
        tv.setText(item);
    }
}
