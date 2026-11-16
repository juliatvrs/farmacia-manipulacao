package modelo;

import java.util.ArrayList;

/**
 * Pedido de um cliente, atendido por um farmacêutico, com um ou mais medicamentos.
 * Responsável: Jair
 */
public class Pedido {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private int id;
    private String dataRealizacao;
    private double valorTotal;
    private String status;
    private Cliente cliente;
    private Farmaceutico farmaceutico;
    // o pedido contém medicamentos (podem ser Capsula, Creme ou Xarope)
    private ArrayList<Medicamento> medicamentos = new ArrayList<>();

    public Pedido(int id, String dataRealizacao, String status, Cliente cliente, Farmaceutico farmaceutico) {
        setId(id);
        setDataRealizacao(dataRealizacao);
        setStatus(status);
        setCliente(cliente);
        setFarmaceutico(farmaceutico);
        // o valor total é calculado por calcularValorTotal()
    }

    public void adicionarMedicamento(Medicamento m) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    // [POLIMORFISMO] percorre a lista de Medicamento chamando calcularPrecoFormula();
    // cada objeto responde com o cálculo da sua própria classe
    public double calcularValorTotal() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void emitirPedido() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public ArrayList<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public int getId() {
        return id;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setId(int id) {
        this.id = id;
    }

    public String getDataRealizacao() {
        return dataRealizacao;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setDataRealizacao(String dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Farmaceutico getFarmaceutico() {
        return farmaceutico;
    }

    // [ENCAPSULAMENTO] TODO: validar o valor e lançar IllegalArgumentException se for inválido
    public void setFarmaceutico(Farmaceutico farmaceutico) {
        this.farmaceutico = farmaceutico;
    }
}
