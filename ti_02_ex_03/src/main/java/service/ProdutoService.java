package service;

import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;

public class ProdutoService {

    private ProdutoDAO produtoDAO;

    public ProdutoService() {
        produtoDAO = new ProdutoDAO();
    }

    public Object add(Request request, Response response) {
        String descricao = request.queryParams("descricao");
        float preco = Float.parseFloat(request.queryParams("preco"));
        int quantidade = Integer.parseInt(request.queryParams("quantidade"));
        String dataFabricacao = request.queryParams("dataFabricacao");
        String dataValidade = request.queryParams("dataValidade");

        Produto produto = new Produto(
            produtoDAO.getMaxId() + 1,
            descricao,
            preco,
            quantidade,
            java.time.LocalDateTime.parse(dataFabricacao),
            java.time.LocalDate.parse(dataValidade)
        );

        produtoDAO.add(produto);
        response.status(201);
        return produto.getId();
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);

        if (produto != null) {
            response.header("Content-Type", "application/xml");
            response.header("Content-Encoding", "UTF-8");

            return "<produto>\n" + 
                   "\t<id>" + produto.getId() + "</id>\n" +
                   "\t<descricao>" + produto.getDescricao() + "</descricao>\n" +
                   "\t<preco>" + produto.getPreco() + "</preco>\n" +
                   "\t<quantidade>" + produto.getQuant() + "</quantidade>\n" +
                   "\t<fabricacao>" + produto.getDataFabricacao() + "</fabricacao>\n" +
                   "\t<validade>" + produto.getDataValidade() + "</validade>\n" +
                   "</produto>\n";
        } else {
            response.status(404);
            return "Produto " + id + " não encontrado.";
        }
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);

        if (produto != null) {
            produto.setDescricao(request.queryParams("descricao"));
            produto.setPreco(Float.parseFloat(request.queryParams("preco")));
            produto.setQuant(Integer.parseInt(request.queryParams("quantidade")));
            produto.setDataFabricacao(java.time.LocalDateTime.parse(request.queryParams("dataFabricacao")));
            produto.setDataValidade(java.time.LocalDate.parse(request.queryParams("dataValidade")));

            produtoDAO.update(produto);
            return produto.getId();
        } else {
            response.status(404);
            return "Produto não encontrado.";
        }
    }

    public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Produto produto = produtoDAO.get(id);

        if (produto != null) {
            produtoDAO.remove(produto);
            response.status(200);
            return id;
        } else {
            response.status(404);
            return "Produto não encontrado.";
        }
    }
}