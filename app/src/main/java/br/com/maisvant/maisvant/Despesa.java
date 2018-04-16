package br.com.maisvant.maisvant;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Despesa {
    @PrimaryKey (autoGenerate = true)
    public int id;
    public int tipo;
    public double valor;
    public Date data;
    public String descricao;
}