package modelo;

/**
 * Matéria-prima (princípio ativo ou reagente) controlada no estoque.
 * Responsável: Vítor
 */
public class MateriaPrima {

    // [ENCAPSULAMENTO] atributos privados para proteger a integridade dos dados do estoque
    private int id;
    private String nome;
    private double quantidadeEstoque;
    private String unidadeMedida;
    private double custoPorUnidade;
    private double limiteAlerta;

    // Construtor delega aos setters para garantir que nenhum objeto nasça com dados inválidos
    public MateriaPrima(int id, String nome, double quantidadeEstoque, String unidadeMedida, double custoPorUnidade, double limiteAlerta) {
        setId(id);
        setNome(nome);
        setQuantidadeEstoque(quantidadeEstoque);
        setUnidadeMedida(unidadeMedida);
        setCustoPorUnidade(custoPorUnidade);
        setLimiteAlerta(limiteAlerta);
    }

    // Verifica se o insumo atingiu ou caiu abaixo do limite mínimo para alertar reposição (RF08)
    public boolean verificarEstoqueBaixo() {
        return this.quantidadeEstoque <= this.limiteAlerta;
    }

    // Garante que a quantidade solicitada é positiva e existe disponível antes de emitir pedido (RF09)
    public boolean temEstoqueSuficiente(double qtd) {
        return qtd > 0 && this.quantidadeEstoque >= qtd;
    }

    // Desconta a quantidade usada na fórmula do estoque atual, bloqueando baixa inválida (RF07)
    public void darBaixaEstoque(double qtd) {
        if (qtd <= 0) {
            throw new IllegalArgumentException("A quantidade para baixa deve ser maior que zero.");
        }
        if (!temEstoqueSuficiente(qtd)) {
            throw new IllegalArgumentException("Estoque insuficiente para a matéria-prima: " + this.nome);
        }
        this.quantidadeEstoque -= qtd;
    }

    public int getId() {
        return id;
    }

    // [ENCAPSULAMENTO] rejeita IDs nulos ou negativos para manter identificação única válida
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID da matéria-prima deve ser maior que zero.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    // [ENCAPSULAMENTO] impede cadastro de insumo sem identificação textual
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da matéria-prima não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public double getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    // [ENCAPSULAMENTO] impede estoque físico negativo no sistema
    public void setQuantidadeEstoque(double quantidadeEstoque) {
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    // [ENCAPSULAMENTO] exige unidade de medida (ex: g, mg, ml) para cálculo proporcional correto
    public void setUnidadeMedida(String unidadeMedida) {
        if (unidadeMedida == null || unidadeMedida.trim().isEmpty()) {
            throw new IllegalArgumentException("A unidade de medida não pode ser vazia.");
        }
        this.unidadeMedida = unidadeMedida.trim();
    }

    public double getCustoPorUnidade() {
        return custoPorUnidade;
    }

    // [ENCAPSULAMENTO] rejeita custo zero ou negativo para não distorcer o preço final da fórmula
    public void setCustoPorUnidade(double custoPorUnidade) {
        if (custoPorUnidade <= 0) {
            throw new IllegalArgumentException("O custo por unidade deve ser maior que zero.");
        }
        this.custoPorUnidade = custoPorUnidade;
    }

    public double getLimiteAlerta() {
        return limiteAlerta;
    }

    // [ENCAPSULAMENTO] impede limite de alerta negativo
    public void setLimiteAlerta(double limiteAlerta) {
        if (limiteAlerta < 0) {
            throw new IllegalArgumentException("O limite de alerta não pode ser negativo.");
        }
        this.limiteAlerta = limiteAlerta;
    }

    // Facilita a exibição formatada nas listagens do menu da Main
    @Override
    public String toString() {
        String alerta = verificarEstoqueBaixo() ? " [ESTOQUE BAIXO]" : "";
        return String.format("ID: %d | %s | Estoque: %.2f %s | Custo: R$ %.2f/%s | Limite: %.2f%s",
                id, nome, quantidadeEstoque, unidadeMedida, custoPorUnidade, unidadeMedida, limiteAlerta, alerta);
    }
}