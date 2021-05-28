package br.com.local.sqliteappempregados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String NOME_BANCO_DE_DADOS = "bdPrateleira.db";

    TextView lblLivros;
    EditText txtNomeNovoLivro, txtAutorNovoLivro, txtGeneroNovoLivro, txtValorNovoLivro;
    Spinner spnQualidades;

    Button btnAdcionaLivro;

    SQLiteDatabase meuBancoDeDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblLivros = findViewById(R.id.lblVisualizaPrateleira);
        txtNomeNovoLivro = findViewById(R.id.txtNomeNovoLivro);
        txtAutorNovoLivro = findViewById(R.id.txtAutorNovoLivro);
        txtGeneroNovoLivro = findViewById(R.id.txtGeneroNovoLivro);
        txtValorNovoLivro = findViewById(R.id.txtValorNovoLivro);
        spnQualidades = findViewById(R.id.spnQualidades);

        btnAdcionaLivro = findViewById(R.id.btnAdicionarLivro);

        btnAdcionaLivro.setOnClickListener(this);

        lblLivros.setOnClickListener(this);

        meuBancoDeDados = openOrCreateDatabase(NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        criarPrateleiraLivros();
    }

    private boolean verificarEntrada(String nome, String autor, String genero, String valor) {
        if (nome.isEmpty()) {
            txtNomeNovoLivro.setError("Por favor entre com o nome");
            txtNomeNovoLivro.requestFocus();
            return false;
        }

        if (autor.isEmpty()) {
            txtAutorNovoLivro.setError("Por favor entre com o autor");
            txtAutorNovoLivro.requestFocus();
            return false;
        }

        if (genero.isEmpty()) {
            txtGeneroNovoLivro.setError("Por favor entre com o autor");
            txtGeneroNovoLivro.requestFocus();
            return false;
        }

        if (valor.isEmpty() || Integer.parseInt(valor) <= 0) {
            txtValorNovoLivro.setError("Por favor entre com o valor");
            txtValorNovoLivro.requestFocus();
            return false;
        }
        return true;
    }

    private void adicionarLivro() {

        String nomeLivro = txtNomeNovoLivro.getText().toString().trim();
        String autorLivro = txtAutorNovoLivro.getText().toString().trim();
        String generoLivro = txtGeneroNovoLivro.getText().toString().trim();
        String qualLivro = spnQualidades.getSelectedItem().toString();
        String valorLivro = txtValorNovoLivro.getText().toString().trim();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dataInclusao = simpleDateFormat.format(calendar.getTime());

        if (verificarEntrada(nomeLivro, autorLivro, generoLivro, valorLivro)) {

            String insertSQL = "INSERT INTO prateleira (" +
                    "nome, " +
                    "autor, " +
                    "genero, " +
                    "qualidades, " +
                    "dataInclusao," +
                    "valor)" +
                    "VALUES(?, ?, ?, ?, ?, ?);";

            meuBancoDeDados.execSQL(insertSQL, new String[]{nomeLivro, autorLivro, generoLivro, qualLivro, dataInclusao, valorLivro});

            Toast.makeText(getApplicationContext(), "Livro adicionado com sucesso!!!", Toast.LENGTH_SHORT).show();
            limparCadastro();
        }

    }

    public void limparCadastro() {

        txtNomeNovoLivro.setText("");
        txtAutorNovoLivro.setText("");
        txtGeneroNovoLivro.setText("");
        txtValorNovoLivro.setText("");
        txtNomeNovoLivro.requestFocus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdicionarLivro:
                adicionarLivro();
                break;
            case R.id.lblVisualizaPrateleira:
                startActivity(new Intent(getApplicationContext(), Prateleira_Activity.class));
                break;
        }

    }

    private void criarPrateleiraLivros() {
        meuBancoDeDados.execSQL(
                "CREATE TABLE IF NOT EXISTS prateleira (" +
                        "id integer PRIMARY KEY AUTOINCREMENT," +
                        "nome varchar(200) NOT NULL," +
                        "autor varchar(200) NOT NULL," +
                        "genero varchar(200) NOT NULL," +
                        "qualidades varchar(200) NOT NULL," +
                        "dataInclusao datetime NOT NULL," +
                        "valor double NOT NULL );"
        );
    }
}