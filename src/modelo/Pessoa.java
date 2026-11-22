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

    // [ENCAPSULAMENTO] rejeita id zero ou negativo, pois o id identifica a pessoa
    // de forma única e precisa ser um número positivo
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O id deve ser um número positivo.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    // [ENCAPSULAMENTO] rejeita nome vazio para não cadastrar pessoa sem identificação,
    // e rejeita ";" porque esse caractere separa os campos no arquivo pessoas.txt
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        }
        if (nome.contains(";")) {
            throw new IllegalArgumentException("O nome não pode conter ponto e vírgula.");
        }
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    // [ENCAPSULAMENTO] guarda o CPF só com números e exige 11 dígitos; assim
    // "123.456.789-09" e "12345678909" viram o mesmo valor, o que permite
    // comparar e buscar por CPF e garantir que ele seja único (RN04)
    public void setCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser vazio.");
        }
        String apenasNumeros = cpf.replaceAll("\\D", "");
        if (apenasNumeros.length() != 11) {
            throw new IllegalArgumentException("O CPF deve ter 11 dígitos.");
        }
        this.cpf = apenasNumeros;
    }

    public String getTelefone() {
        return telefone;
    }

    // [ENCAPSULAMENTO] guarda o telefone só com números e exige 10 ou 11 dígitos
    // (DDD mais o número), para que o formato seja sempre o mesmo no cadastro,
    // nas buscas e no arquivo pessoas.txt, mesmo que o usuário digite com
    // parênteses, espaço ou traço
    public void setTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("O telefone não pode ser vazio.");
        }
        String apenasNumeros = telefone.replaceAll("\\D", "");
        if (apenasNumeros.length() < 10 || apenasNumeros.length() > 11) {
            throw new IllegalArgumentException("O telefone deve ter 10 ou 11 dígitos.");
        }
        this.telefone = apenasNumeros;
    }
}
