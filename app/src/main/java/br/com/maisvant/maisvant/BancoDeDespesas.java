package br.com.maisvant.maisvant;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Despesa.class}, version = 1, exportSchema = false)
public abstract class BancoDeDespesas extends RoomDatabase {
    public abstract  DespesaDao despesaDao();
}