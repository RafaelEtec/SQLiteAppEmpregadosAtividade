package br.com.local.sqliteappempregados;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Prateleira_Activity extends AppCompatActivity {

    List<Livros> livrosList;
    PrateleiraAdapter prateleiraAdapter;
    SQLiteDatabase meuBancoDeDados;
    ListView listViewPrateleira;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prateleira_layout);

        listViewPrateleira = findViewById(R.id.listarLivrosView);
        livrosList = new ArrayList<>();

        meuBancoDeDados = openOrCreateDatabase(MainActivity.NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        visualizarPrateleiraDatabase();
    }

    private void visualizarPrateleiraDatabase() {
        Cursor cursorLivros = meuBancoDeDados.rawQuery("SELECT * FROM prateleira", null);

        if (cursorLivros.moveToFirst()) {
            do {
                livrosList.add(new Livros(
                        cursorLivros.getInt(0),
                        cursorLivros.getString(1),
                        cursorLivros.getString(2),
                        cursorLivros.getString(3),
                        cursorLivros.getString(4),
                        cursorLivros.getString(5),
                        cursorLivros.getDouble(6)
                ));
            } while (cursorLivros.moveToNext());
        }
        cursorLivros.close();

        prateleiraAdapter = new PrateleiraAdapter(this, R.layout.lista_view_prateleira, livrosList, meuBancoDeDados);
        
        listViewPrateleira.setAdapter(prateleiraAdapter);
    }
}