package br.com.maisvant.maisvant;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity
public class Despesa {
    @PrimaryKey(autoGenerate = true)
    public int id_despesa;
    @Embedded
    public Tipo tipo;
    @Embedded
    public Veiculo veiculo;
    public Float valor;
    public String dia;
    public String descricao;

}