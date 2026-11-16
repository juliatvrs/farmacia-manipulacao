package modelo;

import java.util.ArrayList;

/**
 * [ABSTRAÇÃO] Classe abstrata: define o que todo medicamento manipulado deve
 * fazer (calcular o preço da fórmula) sem dizer como, pois cada forma
 * farmacêutica calcula de um jeito.
 * Responsável: Vítor
 */
public abstract class Medicamento {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private int id;
    private String nome;
    private String descricaoReceita;
    // o medicamento é composto por itens de fórmula (matéria-prima + quantidade)
    private ArrayList<ItemFormula> itens = new ArrayList<>();

    public Medicamento(int id, String nome, String descricaoReceita) {
        setId(id);
        setNome(nome);
        setDescricaoReceita(descricaoReceita);
    }

    // método concreto: igual para todos os tipos de medicamento
    public void adicionarItem(ItemFormula item) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // método concreto: soma o custo de todos os itens da fórmula
    public double calcularCustoItens() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // [POLIMORFISMO] método abstrato: cada subclasse implementa o seu cálculo
    public abstract double calcularPrecoFormula();

    public ArrayList<ItemFormula> getItens() {
        return itens;
    }

    public int getId() {
        return id;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricaoReceita() {
        return descricaoReceita;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setDescricaoReceita(String descricaoReceita) {
        this.descricaoReceita = descricaoReceita;
    }
}
