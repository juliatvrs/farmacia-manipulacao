package ui;

import java.util.ArrayList;
import java.util.Scanner;
import modelo.Cliente;
import modelo.Farmaceutico;
import modelo.Medicamento;
import modelo.Pessoa;
import servico.Farmacia;

/**
 * Interface do sistema: o menu em loop no console.
 * Todo o controle do sistema é acessado por aqui.
 * Cada pessoa edita apenas a sua seção, para evitar conflitos no Git.
 */
public class Main {

    // [RNF02] menu principal em loop até o usuário escolher sair; os dados são carregados ao
    // iniciar e salvos ao voltar de cada submenu e ao sair (RNF04), para nada se perder
    // mesmo que o programa seja fechado de forma inesperada
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Farmacia farmacia = new Farmacia();
        farmacia.carregarDados();

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== PHARMAVIDA: FARMÁCIA DE MANIPULAÇÃO ===");
            System.out.println("1 - Pessoas");
            System.out.println("2 - Matérias-primas");
            System.out.println("3 - Pedidos");
            System.out.println("0 - Sair");
            opcao = lerInteiro(scanner, "Escolha: ");
            switch (opcao) {
                case 1:
                    menuPessoas(scanner, farmacia);
                    farmacia.salvarDados();
                    break;
                case 2:
                    // TODO (Júlia): ativar quando a parte do Vítor for juntada na principal
                    // menuMateriasPrimas(scanner, farmacia);
                    // farmacia.salvarDados();
                    System.out.println("Em desenvolvimento.");
                    break;
                case 3:
                    // TODO (Júlia): ativar quando a parte do Jair for juntada na principal
                    // menuPedidos(scanner, farmacia);
                    // farmacia.salvarDados();
                    System.out.println("Em desenvolvimento.");
                    break;
                case 0:
                    farmacia.salvarDados();
                    System.out.println("Dados salvos. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    // ===== JÚLIA: menu principal, pessoas e leitura segura =====

    // [RNF02] submenu de pessoas em loop até o usuário escolher voltar; erros de validação
    // lançados pela Farmacia e pelos setters são capturados aqui, mostrando a mensagem
    // em vez de derrubar o programa
    private static void menuPessoas(Scanner scanner, Farmacia farmacia) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== PESSOAS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar por CPF");
            System.out.println("4 - Editar");
            System.out.println("5 - Remover");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(scanner, "Escolha: ");
            try {
                switch (opcao) {
                    case 1:
                        opcaoCadastrarPessoa(scanner, farmacia);
                        break;
                    case 2:
                        opcaoListarPessoas(farmacia);
                        break;
                    case 3:
                        opcaoBuscarPessoa(scanner, farmacia);
                        break;
                    case 4:
                        opcaoEditarPessoa(scanner, farmacia);
                        break;
                    case 5:
                        opcaoRemoverPessoa(scanner, farmacia);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    // [POLIMORFISMO] percorre a lista de Pessoa e chama exibirDados() em cada uma: o mesmo
    // comando mostra o texto de um cliente ou de um farmacêutico, conforme o objeto real
    private static void opcaoListarPessoas(Farmacia farmacia) {
        ArrayList<Pessoa> pessoas = farmacia.listarPessoas();
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
            return;
        }
        for (Pessoa p : pessoas) {
            System.out.println(p.exibirDados());
        }
    }

    // [RF01] cadastra um cliente ou um farmacêutico; os dados vão direto para o construtor,
    // que valida pelos setters, e erros (nome vazio, CPF repetido etc.) sobem como
    // IllegalArgumentException para o menuPessoas mostrar a mensagem (RNF02)
    private static void opcaoCadastrarPessoa(Scanner scanner, Farmacia farmacia) {
        System.out.println("Tipo: 1 - Cliente, 2 - Farmacêutico");
        int tipo = lerInteiro(scanner, "Escolha: ");
        if (tipo != 1 && tipo != 2) {
            throw new IllegalArgumentException("Tipo inválido.");
        }
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        int id = proximoIdPessoa(farmacia);
        if (tipo == 1) {
            farmacia.cadastrarPessoa(new Cliente(id, nome, cpf, telefone));
        } else {
            System.out.print("CRF: ");
            String crf = scanner.nextLine();
            farmacia.cadastrarPessoa(new Farmaceutico(id, nome, cpf, telefone, crf));
        }
        System.out.println("Pessoa cadastrada com sucesso.");
    }

    // o id é gerado aqui (maior id existente mais 1), para o usuário não digitar e
    // nunca repetir um id no cadastro; um id removido pode ser reaproveitado sem problema,
    // pois a Farmacia não deixa remover quem tem pedidos
    private static int proximoIdPessoa(Farmacia farmacia) {
        int maior = 0;
        for (Pessoa p : farmacia.listarPessoas()) {
            if (p.getId() > maior) {
                maior = p.getId();
            }
        }
        return maior + 1;
    }

    // [RF01] consulta uma pessoa pelo CPF; o CPF pode ser digitado com ou sem pontuação,
    // pois a Farmacia limpa o valor antes de comparar
    private static void opcaoBuscarPessoa(Scanner scanner, Farmacia farmacia) {
        System.out.print("CPF: ");
        Pessoa pessoa = farmacia.buscarPessoa(scanner.nextLine());
        if (pessoa == null) {
            System.out.println("Pessoa não encontrada.");
        } else {
            System.out.println(pessoa.exibirDados());
        }
    }

    // [RF01] edita uma pessoa encontrada pelo CPF; o usuário digita todos os dados novamente,
    // e o objeto novo mantém o id e o tipo (cliente ou farmacêutico) da pessoa original;
    // a Farmacia valida o CPF único (RN04)
    private static void opcaoEditarPessoa(Scanner scanner, Farmacia farmacia) {
        System.out.print("CPF da pessoa a editar: ");
        Pessoa atual = farmacia.buscarPessoa(scanner.nextLine());
        if (atual == null) {
            throw new IllegalArgumentException("Pessoa não encontrada.");
        }
        System.out.println("Editando: " + atual.exibirDados());
        System.out.println("Digite os novos dados.");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        if (atual instanceof Farmaceutico) {
            System.out.print("CRF: ");
            String crf = scanner.nextLine();
            farmacia.editarPessoa(new Farmaceutico(atual.getId(), nome, cpf, telefone, crf));
        } else {
            farmacia.editarPessoa(new Cliente(atual.getId(), nome, cpf, telefone));
        }
        System.out.println("Pessoa atualizada com sucesso.");
    }

    // [RF01] remove uma pessoa encontrada pelo CPF, pedindo confirmação antes porque a
    // remoção não tem volta; se a pessoa tiver pedidos, a Farmacia recusa e o menuPessoas
    // mostra o motivo
    private static void opcaoRemoverPessoa(Scanner scanner, Farmacia farmacia) {
        System.out.print("CPF da pessoa a remover: ");
        Pessoa pessoa = farmacia.buscarPessoa(scanner.nextLine());
        if (pessoa == null) {
            throw new IllegalArgumentException("Pessoa não encontrada.");
        }
        System.out.println("Remover: " + pessoa.exibirDados());
        int confirmar = lerInteiro(scanner, "Confirma a remoção? 1 - Sim, 2 - Não: ");
        if (confirmar != 1) {
            System.out.println("Remoção cancelada.");
            return;
        }
        farmacia.removerPessoa(pessoa.getId());
        System.out.println("Pessoa removida com sucesso.");
    }

    // [RNF02] lê um número inteiro repetindo a pergunta enquanto a entrada for inválida,
    // para o programa não encerrar com erro se o usuário digitar letras ou deixar em branco;
    // usa nextLine (e não nextInt) para não deixar o "Enter" pendente no teclado,
    // o que faria a próxima leitura de texto sair vazia
    private static int lerInteiro(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
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
