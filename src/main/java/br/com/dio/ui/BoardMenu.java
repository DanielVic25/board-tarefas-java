package br.com.dio.ui;

import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
public class BoardMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final BoardEntity entity;


    public void execute() {
        System.out.printf("Bem vindo ao board %s, selecione a operação desejada", entity.getId());
        var option = -1;
        while (option != 9) {
            System.out.println("1 - Criar um card");
            System.out.println("2 - Mover um card");
            System.out.println("3 - Bloquer um card");
            System.out.println("4 - Desbloquear um card");
            System.out.println("5 - Cancelar um card");
            System.out.println("6 - Ver Board");
            System.out.println("7 - Ver colunas");
            System.out.println("8 - Ver coluna com cards");
            System.out.println("9 - ver card");
            System.out.println("10 - Sair");
            option = scanner.nextInt();
            switch (option) {
                case 1 -> crateCard();
                case 2 -> moveCardToNextColumn();
                case 3 -> blockCard();
                case 4 -> unblockCard();
                case 5 -> cancelCard();
                case 6 -> showBoard();
                case 7 -> showColumn();
                case 8 -> showCard();
                case 9 -> System.out.println("Voltando para o menu anterior um card");
                case 10 -> System.exit(0);
                default -> System.out.println("Opção Invalida, informe uma das Opções acima!!");
            }
        }
    }

    private void showCard() {

    }

    private void showColumn() {
        
    }

    private void showBoard() {
        
    }

    private void cancelCard() {
        
    }

    private void unblockCard() {
        
    }

    private void blockCard() {
        
    }

    private void moveCardToNextColumn() {
        
    }

    private void crateCard() {
        
        
    }
}
