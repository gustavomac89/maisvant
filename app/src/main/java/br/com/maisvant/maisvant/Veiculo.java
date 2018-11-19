package br.com.maisvant.maisvant;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Veiculo {
    @PrimaryKey(autoGenerate = true)
    public int id_veiculo;
    public String modelo;
    public String marca;
    public String placa;


    @Override
    public String toString()
    {
        return modelo;
    }
}

