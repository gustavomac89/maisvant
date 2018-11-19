package br.com.maisvant.maisvant;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Orcamento {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Integer combustivel;
    public Integer estacionamento;
    public Integer manutencao;
    public Integer outros;


}