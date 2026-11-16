package modelo;

/**
 * Item de uma fórmula: uma matéria-prima e a quantidade usada dela.
 * Responsável: Vítor
 */
public class ItemFormula {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private double quantidadeUtilizada;
    private MateriaPrima materiaPrima;

    public ItemFormula(double quantidadeUtilizada, MateriaPrima materiaPrima) {
        setQuantidadeUtilizada(quantidadeUtilizada);
        setMateriaPrima(materiaPrima);
    }

    public double calcularCustoProporcional() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public double getQuantidadeUtilizada() {
        return quantidadeUtilizada;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setQuantidadeUtilizada(double quantidadeUtilizada) {
        this.quantidadeUtilizada = quantidadeUtilizada;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
    }
}
