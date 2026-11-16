package modelo;

/**
 * Cliente da farmácia.
 * Responsável: Júlia
 */
// [HERANÇA] Cliente herda os atributos e métodos de Pessoa
public class Cliente extends Pessoa {

    public Cliente(int id, String nome, String cpf, String telefone) {
        super(id, nome, cpf, telefone);
    }

    // [POLIMORFISMO] método sobrescrito: exibe os dados do cliente
    @Override
    public String exibirDados() {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}
