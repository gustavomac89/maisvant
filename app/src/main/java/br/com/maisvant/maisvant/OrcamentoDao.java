package br.com.maisvant.maisvant;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface OrcamentoDao {
    @Insert
    void inserir(Orcamento orcamento);

    @Update
    void atualizar(Orcamento orcamento);

    @Query("SELECT * from Orcamento ORDER BY id DESC LIMIT 1;")
    Orcamento consulta();

    @Delete
    void delete(Orcamento orcamento);
}