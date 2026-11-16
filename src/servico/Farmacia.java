package servico;

import java.util.ArrayList;
import modelo.MateriaPrima;
import modelo.Pedido;
import modelo.Pessoa;

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

    public void cadastrarPessoa(Pessoa p) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Pessoa buscarPessoa(String cpf) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void editarPessoa(Pessoa p) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void removerPessoa(int id) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public ArrayList<Pessoa> listarPessoas() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // carrega primeiro pessoas e matérias-primas, depois os pedidos (que dependem dos dois)
    public void carregarDados() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void salvarDados() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    private void carregarPessoas() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    private void salvarPessoas() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // ===== VÍTOR: matérias-primas =====

    public void cadastrarMateriaPrima(MateriaPrima mp) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public ArrayList<MateriaPrima> listarMateriasPrimas() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    private void carregarMateriasPrimas() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    private void salvarMateriasPrimas() {
        throw new UnsupportedOperationException("TODO: implementar");
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
