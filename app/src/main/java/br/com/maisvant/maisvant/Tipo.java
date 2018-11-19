package br.com.maisvant.maisvant;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Tipo {
    @PrimaryKey(autoGenerate = true)
    public int id_tipo;
    public String nome;


    @Override
    public String toString()
    {
        return nome;
    }
}

