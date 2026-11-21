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
    // CPF exibido parcialmente (só os dois últimos dígitos) para reduzir a
    // exposição dos dados do cliente; o CPF completo continua guardado
    // no objeto e é usado nas buscas
    @Override
    public String exibirDados() {
        String cpfMascarado = "***.***.***-" + getCpf().substring(9);
        return "Cliente | id: " + getId() + " | nome: " + getNome() + " | cpf: " + cpfMascarado + " | telefone: " + getTelefone();
    }
}
