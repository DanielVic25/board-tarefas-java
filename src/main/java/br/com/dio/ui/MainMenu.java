package br.com.dio.ui;

import br.com.dio.exception.RegraNegocioException;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.service.BoardQueryService;
import br.com.dio.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.CANCEL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.FINAL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.INITIAL;
import static br.com.dio.persistence.entity.BoardColumnKindEnum.PENDING;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);

    public void executar() throws SQLException {
        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        var option = -1;
        while (true) {
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            option = scanner.nextInt();
            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção Invalida, informe uma das Opções acima!!");
            }
        }
    }

    private void createBoard() {
        var entity = new BoardEntity();
        System.out.println("Informe o nome do seu board");
        entity.setName(scanner.next());

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim informe quantas, se não digite \"0\"");
        var additionalColumns = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do board");
        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna tarefa pedente do board");
            var pendingColumnName = scanner.next();
            // Corrigido: Estavas a passar initialColumnName
            var pendingColumn = createColumn(pendingColumnName, PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final");
        var finalColumnName = scanner.next();
        // Corrigido: Estavas a passar initialColumnName
        var finalColumn = createColumn(finalColumnName, FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna cancelamento do board");
        var cancelColumnName = scanner.next();
        // Corrigido: Estavas a passar initialColumnName
        var cancelColumn = createColumn(cancelColumnName, CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        entity.setBoardColumn(columns);

        var service = new BoardService();
        try {
            service.insert(entity);
            System.out.println("Board criado com sucesso!");
        } catch (RegraNegocioException e) {
            System.err.println("Erro de Negócio: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro crítico de banco de dados: " + e.getMessage());
        }
    }

    private void selectBoard() throws SQLException {
        System.out.println("Informe o id do board que deseja selecionar: ");
        var id = scanner.nextLong(); // Corrigido: "scaner" para "scanner"

        // Mantive a abertura da conexão aqui, pois o BoardQueryService ainda não foi refatorado
        try (var connection = getConnection()) {
            var queryService = new BoardQueryService(connection);
            var optional = queryService.findById(id);

            // Removida a lógica duplicada de abertura do menu
            if (optional.isPresent()) {
                var boardMenu = new BoardMenu(optional.get());
                boardMenu.execute();
            } else {
                System.out.printf("Não foi encontrado um board com id %s\n", id);
            }
        }
    }

    private void deleteBoard() throws SQLException {
        System.out.println("Informe o id do board que será excluido: ");
        var id = scanner.nextLong();

        var service = new BoardService();
        if (service.delete(id)) {
            System.out.printf("O board %s foi excluido\n", id);
        } else {
            System.out.printf("Não foi encontrado um board com id %s\n", id);
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum kind, final int order) {
        var boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }
}