package br.com.maisvant.maisvant;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Despesa {
    @PrimaryKey (autoGenerate = true)
    public int id;
    public int tipo;
    public String valor;
    public String dia;
    public String descricao;

    public void converterItem(String item) {
        String[] opc = { "Combustivel", "Manutenção","Estacionamento","Outros" };
        for( int i = 0; i < opc.length - 1; i++)
        {
            if(opc[i].equals(item)){
            this.tipo = i;
        }
        }
    }

    @Override
    public String toString() {
        return "Tipo: "+ tipo + " - " + descricao + " - Valor: " + valor;
    }
}

