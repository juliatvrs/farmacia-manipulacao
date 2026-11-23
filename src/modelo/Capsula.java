package modelo;

/**
 * Medicamento manipulado na forma farmacêutica de cápsulas.
 * Responsável: Vítor
 */
// [HERANÇA] Capsula especializa Medicamento, reaproveitando id, nome, descricaoReceita e lista de itens
public class Capsula extends Medicamento {

    // [ENCAPSULAMENTO] atributos privados exclusivos da forma farmacêutica em cápsulas
    private int quantidadeCapsulas;
    private double custoCapsulaVazia;

    // Construtor repassa os dados comuns para a superclasse via super() e valida os específicos via setters
    public Capsula(int id, String nome, String descricaoReceita, int quantidadeCapsulas, double custoCapsulaVazia) {
        super(id, nome, descricaoReceita);
        setQuantidadeCapsulas(quantidadeCapsulas);
        setCustoCapsulaVazia(custoCapsulaVazia);
    }

    // [POLIMORFISMO] sobrescreve o cálculo da superclasse somando os ativos ao custo total das cápsulas vazias (RN03)
    @Override
    public double calcularPrecoFormula() {
        return calcularCustoItens() + (this.quantidadeCapsulas * this.custoCapsulaVazia);
    }

    public int getQuantidadeCapsulas() {
        return quantidadeCapsulas;
    }

    // [ENCAPSULAMENTO] rejeita quantidade nula ou negativa de cápsulas na manipulação
    public void setQuantidadeCapsulas(int quantidadeCapsulas) {
        if (quantidadeCapsulas <= 0) {
            throw new IllegalArgumentException("A quantidade de cápsulas deve ser maior que zero.");
        }
        this.quantidadeCapsulas = quantidadeCapsulas;
    }

    public double getCustoCapsulaVazia() {
        return custoCapsulaVazia;
    }

    // [ENCAPSULAMENTO] impede custo negativo por unidade de cápsula vazia
    public void setCustoCapsulaVazia(double custoCapsulaVazia) {
        if (custoCapsulaVazia < 0) {
            throw new IllegalArgumentException("O custo da cápsula vazia não pode ser negativo.");
        }
        this.custoCapsulaVazia = custoCapsulaVazia;
    }
}