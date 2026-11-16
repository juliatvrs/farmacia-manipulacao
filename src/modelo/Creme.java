package modelo;

/**
 * Medicamento na forma de creme.
 * Responsável: Vítor
 */
// [HERANÇA] Creme herda os atributos e métodos de Medicamento
public class Creme extends Medicamento {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private double volumeMl;
    private double custoBase;

    public Creme(int id, String nome, String descricaoReceita, double volumeMl, double custoBase) {
        super(id, nome, descricaoReceita);
        setVolumeMl(volumeMl);
        setCustoBase(custoBase);
    }

    // [POLIMORFISMO] método sobrescrito: cálculo de preço específico de Creme
    @Override
    public double calcularPrecoFormula() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public double getVolumeMl() {
        return volumeMl;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setVolumeMl(double volumeMl) {
        this.volumeMl = volumeMl;
    }

    public double getCustoBase() {
        return custoBase;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setCustoBase(double custoBase) {
        this.custoBase = custoBase;
    }
}
