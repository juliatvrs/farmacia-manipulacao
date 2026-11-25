package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Pedido de um cliente, atendido por um farmacêutico, com um ou mais medicamentos.
 */
public class Pedido {

    // [ENCAPSULAMENTO] atributos privados, acessados apenas por getters e setters
    private int id;
    private String dataRealizacao;
    private double valorTotal;
    private String status;
    private Cliente cliente;
    private Farmaceutico farmaceutico;
    // [COMPOSIÇÃO] o pedido contém um ou mais medicamentos (Capsula, Creme ou Xarope)
    private ArrayList<Medicamento> medicamentos = new ArrayList<>();

    // Construtor chama os setters para garantir que as validações valham desde a criação do objeto
    public Pedido(int id, String dataRealizacao, String status, Cliente cliente, Farmaceutico farmaceutico) {
        setId(id);
        setDataRealizacao(dataRealizacao);
        setStatus(status);
        setCliente(cliente);
        setFarmaceutico(farmaceutico);
        // o valor total inicia em 0 e é atualizado automaticamente conforme medicamentos são adicionados
        this.valorTotal = 0.0;
    }

    // Adiciona o medicamento e recalcula o total na hora, para o valor do pedido nunca ficar desatualizado (RN03)
    public void adicionarMedicamento(Medicamento m) {
        if (m == null) {
            throw new IllegalArgumentException("O medicamento não pode ser nulo.");
        }
        this.medicamentos.add(m);
        this.valorTotal = calcularValorTotal();
    }

    // [POLIMORFISMO] percorre a lista de Medicamento chamando calcularPrecoFormula();
    // cada subclasse (Capsula, Creme ou Xarope) responde com o cálculo da sua forma farmacêutica (RN03)
    public double calcularValorTotal() {
        double total = 0.0;
        for (Medicamento m : this.medicamentos) {
            total += m.calcularPrecoFormula();
        }
        this.valorTotal = total;
        return total;
    }

    // [RF07 / RF09] emite o pedido para manipulação: primeiro valida se há estoque suficiente
    // para todas as matérias-primas somadas do pedido (evitando baixa parcial se faltar algo no meio),
    // depois efetiva a baixa no estoque e atualiza o status
    public void emitirPedido() {
        if (this.medicamentos.isEmpty()) {
            throw new IllegalArgumentException("Não é possível emitir um pedido sem medicamentos.");
        }
        if (!this.status.equalsIgnoreCase("Recebido")) {
            throw new IllegalArgumentException("Apenas pedidos com status 'Recebido' podem ser emitidos (status atual: " + this.status + ").");
        }

        // Primeira passada: acumula o consumo total por matéria-prima (caso dois medicamentos usem o mesmo insumo)
        HashMap<Integer, Double> consumoPorInsumo = new HashMap<>();
        HashMap<Integer, MateriaPrima> insumosMap = new HashMap<>();

        for (Medicamento m : this.medicamentos) {
            if (m.getItens().isEmpty()) {
                throw new IllegalArgumentException("O medicamento '" + m.getNome() + "' não possui itens na fórmula.");
            }
            for (ItemFormula item : m.getItens()) {
                MateriaPrima mp = item.getMateriaPrima();
                int idMp = mp.getId();
                double totalNecessario = consumoPorInsumo.getOrDefault(idMp, 0.0) + item.getQuantidadeUtilizada();
                consumoPorInsumo.put(idMp, totalNecessario);
                insumosMap.put(idMp, mp);
            }
        }

        // Verifica disponibilidade antes de descontar qualquer quantidade do estoque (RF09)
        for (Integer idMp : consumoPorInsumo.keySet()) {
            MateriaPrima mp = insumosMap.get(idMp);
            double qtdNecessaria = consumoPorInsumo.get(idMp);
            if (!mp.temEstoqueSuficiente(qtdNecessaria)) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para emitir o pedido. Insumo: " + mp.getNome()
                        + " (necessário: " + String.format("%.2f", qtdNecessaria) + " " + mp.getUnidadeMedida()
                        + ", disponível: " + String.format("%.2f", mp.getQuantidadeEstoque()) + " " + mp.getUnidadeMedida() + ").");
            }
        }

        // Segunda passada: com todo o estoque garantido, realiza a baixa definitiva (RF07)
        for (Medicamento m : this.medicamentos) {
            for (ItemFormula item : m.getItens()) {
                item.getMateriaPrima().darBaixaEstoque(item.getQuantidadeUtilizada());
            }
        }

        this.valorTotal = calcularValorTotal();
        this.status = "Em manipulação";
    }

    public ArrayList<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public int getId() {
        return id;
    }

    // [ENCAPSULAMENTO] rejeita ID zero ou negativo para garantir identificação única válida
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID do pedido deve ser maior que zero.");
        }
        this.id = id;
    }

    public String getDataRealizacao() {
        return dataRealizacao;
    }

    // [ENCAPSULAMENTO] só aceita data real no formato dd/MM/aaaa; o modo STRICT recusa dias
    // inexistentes (ex.: 31/02), e esse formato também impede o ';' que quebraria o pedidos.txt
    public void setDataRealizacao(String dataRealizacao) {
        if (dataRealizacao == null || dataRealizacao.trim().isEmpty()) {
            throw new IllegalArgumentException("A data de realização do pedido não pode ser vazia.");
        }
        String texto = dataRealizacao.trim();
        try {
            LocalDate.parse(texto, DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("A data de realização deve ser uma data válida no formato dd/MM/aaaa.");
        }
        this.dataRealizacao = texto;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    // [ENCAPSULAMENTO] impede valor total negativo
    public void setValorTotal(double valorTotal) {
        if (valorTotal < 0) {
            throw new IllegalArgumentException("O valor total do pedido não pode ser negativo.");
        }
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    // [ENCAPSULAMENTO] exige status preenchido e sem ';', pois o status é gravado em pedidos.txt
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O status do pedido não pode ser vazio.");
        }
        if (status.contains(";")) {
            throw new IllegalArgumentException("O status do pedido não pode conter ponto e vírgula.");
        }
        this.status = status.trim();
    }

    public Cliente getCliente() {
        return cliente;
    }

    // [ENCAPSULAMENTO] garante que todo pedido esteja associado a um cliente cadastrado
    public void setCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente do pedido não pode ser nulo.");
        }
        this.cliente = cliente;
    }

    public Farmaceutico getFarmaceutico() {
        return farmaceutico;
    }

    // [ENCAPSULAMENTO] garante que todo pedido tenha um farmacêutico responsável
    public void setFarmaceutico(Farmaceutico farmaceutico) {
        if (farmaceutico == null) {
            throw new IllegalArgumentException("O farmacêutico responsável não pode ser nulo.");
        }
        this.farmaceutico = farmaceutico;
    }

    @Override
    public String toString() {
        return String.format("Pedido #%d | Data: %s | Status: %s | Cliente: %s | Farmacêutico: %s (CRF %s) | Total: R$ %.2f",
                id, dataRealizacao, status, cliente.getNome(), farmaceutico.getNome(), farmaceutico.getCrf(), calcularValorTotal());
    }
}