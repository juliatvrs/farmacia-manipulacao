package modelo;

/**
 * Medicamento na forma de cápsulas.
 * Responsável: Vítor
 */
// [HERANÇA] Capsula herda os atributos e métodos de Medicamento
public class Capsula extends Medicamento {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private int quantidadeCapsulas;
    private double custoCapsulaVazia;

    public Capsula(int id, String nome, String descricaoReceita, int quantidadeCapsulas, double custoCapsulaVazia) {
        super(id, nome, descricaoReceita);
        setQuantidadeCapsulas(quantidadeCapsulas);
        setCustoCapsulaVazia(custoCapsulaVazia);
    }

    // [POLIMORFISMO] método sobrescrito: cálculo de preço específico de Capsula
    @Override
    public double calcularPrecoFormula() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public int getQuantidadeCapsulas() {
        return quantidadeCapsulas;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setQuantidadeCapsulas(int quantidadeCapsulas) {
        this.quantidadeCapsulas = quantidadeCapsulas;
    }

    public double getCustoCapsulaVazia() {
        return custoCapsulaVazia;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setCustoCapsulaVazia(double custoCapsulaVazia) {
        this.custoCapsulaVazia = custoCapsulaVazia;
    }
}
