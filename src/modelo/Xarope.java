package modelo;

/**
 * Medicamento manipulado na forma farmacêutica líquida de xarope.
 * Responsável: Vítor
 */
// [HERANÇA] Xarope especializa Medicamento, aproveitando id, nome, descricaoReceita e lista de itens
public class Xarope extends Medicamento {

    // [ENCAPSULAMENTO] atributos privados exclusivos da apresentação em xarope
    private double volumeMl;
    private double custoFrasco;

    // Construtor inicializa os dados herdados via super() e valida os específicos via setters
    public Xarope(int id, String nome, String descricaoReceita, double volumeMl, double custoFrasco) {
        super(id, nome, descricaoReceita);
        setVolumeMl(volumeMl);
        setCustoFrasco(custoFrasco);
    }

    // [POLIMORFISMO] sobrescreve o cálculo da superclasse somando os ativos ao custo do frasco (RN03)
    @Override
    public double calcularPrecoFormula() {
        return calcularCustoItens() + this.custoFrasco;
    }

    public double getVolumeMl() {
        return volumeMl;
    }

    // [ENCAPSULAMENTO] rejeita volume nulo ou negativo para garantir especificação válida do frasco
    public void setVolumeMl(double volumeMl) {
        if (volumeMl <= 0) {
            throw new IllegalArgumentException("O volume em ml do xarope deve ser maior que zero.");
        }
        this.volumeMl = volumeMl;
    }

    public double getCustoFrasco() {
        return custoFrasco;
    }

    // [ENCAPSULAMENTO] impede custo negativo do frasco do xarope
    public void setCustoFrasco(double custoFrasco) {
        if (custoFrasco < 0) {
            throw new IllegalArgumentException("O custo do frasco não pode ser negativo.");
        }
        this.custoFrasco = custoFrasco;
    }
}