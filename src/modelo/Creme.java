package modelo;

/**
 * Medicamento manipulado na forma farmacêutica de creme dermatológico.
 */
// [HERANÇA] Creme especializa Medicamento, herdando id, nome, descricaoReceita e a lista de itens
public class Creme extends Medicamento {

    // [ENCAPSULAMENTO] atributos privados específicos da apresentação em creme
    private double volumeMl;
    private double custoBase;

    // Construtor inicializa a superclasse via super() e valida os atributos próprios via setters
    public Creme(int id, String nome, String descricaoReceita, double volumeMl, double custoBase) {
        super(id, nome, descricaoReceita);
        setVolumeMl(volumeMl);
        setCustoBase(custoBase);
    }

    // [POLIMORFISMO] sobrescreve o cálculo da superclasse somando os itens da fórmula ao custo da base (RN03)
    @Override
    public double calcularPrecoFormula() {
        return calcularCustoItens() + this.custoBase;
    }

    public double getVolumeMl() {
        return volumeMl;
    }

    // [ENCAPSULAMENTO] rejeita volume nulo ou negativo para garantir especificação válida do pote/bisnaga
    public void setVolumeMl(double volumeMl) {
        if (volumeMl <= 0) {
            throw new IllegalArgumentException("O volume em ml do creme deve ser maior que zero.");
        }
        this.volumeMl = volumeMl;
    }

    public double getCustoBase() {
        return custoBase;
    }

    // [ENCAPSULAMENTO] impede custo negativo da base creme
    public void setCustoBase(double custoBase) {
        if (custoBase < 0) {
            throw new IllegalArgumentException("O custo da base do creme não pode ser negativo.");
        }
        this.custoBase = custoBase;
    }
}