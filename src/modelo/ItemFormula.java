package modelo;

/**
 * Item de uma fórmula: associa uma matéria-prima à quantidade utilizada dela no medicamento.
 * Responsável: Vítor
 */
public class ItemFormula {

    // [ENCAPSULAMENTO] atributos privados para evitar alteração direta sem validação
    private double quantidadeUtilizada;
    private MateriaPrima materiaPrima;

    // Construtor chama os setters para validar a quantidade e a referência do insumo logo na criação
    public ItemFormula(double quantidadeUtilizada, MateriaPrima materiaPrima) {
        setQuantidadeUtilizada(quantidadeUtilizada);
        setMateriaPrima(materiaPrima);
    }

    // Calcula o custo proporcional deste ingrediente na receita (RF06 / RN03)
    public double calcularCustoProporcional() {
        return this.quantidadeUtilizada * this.materiaPrima.getCustoPorUnidade();
    }

    public double getQuantidadeUtilizada() {
        return quantidadeUtilizada;
    }

    // [ENCAPSULAMENTO] rejeita quantidade nula ou negativa pois todo item da fórmula deve consumir insumo
    public void setQuantidadeUtilizada(double quantidadeUtilizada) {
        if (quantidadeUtilizada <= 0) {
            throw new IllegalArgumentException("A quantidade utilizada na fórmula deve ser maior que zero.");
        }
        this.quantidadeUtilizada = quantidadeUtilizada;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    // [ENCAPSULAMENTO] impede que um item de fórmula exista sem uma matéria-prima vinculada
    public void setMateriaPrima(MateriaPrima materiaPrima) {
        if (materiaPrima == null) {
            throw new IllegalArgumentException("A matéria-prima do item da fórmula não pode ser nula.");
        }
        this.materiaPrima = materiaPrima;
    }

    // Facilita a exibição detalhada dos ingredientes de cada medicamento
    @Override
    public String toString() {
        return String.format("%s - %.2f %s (R$ %.2f)",
                materiaPrima.getNome(),
                quantidadeUtilizada,
                materiaPrima.getUnidadeMedida(),
                calcularCustoProporcional());
    }
}