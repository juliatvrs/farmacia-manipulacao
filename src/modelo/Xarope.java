package modelo;

/**
 * Medicamento na forma de xarope.
 * Responsável: Vítor
 */
// [HERANÇA] Xarope herda os atributos e métodos de Medicamento
public class Xarope extends Medicamento {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private double volumeMl;
    private double custoFrasco;

    public Xarope(int id, String nome, String descricaoReceita, double volumeMl, double custoFrasco) {
        super(id, nome, descricaoReceita);
        setVolumeMl(volumeMl);
        setCustoFrasco(custoFrasco);
    }

    // [POLIMORFISMO] método sobrescrito: cálculo de preço específico de Xarope
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

    public double getCustoFrasco() {
        return custoFrasco;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setCustoFrasco(double custoFrasco) {
        this.custoFrasco = custoFrasco;
    }
}
