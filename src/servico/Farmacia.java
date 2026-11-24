package servico;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 * Centraliza as listas do sistema, as operações de gerenciamento e a
 * leitura/escrita dos arquivos .txt.
 * Cada pessoa edita apenas a sua seção, para evitar conflitos no Git.
 */
public class Farmacia {

    // caminhos dos arquivos de dados (relativos à pasta do projeto)
    private static final String ARQUIVO_PESSOAS = "dados/pessoas.txt";
    private static final String ARQUIVO_MATERIAS_PRIMAS = "dados/materias_primas.txt";
    private static final String ARQUIVO_PEDIDOS = "dados/pedidos.txt";

    private ArrayList<Pessoa> pessoas = new ArrayList<>();
    private ArrayList<MateriaPrima> materiasPrimas = new ArrayList<>();
    private ArrayList<Pedido> pedidos = new ArrayList<>();

    // ===== JÚLIA: pessoas e carregamento geral =====

    // [ENCAPSULAMENTO] todo cadastro passa por aqui, para a regra do CPF único valer
    // para qualquer pessoa adicionada à lista (RN04); a mensagem de erro não mostra
    // dados da pessoa já cadastrada, para não expor informações do cliente (RNF03)
    public void cadastrarPessoa(Pessoa p) {
        if (p == null) {
            throw new IllegalArgumentException("A pessoa não pode ser nula.");
        }
        if (buscarPessoa(p.getCpf()) != null) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com esse CPF.");
        }
        pessoas.add(p);
    }

    // [ENCAPSULAMENTO] a lista de pessoas é privada, então a busca por CPF passa por aqui;
    // o CPF recebido é limpo porque o CPF guardado em cada pessoa só tem números
    public Pessoa buscarPessoa(String cpf) {
        if (cpf == null) {
            return null;
        }
        String cpfLimpo = cpf.replaceAll("\\D", "");
        for (Pessoa p : pessoas) {
            if (p.getCpf().equals(cpfLimpo)) {
                return p;
            }
        }
        return null;
    }

    // [ENCAPSULAMENTO] a edição passa por aqui para a regra do CPF único (RN04) continuar
    // valendo: o CPF novo não pode pertencer a outra pessoa; a mensagem de erro não
    // mostra dados de ninguém (RNF03)
    public void editarPessoa(Pessoa p) {
        if (p == null) {
            throw new IllegalArgumentException("A pessoa não pode ser nula.");
        }
        int posicao = -1;
        for (int i = 0; i < pessoas.size(); i++) {
            if (pessoas.get(i).getId() == p.getId()) {
                posicao = i;
                break;
            }
        }
        if (posicao == -1) {
            throw new IllegalArgumentException("Pessoa não encontrada.");
        }
        Pessoa donaDoCpf = buscarPessoa(p.getCpf());
        if (donaDoCpf != null && donaDoCpf.getId() != p.getId()) {
            throw new IllegalArgumentException("Já existe outra pessoa cadastrada com esse CPF.");
        }
        // copia os dados para o objeto que já está na lista (em vez de trocá-lo), assim os
        // pedidos que apontam para essa pessoa passam a enxergar os dados atualizados
        Pessoa existente = pessoas.get(posicao);
        if (existente.getClass() != p.getClass()) {
            throw new IllegalArgumentException("O tipo da pessoa não pode ser alterado.");
        }
        existente.setNome(p.getNome());
        existente.setCpf(p.getCpf());
        existente.setTelefone(p.getTelefone());
        if (existente instanceof Farmaceutico) {
            ((Farmaceutico) existente).setCrf(((Farmaceutico) p).getCrf());
        }
    }

    // [ENCAPSULAMENTO] a remoção passa por aqui para impedir apagar uma pessoa que já tem
    // pedidos, senão o histórico de pedidos (RF11) ficaria apontando para alguém que não existe mais
    public void removerPessoa(int id) {
        Pessoa alvo = null;
        for (Pessoa p : pessoas) {
            if (p.getId() == id) {
                alvo = p;
                break;
            }
        }
        if (alvo == null) {
            throw new IllegalArgumentException("Pessoa não encontrada.");
        }
        for (Pedido pedido : pedidos) {
            boolean ehCliente = pedido.getCliente() != null && pedido.getCliente().getId() == id;
            boolean ehFarmaceutico = pedido.getFarmaceutico() != null && pedido.getFarmaceutico().getId() == id;
            if (ehCliente || ehFarmaceutico) {
                throw new IllegalArgumentException("Não é possível remover uma pessoa que possui pedidos registrados.");
            }
        }
        pessoas.remove(alvo);
    }

    // [ENCAPSULAMENTO] devolve uma cópia da lista, e não a lista interna, para quem
    // chamar não conseguir adicionar ou remover pessoas por fora da Farmacia
    // (assim as regras, como o CPF único, não são contornadas)
    public ArrayList<Pessoa> listarPessoas() {
        return new ArrayList<>(pessoas);
    }

    // [RNF04] lê do disco todos os dados do sistema; é chamado pela Main ao iniciar o programa.
    // A ordem importa: primeiro pessoas e matérias-primas, depois os pedidos, pois cada
    // pedido aponta para um cliente, um farmacêutico e matérias-primas que já precisam estar na memória
    public void carregarDados() {
        carregarPessoas();
        // TODO (Júlia): ativar quando a parte do Vítor for juntada na principal
        // carregarMateriasPrimas();
        // TODO (Júlia): ativar quando a parte do Jair for juntada na principal
        // carregarPedidos();
    }

    // [RNF04] grava no disco todos os dados do sistema; é chamado pela Main ao sair do
    // programa, para que o que foi cadastrado na sessão continue disponível na próxima
    public void salvarDados() {
        salvarPessoas();
        // TODO (Júlia): ativar quando a parte do Vítor for juntada na principal
        // salvarMateriasPrimas();
        // TODO (Júlia): ativar quando a parte do Jair for juntada na principal
        // salvarPedidos();
    }

    // [RNF04] lê dados/pessoas.txt e recria as pessoas na lista; se o arquivo ainda não
    // existe (primeira execução), começa com a lista vazia; linha estragada é ignorada
    // para um erro no arquivo não impedir o programa de abrir
    private void carregarPessoas() {
        File arquivo = new File(ARQUIVO_PESSOAS);
        if (!arquivo.exists()) {
            return;
        }
        try (Scanner leitor = new Scanner(arquivo, "UTF-8")) {
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(";");
                try {
                    int id = Integer.parseInt(campos[1]);
                    if (campos[0].equals("CLIENTE")) {
                        cadastrarPessoa(new Cliente(id, campos[2], campos[3], campos[4]));
                    } else if (campos[0].equals("FARMACEUTICO")) {
                        cadastrarPessoa(new Farmaceutico(id, campos[2], campos[3], campos[4], campos[5]));
                    }
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Uma linha inválida de pessoas.txt foi ignorada.");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar as pessoas.");
        }
    }

    // [RNF04] grava as pessoas em dados/pessoas.txt, uma por linha; o arquivo é
    // sobrescrito a cada gravação para não duplicar registros; o tipo (CLIENTE ou
    // FARMACEUTICO) vai no começo da linha para o carregarPessoas saber qual objeto criar
    private void salvarPessoas() {
        try (PrintWriter escritor = new PrintWriter(ARQUIVO_PESSOAS, "UTF-8")) {
            for (Pessoa p : pessoas) {
                String linha = p.getId() + ";" + p.getNome() + ";" + p.getCpf() + ";" + p.getTelefone();
                if (p instanceof Farmaceutico) {
                    Farmaceutico f = (Farmaceutico) p;
                    escritor.println("FARMACEUTICO;" + linha + ";" + f.getCrf());
                } else {
                    escritor.println("CLIENTE;" + linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar as pessoas.");
        }
    }

    // ===== VÍTOR: matérias-primas =====

    // [ENCAPSULAMENTO] centraliza o cadastro na Farmacia para impedir objeto nulo ou ID duplicado
    public void cadastrarMateriaPrima(MateriaPrima mp) {
        if (mp == null) {
            throw new IllegalArgumentException("A matéria-prima não pode ser nula.");
        }
        for (MateriaPrima existente : materiasPrimas) {
            if (existente.getId() == mp.getId()) {
                throw new IllegalArgumentException("Já existe uma matéria-prima cadastrada com esse ID.");
            }
        }
        materiasPrimas.add(mp);
    }

    // [ENCAPSULAMENTO] devolve uma cópia da lista interna para proteger o estoque contra alterações externas
    public ArrayList<MateriaPrima> listarMateriasPrimas() {
        return new ArrayList<>(materiasPrimas);
    }

    // [RNF04] lê dados/materias_primas.txt ao iniciar o sistema; se ainda não existir, inicia com lista vazia
    private void carregarMateriasPrimas() {
        File arquivo = new File(ARQUIVO_MATERIAS_PRIMAS);
        if (!arquivo.exists()) {
            return;
        }
        try (Scanner leitor = new Scanner(arquivo, "UTF-8")) {
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(";");
                try {
                    int id = Integer.parseInt(campos[0]);
                    String nome = campos[1];
                    double quantidadeEstoque = Double.parseDouble(campos[2]);
                    String unidadeMedida = campos[3];
                    double custoPorUnidade = Double.parseDouble(campos[4]);
                    double limiteAlerta = Double.parseDouble(campos[5]);

                    cadastrarMateriaPrima(new MateriaPrima(id, nome, quantidadeEstoque, unidadeMedida, custoPorUnidade, limiteAlerta));
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Uma linha inválida de materias_primas.txt foi ignorada.");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar as matérias-primas.");
        }
    }

    // [RNF04] grava todas as matérias-primas em dados/materias_primas.txt usando ';' como separador
    private void salvarMateriasPrimas() {
        File arquivo = new File(ARQUIVO_MATERIAS_PRIMAS);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }
        try (PrintWriter escritor = new PrintWriter(arquivo, "UTF-8")) {
            for (MateriaPrima mp : materiasPrimas) {
                escritor.println(mp.getId() + ";"
                        + mp.getNome() + ";"
                        + mp.getQuantidadeEstoque() + ";"
                        + mp.getUnidadeMedida() + ";"
                        + mp.getCustoPorUnidade() + ";"
                        + mp.getLimiteAlerta());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar as matérias-primas.");
        }
    }

    // ===== JAIR: pedidos =====

    // [ENCAPSULAMENTO] centraliza o cadastro do pedido na Farmacia para validar ID único
    // e garantir que o pedido contenha pelo menos um medicamento (RN02)
    public void registrarPedido(Pedido p) {
        if (p == null) {
            throw new IllegalArgumentException("O pedido não pode ser nulo.");
        }
        if (p.getMedicamentos().isEmpty()) {
            throw new IllegalArgumentException("O pedido precisa conter pelo menos um medicamento.");
        }
        for (Pedido existente : pedidos) {
            if (existente.getId() == p.getId()) {
                throw new IllegalArgumentException("Já existe um pedido cadastrado com esse ID.");
            }
        }
        p.calcularValorTotal();
        pedidos.add(p);
    }

    // [ENCAPSULAMENTO / RF11] consulta os pedidos de um cliente pelo CPF (normalizando pontuação,
    // como em buscarPessoa) e retorna uma nova lista para proteger a coleção interna
    public ArrayList<Pedido> listarPedidosCliente(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do cliente não pode ser vazio.");
        }
        String cpfLimpo = cpf.replaceAll("\\D", "");
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("O CPF deve ter 11 dígitos.");
        }
        ArrayList<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getCliente() != null && p.getCliente().getCpf().equals(cpfLimpo)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // [RNF04] lê dados/pedidos.txt ao iniciar o sistema, reconstruindo cada Pedido,
    // seus Medicamentos (Capsula, Creme ou Xarope) e seus ItemFormula vinculados às matérias-primas
    private void carregarPedidos() {
        File arquivo = new File(ARQUIVO_PEDIDOS);
        if (!arquivo.exists()) {
            return;
        }
        try (Scanner leitor = new Scanner(arquivo, "UTF-8")) {
            Pedido pedidoAtual = null;
            Medicamento medicamentoAtual = null;

            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(";");
                try {
                    if (campos[0].equals("PEDIDO")) {
                        // antes de iniciar um novo pedido, finaliza e registra o pedido anterior lido
                        if (pedidoAtual != null && !pedidoAtual.getMedicamentos().isEmpty()) {
                            pedidoAtual.calcularValorTotal();
                            registrarPedido(pedidoAtual);
                        }
                        pedidoAtual = null;
                        medicamentoAtual = null;

                        int id = Integer.parseInt(campos[1]);
                        String dataRealizacao = campos[2];
                        String status = campos[3];
                        Pessoa pCliente = buscarPessoa(campos[4]);
                        Pessoa pFarmaceutico = buscarPessoa(campos[5]);

                        if (!(pCliente instanceof Cliente) || !(pFarmaceutico instanceof Farmaceutico)) {
                            System.out.println("Pedido ignorado em pedidos.txt: cliente ou farmacêutico não encontrado.");
                            continue;
                        }
                        pedidoAtual = new Pedido(id, dataRealizacao, status, (Cliente) pCliente, (Farmaceutico) pFarmaceutico);

                    } else if (campos[0].equals("MEDICAMENTO")) {
                        if (pedidoAtual == null) {
                            continue;
                        }
                        medicamentoAtual = null;

                        String tipo = campos[1];
                        int idMed = Integer.parseInt(campos[2]);
                        String nomeMed = campos[3];
                        String descricaoReceita = campos[4];

                        if (tipo.equals("CAPSULA")) {
                            int qtdCapsulas = Integer.parseInt(campos[5]);
                            double custoCapsulaVazia = Double.parseDouble(campos[6]);
                            medicamentoAtual = new Capsula(idMed, nomeMed, descricaoReceita, qtdCapsulas, custoCapsulaVazia);
                        } else if (tipo.equals("CREME")) {
                            double volumeMl = Double.parseDouble(campos[5]);
                            double custoBase = Double.parseDouble(campos[6]);
                            medicamentoAtual = new Creme(idMed, nomeMed, descricaoReceita, volumeMl, custoBase);
                        } else if (tipo.equals("XAROPE")) {
                            double volumeMl = Double.parseDouble(campos[5]);
                            double custoFrasco = Double.parseDouble(campos[6]);
                            medicamentoAtual = new Xarope(idMed, nomeMed, descricaoReceita, volumeMl, custoFrasco);
                        }

                        if (medicamentoAtual != null) {
                            pedidoAtual.adicionarMedicamento(medicamentoAtual);
                        }

                    } else if (campos[0].equals("ITEM")) {
                        if (pedidoAtual == null || medicamentoAtual == null) {
                            continue;
                        }
                        int idMateriaPrima = Integer.parseInt(campos[1]);
                        double quantidadeUtilizada = Double.parseDouble(campos[2]);

                        MateriaPrima mp = null;
                        for (MateriaPrima itemMp : listarMateriasPrimas()) {
                            if (itemMp.getId() == idMateriaPrima) {
                                mp = itemMp;
                                break;
                            }
                        }
                        if (mp == null) {
                            throw new IllegalArgumentException("Matéria-prima não encontrada para o item.");
                        }

                        medicamentoAtual.adicionarItem(new ItemFormula(quantidadeUtilizada, mp));
                        pedidoAtual.calcularValorTotal();
                    }
                } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Uma linha inválida de pedidos.txt foi ignorada.");
                }
            }

            // registra o último pedido lido ao chegar no fim do arquivo
            if (pedidoAtual != null && !pedidoAtual.getMedicamentos().isEmpty()) {
                pedidoAtual.calcularValorTotal();
                registrarPedido(pedidoAtual);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar os pedidos.");
        }
    }

    // [RNF04] grava todos os pedidos, seus medicamentos e seus itens de fórmula em dados/pedidos.txt,
    // respeitando a hierarquia de linhas definida no README para reconstrução na próxima execução
    private void salvarPedidos() {
        File arquivo = new File(ARQUIVO_PEDIDOS);
        if (arquivo.getParentFile() != null) {
            arquivo.getParentFile().mkdirs();
        }
        try (PrintWriter escritor = new PrintWriter(arquivo, "UTF-8")) {
            for (Pedido p : pedidos) {
                escritor.println("PEDIDO;"
                        + p.getId() + ";"
                        + p.getDataRealizacao() + ";"
                        + p.getStatus() + ";"
                        + p.getCliente().getCpf() + ";"
                        + p.getFarmaceutico().getCpf());

                for (Medicamento m : p.getMedicamentos()) {
                    if (m instanceof Capsula) {
                        Capsula c = (Capsula) m;
                        escritor.println("MEDICAMENTO;CAPSULA;"
                                + c.getId() + ";"
                                + c.getNome() + ";"
                                + c.getDescricaoReceita() + ";"
                                + c.getQuantidadeCapsulas() + ";"
                                + c.getCustoCapsulaVazia());
                    } else if (m instanceof Creme) {
                        Creme cr = (Creme) m;
                        escritor.println("MEDICAMENTO;CREME;"
                                + cr.getId() + ";"
                                + cr.getNome() + ";"
                                + cr.getDescricaoReceita() + ";"
                                + cr.getVolumeMl() + ";"
                                + cr.getCustoBase());
                    } else if (m instanceof Xarope) {
                        Xarope x = (Xarope) m;
                        escritor.println("MEDICAMENTO;XAROPE;"
                                + x.getId() + ";"
                                + x.getNome() + ";"
                                + x.getDescricaoReceita() + ";"
                                + x.getVolumeMl() + ";"
                                + x.getCustoFrasco());
                    }

                    for (ItemFormula item : m.getItens()) {
                        escritor.println("ITEM;"
                                + item.getMateriaPrima().getId() + ";"
                                + item.getQuantidadeUtilizada());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar os pedidos.");
        }
    }
}
