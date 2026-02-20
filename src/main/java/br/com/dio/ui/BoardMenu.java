package br.com.dio.ui;

import br.com.dio.persistence.entity.BlockEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.persistence.entity.CardEntity;
import br.com.dio.service.CardService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;
import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final BoardEntity entity;

    public void execute() {
        System.out.printf("Bem vindo ao board %s, selecione a operação desejada\n", entity.getName());
        var option = -1;
        while (option != 9) {
            System.out.println("1 - Criar um card");
            System.out.println("2 - Mover um card");
            System.out.println("3 - Bloquear um card");
            System.out.println("4 - Desbloquear um card");
            System.out.println("5 - Cancelar um card");
            System.out.println("6 - Sair do Board");
            option = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha

            try {
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> {
                        System.out.println("Saindo do board...");
                        return;
                    }
                    default -> System.out.println("Opção Invalida, informe uma das Opções acima!!");
                }
            } catch (SQLException e) {
                System.out.println("Erro ao executar operação no banco: " + e.getMessage());
            }
        }
    }

    private void createCard() throws SQLException {
        var card = new CardEntity();
        System.out.println("Informe o título do card:");
        card.setTitle(scanner.nextLine());
        System.out.println("Informe a descrição do card:");
        card.setDescription(scanner.nextLine());

        // Busca a coluna inicial (INITIAL) do board atual
        var initialColumn = entity.getBoardColumn().stream()
                .filter(c -> c.getKind().equals(BoardColumnKindEnum.INITIAL))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Coluna inicial não encontrada"));

        card.getBoardColumn().setId(initialColumn.getId());

        try (var connection = getConnection()) {
            new CardService(connection).create(card);
            System.out.println("Card criado com ID: " + card.getId());
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.println("Informe o ID do card:");
        var cardId = scanner.nextLong();
        System.out.println("Informe o ID da coluna de destino:");
        var columnId = scanner.nextLong();

        try (var connection = getConnection()) {
            new CardService(connection).moveToColumn(columnId, cardId);
            System.out.println("Card movido com sucesso!");
        }
    }

    private void blockCard() throws SQLException {
        System.out.println("Informe o ID do card para bloquear:");
        var cardId = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Informe o motivo do bloqueio:");
        var reason = scanner.nextLine();

        var block = new BlockEntity();
        block.setBlockReason(reason);
        block.getCard().setId(cardId);

        try (var connection = getConnection()) {
            new CardService(connection).block(block);
            System.out.println("Card bloqueado.");
        }
    }

    private void unblockCard() throws SQLException {
        System.out.println("Informe o ID do card para desbloquear:");
        var cardId = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Informe o motivo do desbloqueio:");
        var reason = scanner.nextLine();

        try (var connection = getConnection()) {
            new CardService(connection).unblock(cardId, reason);
            System.out.println("Card desbloqueado.");
        }
    }

    private void cancelCard() throws SQLException {
        System.out.println("Informe o ID do card para cancelar:");
        var cardId = scanner.nextLong();

        var cancelColumn = entity.getBoardColumn().stream()
                .filter(c -> c.getKind().equals(BoardColumnKindEnum.CANCEL))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Coluna de cancelamento não encontrada"));

        try (var connection = getConnection()) {
            new CardService(connection).moveToColumn(cancelColumn.getId(), cardId);
            System.out.println("Card movido para cancelamento.");
        }
    }
}