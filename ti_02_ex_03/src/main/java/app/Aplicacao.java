package app;

import static spark.Spark.*;

import service.ProdutoService;

public class Aplicacao {

    private static ProdutoService produtoService = new ProdutoService();

    public static void main(String[] args) {
        port(6789);

        // Rota para adicionar um produto
        post("/produto", (request, response) -> {
            // Chama o serviço para adicionar o produto no banco de dados
            return produtoService.add(request, response);
        });

        // Rota para obter um produto por ID
        get("/produto/:id", (request, response) -> {
            // Chama o serviço para buscar o produto no banco de dados
            return produtoService.get(request, response);
        });

        // Rota para atualizar um produto
        put("/produto/update/:id", (request, response) -> {
            // Chama o serviço para atualizar o produto no banco de dados
            return produtoService.update(request, response);
        });

        // Rota para excluir um produto por ID
        delete("/produto/delete/:id", (request, response) -> {
            // Chama o serviço para remover o produto no banco de dados
            return produtoService.remove(request, response);
        });

        // Rota para obter todos os produtos
        get("/produto", (request, response) -> {
            // Chama o serviço para listar todos os produtos do banco de dados
            return produtoService.getAll(request, response);
        });

    }
}