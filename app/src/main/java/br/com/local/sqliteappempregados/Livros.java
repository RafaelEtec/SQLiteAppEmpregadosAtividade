package br.com.local.sqliteappempregados;

public class Livros {
    int id;
    String nome, autor, qualidade, genero, dataInclusaoSistema;
    double valor;

    public Livros(int id, String nome, String autor, String qualidade, String genero, String dataInclusaoSistema, double valor) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.genero = genero;
        this.qualidade = qualidade;
        this.dataInclusaoSistema = dataInclusaoSistema;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getQualidade() {
        return qualidade;
    }

    public void setQualidade() {
        this.qualidade = qualidade;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDataInclusaoSistema() {
        return dataInclusaoSistema;
    }

    public void setDataInclusaoSistema(String dataInclusaoSistema) {
        this.dataInclusaoSistema = dataInclusaoSistema;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
