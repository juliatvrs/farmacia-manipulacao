package modelo;

/**
 * [ABSTRAÇÃO] Classe abstrata: reúne o que todo cadastro de pessoa da farmácia
 * tem em comum, sem permitir criar uma "pessoa genérica".
 * Responsável: Júlia
 */
public abstract class Pessoa {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private int id;
    private String nome;
    private String cpf;
    private String telefone;

    public Pessoa(int id, String nome, String cpf, String telefone) {
        // usa os setters para a validação valer também na criação do objeto
        setId(id);
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
    }

    // [POLIMORFISMO] método abstrato: cada subclasse exibe seus dados do seu jeito
    public abstract String exibirDados();

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

    public String getCpf() {
        return cpf;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
