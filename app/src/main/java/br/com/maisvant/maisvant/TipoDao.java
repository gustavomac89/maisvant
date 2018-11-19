package br.com.maisvant.maisvant;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TipoDao {
    @Insert
    void inserir(Tipo tipo);

    @Query("SELECT * from Tipo")
    List<Tipo> listar();

    @Update
    void update(Tipo item);


    @Query("SELECT * from Tipo  where id_tipo = :id LIMIT 1")
    Tipo tipoId(String id);

    @Delete
    void delete(Tipo tipo);
}