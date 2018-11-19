package br.com.maisvant.maisvant;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DespesaDao {
    @Insert
    void inserir(Despesa item);

    @Query("SELECT * from Despesa")
    List<Despesa> listar();

    @Query("SELECT sum(valor) from Despesa where id_tipo = :tipo")
    int getValorDespesas(int tipo);

    @Delete
    void delete(Despesa item);

}