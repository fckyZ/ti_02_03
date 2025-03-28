package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection conexao;

    public ProdutoDAO() {
        conexao = null;
    }

    public boolean conectar() {
        String driverName = "org.postgresql.Driver";
        String serverName = "localhost";
        String mydatabase = "ativ";
        int porta = 5432;
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
        String username = "ti2cc";
        String password = "Gugu2005";
        boolean status = false;

        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, username, password);
            status = conexao != null;
        } catch (ClassNotFoundException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
        }

        return status;
    }

    public boolean close() {
        boolean status = false;
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                status = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    public boolean inserirProduto(Produto produto) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("INSERT INTO produto (id, descricao, preco, quantidade, data_fabricacao, data_validade) "
                    + "VALUES (" + produto.getId() + ", '" + produto.getDescricao() + "', " + produto.getPreco() + ", "
                    + produto.getQuant() + ", '" + produto.getDataFabricacao() + "', '" + produto.getDataValidade() + "');");
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Produto get(int id) {
        Produto produto = null;
        try {
            Statement st = conexao.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM produto WHERE id = " + id);
            if (rs.next()) {
                produto = new Produto(rs.getInt("id"), rs.getString("descricao"), rs.getFloat("preco"), 
                        rs.getInt("quantidade"), rs.getTimestamp("data_fabricacao").toLocalDateTime(), 
                        rs.getDate("data_validade").toLocalDate());
            }
            st.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return produto;
    }

    public void update(Produto produto) {
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("UPDATE produto SET descricao = '" + produto.getDescricao() + "', preco = " + produto.getPreco() 
                + ", quantidade = " + produto.getQuant() + ", data_fabricacao = '" + produto.getDataFabricacao() 
                + "', data_validade = '" + produto.getDataValidade() + "' WHERE id = " + produto.getId());
            st.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public void remove(Produto produto) {
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM produto WHERE id = " + produto.getId());
            st.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
    }

    public List<Produto> getAll() {
        List<Produto> produtos = new ArrayList<>();
        try {
            Statement st = conexao.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM produto");
            while (rs.next()) {
                Produto produto = new Produto(rs.getInt("id"), rs.getString("descricao"), rs.getFloat("preco"), 
                        rs.getInt("quantidade"), rs.getTimestamp("data_fabricacao").toLocalDateTime(), 
                        rs.getDate("data_validade").toLocalDate());
                produtos.add(produto);
            }
            st.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return produtos;
    }
}