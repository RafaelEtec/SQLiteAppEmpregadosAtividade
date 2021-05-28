package br.com.local.sqliteappempregados;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PrateleiraAdapter extends ArrayAdapter<Livros> {

    Context mCtx;
    int listaLayoutRes;
    List<Livros> listaLivros;
    SQLiteDatabase meuBancoDeDados;

    public PrateleiraAdapter(Context mCtx, int listaLayoutRes, List<Livros> listaLivros, SQLiteDatabase meuBancoDeDados) {
        super(mCtx, listaLayoutRes, listaLivros);

        this.mCtx = mCtx;
        this.listaLayoutRes = listaLayoutRes;
        this.listaLivros = listaLivros;
        this.meuBancoDeDados = meuBancoDeDados;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listaLayoutRes, null);

        final Livros livros = listaLivros.get(position);

        TextView txtViewNome = view.findViewById(R.id.txtNomeViewLivro);
        TextView txtViewAutor = view.findViewById(R.id.txtAutorViewLivro);
        TextView txtViewGenero = view.findViewById(R.id.txtGeneroViewLivro);
        TextView txtViewQual = view.findViewById(R.id.txtQualidadeViewlivro);
        TextView txtViewValor = view.findViewById(R.id.txtValorViewLivro);
        TextView txtViewDataInclusao = view.findViewById(R.id.txtInclusaoViewLivro);

        txtViewNome.setText(livros.getNome());
        txtViewAutor.setText(livros.getAutor());
        txtViewGenero.setText(livros.getGenero());
        txtViewQual.setText(livros.getQualidade());
        txtViewValor.setText(String.valueOf(livros.getValor()));
        txtViewDataInclusao.setText(livros.getDataInclusaoSistema());

        Button btnExcluirLivro = view.findViewById(R.id.btnExcluirViewLivro);
        Button btnEditarLivro = view.findViewById(R.id.btnEditarViewLivro);

        btnEditarLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarLivro(livros);
            }
        });

        btnExcluirLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Deseja excluir?");
                builder.setIcon(android.R.drawable.ic_notification_clear_all);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM prateleira WHERE id = ?";
                        meuBancoDeDados.execSQL(sql, new Integer[]{livros.getId()});
                        recarregarPrateleiraDB();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    public void alterarLivro(final Livros livros) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.caixa_alterar_livro, null);
        builder.setView(view);

        final EditText txtEditarNome = view.findViewById(R.id.txtEditarNome);
        final EditText txtEditarAutor = view.findViewById(R.id.txtEditarAutor);
        final EditText txtEditarGenero = view.findViewById(R.id.txtEditarGenero);
        final EditText txtEditarValor = view.findViewById(R.id.txtEditarValor);
        final Spinner spnQualidades = view.findViewById(R.id.spnQualidades);

        txtEditarNome.setText(livros.getNome());
        txtEditarAutor.setText(livros.getAutor());
        txtEditarGenero.setText(livros.getGenero());
        txtEditarValor.setText(String.valueOf(livros.getValor()));

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnAlterarLivro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = txtEditarNome.getText().toString().trim();
                String autor = txtEditarAutor.getText().toString().trim();
                String genero = txtEditarGenero.getText().toString().trim();
                String valor = txtEditarValor.getText().toString().trim();
                String qualidades = spnQualidades.getSelectedItem().toString().trim();

                if (nome.isEmpty()) {
                    txtEditarNome.setError("Nome está em branco");
                    txtEditarNome.requestFocus();
                    return;
                }
                if (autor.isEmpty()) {
                    txtEditarAutor.setError("Autor está em branco");
                    txtEditarAutor.requestFocus();
                    return;
                }
                if (genero.isEmpty()) {
                    txtEditarGenero.setError("Gênero está em branco");
                    txtEditarGenero.requestFocus();
                    return;
                }
                if (valor.isEmpty()) {
                    txtEditarValor.setError("Valor está em branco");
                    txtEditarValor.requestFocus();
                    return;
                }

                String sql = "UPDATE prateleira SET nome = ?, autor = ?, genero = ?, qualidades = ?, valor = ? WHERE id = ?";
                meuBancoDeDados.execSQL(sql,
                        new String[]{nome, qualidades, valor, String.valueOf(livros.getId())});
                Toast.makeText(mCtx, "Livro alterado com sucesso!!!", Toast.LENGTH_LONG).show();

                recarregarPrateleiraDB();

                dialog.dismiss();
            }
        });
    }

    public void recarregarPrateleiraDB() {
        Cursor cursorLivros = meuBancoDeDados.rawQuery("SELECT * FROM prateleira", null);

        if (cursorLivros.moveToFirst()) {
            listaLivros.clear();
            do {
                listaLivros.add(new Livros(
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
        notifyDataSetChanged();
    }
}
