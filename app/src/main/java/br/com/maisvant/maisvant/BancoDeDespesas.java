package br.com.maisvant.maisvant;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {Despesa.class, Orcamento.class, Tipo.class, Veiculo.class}, version = 6, exportSchema = false)
public abstract class BancoDeDespesas extends RoomDatabase {

    private static BancoDeDespesas instancia;
    public static BancoDeDespesas obterInstanciaUnica(
            Context context) {
        synchronized (BancoDeDespesas.class) {
            if (instancia == null)
                instancia = Room.databaseBuilder(
                        context.getApplicationContext(),
                        BancoDeDespesas.class,
                        "banco").build();

            return instancia;
        }
    }
    public abstract  DespesaDao despesaDao();
    public abstract  OrcamentoDao orcamentoDao();
    public abstract  TipoDao tipoDao();
    public abstract  VeiculoDao veiculoDao();
}