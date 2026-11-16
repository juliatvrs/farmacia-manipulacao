package modelo;

/**
 * Farmacêutico responsável por manipular os pedidos.
 * Responsável: Júlia
 */
// [HERANÇA] Farmaceutico herda os atributos e métodos de Pessoa
public class Farmaceutico extends Pessoa {

    private String crf;

    public Farmaceutico(int id, String nome, String cpf, String telefone, String crf) {
        super(id, nome, cpf, telefone);
        setCrf(crf);
    }

    // [POLIMORFISMO] método sobrescrito: exibe os dados do farmacêutico
    @Override
    public String exibirDados() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public String getCrf() {
        return crf;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setCrf(String crf) {
        this.crf = crf;
    }
}
