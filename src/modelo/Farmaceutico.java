package modelo;

/**
 * Farmacêutico responsável por manipular os pedidos.
 * Responsável: Júlia
 */
// [HERANÇA] Farmaceutico herda os atributos e métodos de Pessoa
public class Farmaceutico extends Pessoa {

    // [ENCAPSULAMENTO] atributo privado, acessado apenas por getter e setter
    private String crf;

    public Farmaceutico(int id, String nome, String cpf, String telefone, String crf) {
        super(id, nome, cpf, telefone);
        setCrf(crf);
    }

    // [POLIMORFISMO] método sobrescrito: exibe os dados do farmacêutico
    // CPF exibido parcialmente (só os dois últimos dígitos) para reduzir a
    // exposição dos dados (RNF03); o CPF completo continua guardado no objeto
    @Override
    public String exibirDados() {
        String cpfMascarado = "***.***.***-" + getCpf().substring(9);
        return "Farmacêutico | id: " + getId() + " | nome: " + getNome() + " | cpf: " + cpfMascarado + " | telefone: " + getTelefone() + " | crf: " + getCrf();
    }

    public String getCrf() {
        return crf;
    }

    // [ENCAPSULAMENTO] rejeita CRF vazio, pois ele identifica o farmacêutico, e rejeita ";"
    // porque esse caractere separa os campos no arquivo pessoas.txt
    public void setCrf(String crf) {
        if (crf == null || crf.trim().isEmpty()) {
            throw new IllegalArgumentException("O CRF não pode ser vazio.");
        }
        if (crf.contains(";")) {
            throw new IllegalArgumentException("O CRF não pode conter ponto e vírgula.");
        }
        this.crf = crf.trim();
    }
}
