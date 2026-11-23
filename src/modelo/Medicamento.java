package modelo;

import java.util.ArrayList;

/**
 * [ABSTRAÇÃO] Classe abstrata: define a estrutura e o comportamento base de todo
 * medicamento manipulado, deixando o cálculo final do preço para cada forma farmacêutica.
 * Responsável: Vítor
 */
public abstract class Medicamento {

    // [ENCAPSULAMENTO] atributos privados protegidos contra acesso direto
    private int id;
    private String nome;
    private String descricaoReceita;
    // Composição: um medicamento é formado por um ou mais itens de fórmula (RN02)
    private ArrayList<ItemFormula> itens = new ArrayList<>();

    // Construtor utiliza os setters para validar os dados desde a instanciação
    public Medicamento(int id, String nome, String descricaoReceita) {
        setId(id);
        setNome(nome);
        setDescricaoReceita(descricaoReceita);
    }

    // Método concreto: adiciona um ingrediente validado à composição da fórmula
    public void adicionarItem(ItemFormula item) {
        if (item == null) {
            throw new IllegalArgumentException("O item da fórmula não pode ser nulo.");
        }
        this.itens.add(item);
    }

    // Método concreto: soma o custo proporcional de todas as matérias-primas usadas
    public double calcularCustoItens() {
        double total = 0.0;
        for (ItemFormula item : this.itens) {
            total += item.calcularCustoProporcional();
        }
        return total;
    }

    // [POLIMORFISMO] método abstrato: obriga cada subclasse a implementar sua própria regra de preço
    public abstract double calcularPrecoFormula();

    public ArrayList<ItemFormula> getItens() {
        return itens;
    }

    public int getId() {
        return id;
    }

    // [ENCAPSULAMENTO] rejeita IDs nulos ou negativos
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID do medicamento deve ser maior que zero.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    // [ENCAPSULAMENTO] exige nome válido para identificar a fórmula manipulada
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do medicamento não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getDescricaoReceita() {
        return descricaoReceita;
    }

    // [ENCAPSULAMENTO] exige a descrição da receita conforme RF04
    public void setDescricaoReceita(String descricaoReceita) {
        if (descricaoReceita == null || descricaoReceita.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da receita não pode ser vazia.");
        }
        this.descricaoReceita = descricaoReceita.trim();
    }

    // Facilita a exibição resumida do medicamento no pedido
    @Override
    public String toString() {
        return String.format("ID: %d | %s (%s) | Receita: %s | Preço: R$ %.2f",
                id, nome, this.getClass().getSimpleName(), descricaoReceita, calcularPrecoFormula());
    }
}
