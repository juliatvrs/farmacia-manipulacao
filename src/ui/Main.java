package ui;

import java.util.Scanner;
import modelo.Medicamento;
import servico.Farmacia;

/**
 * Interface do sistema: o menu em loop no console.
 * Todo o controle do sistema é acessado por aqui.
 * Cada pessoa edita apenas a sua seção, para evitar conflitos no Git.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Farmacia farmacia = new Farmacia();

        // TODO (Júlia): carregar os dados ao iniciar, repetir o menu principal
        // até o usuário escolher sair e salvar os dados ao encerrar
        // (tratar entradas inválidas sem encerrar o programa)

        scanner.close();
    }

    // ===== JÚLIA: menu principal, pessoas e leitura segura =====

    private static void menuPessoas(Scanner scanner, Farmacia farmacia) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // lê um número inteiro repetindo a pergunta enquanto a entrada for inválida
    private static int lerInteiro(Scanner scanner, String mensagem) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // ===== VÍTOR: matérias-primas e montagem de medicamento =====

    private static void menuMateriasPrimas(Scanner scanner, Farmacia farmacia) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // pergunta o tipo (cápsula, creme ou xarope), os dados e os itens da fórmula
    private static Medicamento montarMedicamento(Scanner scanner, Farmacia farmacia) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // ===== JAIR: pedidos =====

    private static void menuPedidos(Scanner scanner, Farmacia farmacia) {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}
