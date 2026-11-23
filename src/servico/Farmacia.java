package servico;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import modelo.MateriaPrima;
import modelo.Pedido;
import modelo.Pessoa;
import modelo.Cliente;
import modelo.Farmaceutico;

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

    public void registrarPedido(Pedido p) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public ArrayList<Pedido> listarPedidosCliente(String cpf) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // grava também os medicamentos e os itens de fórmula de cada pedido
    private void carregarPedidos() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    private void salvarPedidos() {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}
