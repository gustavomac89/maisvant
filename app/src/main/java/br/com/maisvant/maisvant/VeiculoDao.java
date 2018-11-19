package br.com.maisvant.maisvant;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface VeiculoDao {
    @Insert
    void inserir(Veiculo item);

    @Update
    void update(Veiculo item);

    @Query("SELECT * from Veiculo")
    List<Veiculo> listar();

    @Query("SELECT * from Veiculo  where id_veiculo = :id LIMIT 1")
    Veiculo veiculoId(String id);

    @Query("SELECT count(id_veiculo) from Veiculo where id_veiculo = :id")
    int getQuantidade(int id);

    @Delete
    void delete(Veiculo item);
}