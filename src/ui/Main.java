package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.Capsula;
import modelo.Cliente;
import modelo.Creme;
import modelo.Farmaceutico;
import modelo.ItemFormula;
import modelo.MateriaPrima;
import modelo.Medicamento;
import modelo.Pedido;
import modelo.Pessoa;
import modelo.Xarope;
import servico.Farmacia;

/**
 * Interface do sistema: o menu em loop no console.
 * Todo o controle do sistema é acessado por aqui.
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
                    menuMateriasPrimas(scanner, farmacia);
                    farmacia.salvarDados();
                    break;
                case 3:
                    menuPedidos(scanner, farmacia);
                    farmacia.salvarDados();
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

    // ===== Menu principal, pessoas e leitura segura =====

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

    // ===== Matérias-primas e montagem de medicamento =====

    // [RNF02] submenu de controle de insumos em loop; exceções de validação dos setters e
    // da Farmacia são tratadas aqui para exibir mensagens claras sem encerrar o programa
    private static void menuMateriasPrimas(Scanner scanner, Farmacia farmacia) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== MATÉRIAS-PRIMAS ===");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todas");
            System.out.println("3 - Repor estoque");
            System.out.println("4 - Alerta de estoque baixo");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(scanner, "Escolha: ");
            try {
                switch (opcao) {
                    case 1:
                        opcaoCadastrarMateriaPrima(scanner, farmacia);
                        break;
                    case 2:
                        opcaoListarMateriasPrimas(farmacia);
                        break;
                    case 3:
                        opcaoReporEstoque(scanner, farmacia);
                        break;
                    case 4:
                        opcaoAlertaEstoqueBaixo(farmacia);
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

    // [RF02] cadastra um novo princípio ativo ou reagente com ID sequencial automático
    private static void opcaoCadastrarMateriaPrima(Scanner scanner, Farmacia farmacia) {
        System.out.print("Nome da matéria-prima: ");
        String nome = scanner.nextLine();
        double quantidade = lerDecimal(scanner, "Quantidade inicial em estoque: ");
        System.out.print("Unidade de medida (ex: g, mg, ml): ");
        String unidade = scanner.nextLine();
        double custo = lerDecimal(scanner, "Custo por unidade (R$): ");
        double limite = lerDecimal(scanner, "Limite mínimo para alerta de estoque: ");

        int id = proximoIdMateriaPrima(farmacia);
        farmacia.cadastrarMateriaPrima(new MateriaPrima(id, nome, quantidade, unidade, custo, limite));
        System.out.println("Matéria-prima cadastrada com sucesso (ID " + id + ").");
    }

    // [RF02] exibe todas as matérias-primas cadastradas no estoque
    private static void opcaoListarMateriasPrimas(Farmacia farmacia) {
        ArrayList<MateriaPrima> lista = farmacia.listarMateriasPrimas();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma matéria-prima cadastrada.");
            return;
        }
        for (MateriaPrima mp : lista) {
            System.out.println(mp);
        }
    }

    // [RF02] soma uma nova quantidade comprada ao estoque atual de um insumo existente
    private static void opcaoReporEstoque(Scanner scanner, Farmacia farmacia) {
        opcaoListarMateriasPrimas(farmacia);
        if (farmacia.listarMateriasPrimas().isEmpty()) {
            return;
        }
        int id = lerInteiro(scanner, "ID da matéria-prima para repor: ");
        MateriaPrima alvo = buscarMateriaPrimaPorId(farmacia, id);
        if (alvo == null) {
            throw new IllegalArgumentException("Matéria-prima não encontrada.");
        }
        double qtdAdicional = lerDecimal(scanner, "Quantidade a adicionar (" + alvo.getUnidadeMedida() + "): ");
        if (qtdAdicional <= 0) {
            throw new IllegalArgumentException("A quantidade de reposição deve ser maior que zero.");
        }
        alvo.setQuantidadeEstoque(alvo.getQuantidadeEstoque() + qtdAdicional);
        System.out.printf("Estoque atualizado! Novo saldo de %s: %.2f %s%n",
                alvo.getNome(), alvo.getQuantidadeEstoque(), alvo.getUnidadeMedida());
    }

    // [RF08] lista apenas os insumos cujo estoque atual atingiu ou ficou abaixo do limite de alerta
    private static void opcaoAlertaEstoqueBaixo(Farmacia farmacia) {
        ArrayList<MateriaPrima> lista = farmacia.listarMateriasPrimas();
        boolean encontrouAlerta = false;
        for (MateriaPrima mp : lista) {
            if (mp.verificarEstoqueBaixo()) {
                System.out.println(mp);
                encontrouAlerta = true;
            }
        }
        if (!encontrouAlerta) {
            System.out.println("Nenhuma matéria-prima com estoque baixo no momento.");
        }
    }

    // [POLIMORFISMO / RF04 / RN02] monta um medicamento (Capsula, Creme ou Xarope) e vincula
    // seus itens de fórmula verificando disponibilidade no estoque; retorna como Medicamento
    private static Medicamento montarMedicamento(Scanner scanner, Farmacia farmacia) {
        if (farmacia.listarMateriasPrimas().isEmpty()) {
            throw new IllegalArgumentException("Não há matérias-primas cadastradas para montar uma fórmula.");
        }

        System.out.println("\nForma farmacêutica: 1 - Cápsula, 2 - Creme, 3 - Xarope");
        int tipo = lerInteiro(scanner, "Escolha: ");
        if (tipo < 1 || tipo > 3) {
            throw new IllegalArgumentException("Tipo de medicamento inválido.");
        }

        int id = lerInteiro(scanner, "ID do medicamento: ");
        System.out.print("Nome da fórmula: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição da receita (posologia / observações): ");
        String descricaoReceita = scanner.nextLine();

        Medicamento medicamento;
        if (tipo == 1) {
            int qtdCapsulas = lerInteiro(scanner, "Quantidade de cápsulas: ");
            double custoCapsula = lerDecimal(scanner, "Custo por cápsula vazia (R$): ");
            medicamento = new Capsula(id, nome, descricaoReceita, qtdCapsulas, custoCapsula);
        } else if (tipo == 2) {
            double volumeMl = lerDecimal(scanner, "Volume do creme (ml): ");
            double custoBase = lerDecimal(scanner, "Custo da base dermatológica (R$): ");
            medicamento = new Creme(id, nome, descricaoReceita, volumeMl, custoBase);
        } else {
            double volumeMl = lerDecimal(scanner, "Volume do xarope (ml): ");
            double custoFrasco = lerDecimal(scanner, "Custo do frasco (R$): ");
            medicamento = new Xarope(id, nome, descricaoReceita, volumeMl, custoFrasco);
        }

        System.out.println("\n--- Adição de Itens da Fórmula ---");
        boolean adicionarMais = true;
        while (adicionarMais) {
            opcaoListarMateriasPrimas(farmacia);
            int idMp = lerInteiro(scanner, "ID da matéria-prima para adicionar à fórmula: ");
            MateriaPrima mp = buscarMateriaPrimaPorId(farmacia, idMp);
            if (mp == null) {
                System.out.println("Matéria-prima não encontrada. Tente novamente.");
                continue;
            }
            double qtdUsada = lerDecimal(scanner, "Quantidade utilizada (" + mp.getUnidadeMedida() + "): ");
            // [RF09] bloqueia item se não houver saldo suficiente no estoque
            if (!mp.temEstoqueSuficiente(qtdUsada)) {
                System.out.println("Estoque insuficiente (disponível: " + mp.getQuantidadeEstoque() + " " + mp.getUnidadeMedida() + ").");
                continue;
            }
            medicamento.adicionarItem(new ItemFormula(qtdUsada, mp));
            System.out.println("Item adicionado à fórmula!");

            int continuar = lerInteiro(scanner, "Adicionar outra matéria-prima? 1 - Sim, 2 - Não: ");
            if (continuar != 1) {
                adicionarMais = false;
            }
        }

        // [RN02] garante que nenhum medicamento seja concluído sem pelo menos 1 matéria-prima
        if (medicamento.getItens().isEmpty()) {
            throw new IllegalArgumentException("O medicamento precisa ter pelo menos um item na fórmula.");
        }

        System.out.printf("Fórmula montada! Preço calculado: R$ %.2f%n", medicamento.calcularPrecoFormula());
        return medicamento;
    }

    // gera o próximo ID sequencial para matéria-prima evitando repetição
    private static int proximoIdMateriaPrima(Farmacia farmacia) {
        int maior = 0;
        for (MateriaPrima mp : farmacia.listarMateriasPrimas()) {
            if (mp.getId() > maior) {
                maior = mp.getId();
            }
        }
        return maior + 1;
    }

    // a Farmacia só devolve uma cópia da lista, então a busca por ID é feita aqui, na camada de interface
    private static MateriaPrima buscarMateriaPrimaPorId(Farmacia farmacia, int id) {
        for (MateriaPrima mp : farmacia.listarMateriasPrimas()) {
            if (mp.getId() == id) {
                return mp;
            }
        }
        return null;
    }

    // [RNF02] lê um número decimal repetindo enquanto inválido e aceitando vírgula ou ponto
    private static double lerDecimal(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                String texto = scanner.nextLine().trim().replace(",", ".");
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor numérico válido.");
            }
        }
    }

    // ===== Pedidos =====

    // [RNF02] submenu de pedidos em loop; captura IllegalArgumentException das validações
    // de Pedido e Farmacia para exibir a mensagem de erro sem encerrar o programa
    private static void menuPedidos(Scanner scanner, Farmacia farmacia) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== PEDIDOS ===");
            System.out.println("1 - Registrar pedido");
            System.out.println("2 - Emitir pedido (baixar estoque)");
            System.out.println("3 - Histórico de pedidos do cliente");
            System.out.println("4 - Atualizar status de pedido");
            System.out.println("0 - Voltar");
            opcao = lerInteiro(scanner, "Escolha: ");
            try {
                switch (opcao) {
                    case 1:
                        opcaoRegistrarPedido(scanner, farmacia);
                        break;
                    case 2:
                        opcaoEmitirPedido(scanner, farmacia);
                        break;
                    case 3:
                        opcaoHistoricoCliente(scanner, farmacia);
                        break;
                    case 4:
                        opcaoAtualizarStatusPedido(scanner, farmacia);
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

    // Registra um novo pedido vinculando cliente, farmacêutico e um ou mais medicamentos montados (RN01)
    private static void opcaoRegistrarPedido(Scanner scanner, Farmacia farmacia) {
        int id = lerInteiro(scanner, "ID do pedido: ");

        System.out.print("CPF do cliente: ");
        Pessoa pCliente = farmacia.buscarPessoa(scanner.nextLine());
        if (!(pCliente instanceof Cliente)) {
            throw new IllegalArgumentException("Cliente não encontrado com o CPF informado.");
        }

        System.out.print("CPF do farmacêutico responsável: ");
        Pessoa pFarmaceutico = farmacia.buscarPessoa(scanner.nextLine());
        if (!(pFarmaceutico instanceof Farmaceutico)) {
            throw new IllegalArgumentException("Farmacêutico não encontrado com o CPF informado.");
        }

        System.out.print("Data de realização (dd/MM/aaaa ou Enter para hoje): ");
        String data = scanner.nextLine().trim();
        if (data.isEmpty()) {
            data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        Pedido pedido = new Pedido(id, data, "Recebido", (Cliente) pCliente, (Farmaceutico) pFarmaceutico);

        // Reutiliza montarMedicamento() para montar uma ou mais fórmulas no mesmo pedido
        boolean adicionarMais = true;
        while (adicionarMais) {
            Medicamento med = montarMedicamento(scanner, farmacia);
            pedido.adicionarMedicamento(med);

            int continuar = lerInteiro(scanner, "Adicionar outro medicamento ao pedido? 1 - Sim, 2 - Não: ");
            if (continuar != 1) {
                adicionarMais = false;
            }
        }

        farmacia.registrarPedido(pedido);
        System.out.printf("Pedido #%d registrado com sucesso! Valor total: R$ %.2f%n",
                pedido.getId(), pedido.getValorTotal());

        int emitirAgora = lerInteiro(scanner, "Deseja emitir o pedido agora (dar baixa no estoque)? 1 - Sim, 2 - Não: ");
        if (emitirAgora == 1) {
            pedido.emitirPedido();
            System.out.println("Pedido emitido! Status atual: " + pedido.getStatus());
            alertarEstoqueBaixoDoPedido(pedido);
        }
    }

    // [RF07 / RF09] localiza um pedido do cliente e executa emitirPedido(), validando e descontando o estoque
    private static void opcaoEmitirPedido(Scanner scanner, Farmacia farmacia) {
        Pedido alvo = selecionarPedidoDoCliente(scanner, farmacia, "ID do pedido a emitir: ");
        if (alvo == null) {
            return;
        }
        alvo.emitirPedido();
        System.out.println("Pedido #" + alvo.getId() + " emitido com sucesso! Novo status: " + alvo.getStatus());
        alertarEstoqueBaixoDoPedido(alvo);
    }

    // [RF11] exibe todos os pedidos realizados por um cliente, detalhando medicamentos e matérias-primas
    private static void opcaoHistoricoCliente(Scanner scanner, Farmacia farmacia) {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        Pessoa pessoa = farmacia.buscarPessoa(cpf);
        if (!(pessoa instanceof Cliente)) {
            throw new IllegalArgumentException("Cliente não encontrado com o CPF informado.");
        }

        System.out.println("\nHistórico de: " + pessoa.exibirDados());
        ArrayList<Pedido> historico = farmacia.listarPedidosCliente(cpf);
        if (historico.isEmpty()) {
            System.out.println("Nenhum pedido encontrado para este cliente.");
            return;
        }

        for (Pedido pedido : historico) {
            System.out.println("--------------------------------------------------");
            System.out.println(pedido);
            for (Medicamento m : pedido.getMedicamentos()) {
                System.out.println("  * " + m);
                for (ItemFormula item : m.getItens()) {
                    System.out.println("      - " + item);
                }
            }
        }
    }

    // A mudança de status passa por aqui para aplicar as regras do fluxo (RF10) 
    // e garantir que a baixa de estoque aconteça só na emissão (RF07)
    private static void opcaoAtualizarStatusPedido(Scanner scanner, Farmacia farmacia) {
        Pedido alvo = selecionarPedidoDoCliente(scanner, farmacia, "ID do pedido para atualizar status: ");
        if (alvo == null) {
            return;
        }

        System.out.println("Status atual: " + alvo.getStatus());
        System.out.println("Novo status:");
        System.out.println("1 - Recebido");
        System.out.println("2 - Em manipulação (emite o pedido e dá baixa no estoque)");
        System.out.println("3 - Pronto");
        System.out.println("4 - Entregue");
        System.out.println("5 - Cancelado");
        int opcaoStatus = lerInteiro(scanner, "Escolha: ");

        String novoStatus;
        switch (opcaoStatus) {
            case 1:
                novoStatus = "Recebido";
                break;
            case 2:
                novoStatus = "Em manipulação";
                break;
            case 3:
                novoStatus = "Pronto";
                break;
            case 4:
                novoStatus = "Entregue";
                break;
            case 5:
                novoStatus = "Cancelado";
                break;
            default:
                throw new IllegalArgumentException("Opção de status inválida.");
        }

        // [RF07] pedido já emitido não volta para 'Recebido': o estoque já foi descontado
        // e uma nova emissão descontaria os insumos uma segunda vez
        if (novoStatus.equals("Recebido") && !alvo.getStatus().equalsIgnoreCase("Recebido")) {
            throw new IllegalArgumentException("Um pedido já emitido não pode voltar para 'Recebido', pois o estoque já foi descontado.");
        }

        // [RF07] a baixa só acontece em emitirPedido(): sem emitir, o pedido não pode avançar nem ser reativado
        if (alvo.getStatus().equalsIgnoreCase("Recebido") && (novoStatus.equals("Pronto") || novoStatus.equals("Entregue"))) {
            throw new IllegalArgumentException("Emita o pedido (status 'Em manipulação') antes de avançar, para o estoque ser descontado.");
        }
        if (alvo.getStatus().equalsIgnoreCase("Cancelado") && !novoStatus.equals("Cancelado")) {
            throw new IllegalArgumentException("Um pedido cancelado não pode ser reativado.");
        }

        // [RF10] pedido entregue é um estado final: o fluxo termina na entrega
        if (alvo.getStatus().equalsIgnoreCase("Entregue") && !novoStatus.equals("Entregue")) {
            throw new IllegalArgumentException("Um pedido entregue não pode mudar de status.");
        }

        // Garante que mudar de 'Recebido' para 'Em manipulação' também dê baixa no estoque
        if (novoStatus.equals("Em manipulação") && alvo.getStatus().equalsIgnoreCase("Recebido")) {
            alvo.emitirPedido();
            System.out.println("Pedido emitido e status atualizado para: " + alvo.getStatus());
            alertarEstoqueBaixoDoPedido(alvo);
            return;
        }

        alvo.setStatus(novoStatus);
        System.out.println("Status do pedido #" + alvo.getId() + " atualizado para: " + alvo.getStatus());
    }

    // Auxiliar das opções de emitir e atualizar status: localiza o pedido sempre do mesmo jeito, 
    // procurando só entre os pedidos do cliente informado
    private static Pedido selecionarPedidoDoCliente(Scanner scanner, Farmacia farmacia, String promptId) {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        Pessoa pessoa = farmacia.buscarPessoa(cpf);
        if (!(pessoa instanceof Cliente)) {
            throw new IllegalArgumentException("Cliente não encontrado com o CPF informado.");
        }

        ArrayList<Pedido> pedidosCliente = farmacia.listarPedidosCliente(cpf);
        if (pedidosCliente.isEmpty()) {
            System.out.println("Nenhum pedido encontrado para este cliente.");
            return null;
        }

        for (Pedido p : pedidosCliente) {
            System.out.println(p);
        }

        int idPedido = lerInteiro(scanner, promptId);
        for (Pedido p : pedidosCliente) {
            if (p.getId() == idPedido) {
                return p;
            }
        }
        throw new IllegalArgumentException("Pedido não encontrado com esse ID para o cliente informado.");
    }

    // [RF08] avisa logo após a baixa no estoque se alguma matéria-prima do pedido atingiu o limite de alerta
    private static void alertarEstoqueBaixoDoPedido(Pedido pedido) {
        for (Medicamento m : pedido.getMedicamentos()) {
            for (ItemFormula item : m.getItens()) {
                MateriaPrima mp = item.getMateriaPrima();
                if (mp.verificarEstoqueBaixo()) {
                    System.out.println("ALERTA DE ESTOQUE BAIXO: " + mp);
                }
            }
        }
    }
}
