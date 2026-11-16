package modelo;

/**
 * Matéria-prima (princípio ativo ou reagente) controlada no estoque.
 * Responsável: Vítor
 */
public class MateriaPrima {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private int id;
    private String nome;
    private double quantidadeEstoque;
    private String unidadeMedida;
    private double custoPorUnidade;
    private double limiteAlerta;

    public MateriaPrima(int id, String nome, double quantidadeEstoque, String unidadeMedida, double custoPorUnidade, double limiteAlerta) {
        setId(id);
        setNome(nome);
        setQuantidadeEstoque(quantidadeEstoque);
        setUnidadeMedida(unidadeMedida);
        setCustoPorUnidade(custoPorUnidade);
        setLimiteAlerta(limiteAlerta);
    }

    public boolean verificarEstoqueBaixo() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public boolean temEstoqueSuficiente(double qtd) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void darBaixaEstoque(double qtd) {
        throw new UnsupportedOperationException("TODO: implementar");
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

    public double getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setQuantidadeEstoque(double quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public double getCustoPorUnidade() {
        return custoPorUnidade;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setCustoPorUnidade(double custoPorUnidade) {
        this.custoPorUnidade = custoPorUnidade;
    }

    public double getLimiteAlerta() {
        return limiteAlerta;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setLimiteAlerta(double limiteAlerta) {
        this.limiteAlerta = limiteAlerta;
    }
}
