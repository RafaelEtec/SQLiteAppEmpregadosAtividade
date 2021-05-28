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
    EditText txtNomeLivro, txtAutorLivro, txtGeneroLivro, txtValorLivro;
    Spinner spnQualidades;

    Button btnAdcionaLivro;

    SQLiteDatabase meuBancoDeDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblLivros = findViewById(R.id.lblVisualizaPrateleira);
        txtNomeLivro = findViewById(R.id.txtNomeNovoLivro);
        txtAutorLivro = findViewById(R.id.txtAutorNovoLivro);
        txtGeneroLivro = findViewById(R.id.txtGeneroNovoLivro);
        txtValorLivro = findViewById(R.id.txtValorNovoLivro);
        spnQualidades = findViewById(R.id.spnQualidades);

        btnAdcionaLivro = findViewById(R.id.btnAdicionarLivro);

        btnAdcionaLivro.setOnClickListener(this);

        lblLivros.setOnClickListener(this);

        //Criando banco de dados

        meuBancoDeDados = openOrCreateDatabase(NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        criarPrateleiraLivros();
    }

//Este método irá validar o nome e o salário
    //departamento não precisa de validação, pois é um spinner e não pode estar vazio

    private boolean verificarEntrada(String nome, String autor, String genero, String valor) {
        if (nome.isEmpty()) {
            txtNomeLivro.setError("Por favor entre com o nome");
            txtNomeLivro.requestFocus();
            return false;
        }

        if (autor.isEmpty()) {
            txtAutorLivro.setError("Por favor entre com o autor");
            txtAutorLivro.requestFocus();
            return false;
        }

        if (genero.isEmpty()) {
            txtGeneroLivro.setError("Por favor entre com o autor");
            txtGeneroLivro.requestFocus();
            return false;
        }

        if (valor.isEmpty() || Integer.parseInt(valor) <= 0) {
            txtValorLivro.setError("Por favor entre com o valor");
            txtValorLivro.requestFocus();
            return false;
        }
        return true;
    }

    //Neste método vamos fazer a operação para adicionar os funcionario
    private void adicionarLivro() {

        Locale meuLocal = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(meuLocal);


        String nomeLivro = txtNomeLivro.getText().toString().trim();
        String autorLivro = txtAutorLivro.getText().toString().trim();
        String generoLivro = txtGeneroLivro.getText().toString().trim();
        String valorLivro = txtValorLivro.getText().toString().trim();
        String qualLivro = spnQualidades.getSelectedItem().toString();

        // obtendo o horário atual para data de inclusão

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dataInclusao = simpleDateFormat.format(calendar.getTime());


        //validando entrada
        if (verificarEntrada(nomeLivro, autorLivro, generoLivro, valorLivro)) {

            String insertSQL = "INSERT INTO prateleira (" +
                    "nome, " +
                    "autor, " +
                    "genero, " +
                    "qualidade, " +
                    "dataInclusao," +
                    "valor)" +
                    "VALUES(?, ?, ?, ?, ?);";

            // usando o mesmo método execsql para inserir valores
            // desta vez tem dois parâmetros
            // primeiro é a string sql e segundo são os parâmetros que devem ser vinculados à consulta

            meuBancoDeDados.execSQL(insertSQL, new String[]{nomeLivro, autorLivro, generoLivro, qualLivro, dataInclusao, valorLivro});

            Toast.makeText(getApplicationContext(), "Livro adicionado com sucesso!!!", Toast.LENGTH_SHORT).show();
            limparCadastro();
        }

    }

    public void limparCadastro() {

        txtNomeLivro.setText("");
        txtAutorLivro.setText("");
        txtGeneroLivro.setText("");
        txtValorLivro.setText("");
        txtNomeLivro.requestFocus();
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
    // este método irá criar a tabela
    // como vamos chamar esse método toda vez que lançarmos o aplicativo
    // Eu adicionei IF NOT EXISTS ao SQL
    // então, só criará a tabela quando a tabela ainda não estiver criada

    private void criarPrateleiraLivros() {
        meuBancoDeDados.execSQL(
                "CREATE TABLE IF NOT EXISTS prateleira (" +
                        "id integer PRIMARY KEY AUTOINCREMENT," +
                        "nome varchar(200) NOT NULL," +
                        "autor varchar(200) NOT NULL," +
                        "genero varchar(200) NOT NULL," +
                        "qualidade varchar(200) NOT NULL," +
                        "dataInclusao datetime NOT NULL," +
                        "valor double NOT NULL );"
        );
    }
}