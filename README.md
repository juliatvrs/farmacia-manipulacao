# Sistema de Farmácia de Manipulação (PharmaVida)

Trabalho final de Programação Orientada a Objetos 1, UEMG Divinópolis, 4º período, 2026.
Grupo 8: Jair Silveira Mesquita, Júlia Tavares Magalhães e Vitor Oliveira Chaves.

Sistema de console em Java para uma farmácia de manipulação: cadastra pessoas e matérias-primas, registra pedidos com medicamentos manipulados, emite o pedido com baixa de estoque e grava os dados em arquivos .txt.

## Como executar

Requisito: JDK instalado.

Abra a pasta do projeto no VS Code, com a extensão Java, e execute `src/ui/Main.java`. Execute a partir da raiz do projeto, porque os arquivos são lidos e gravados em `dados/` por caminho relativo.

## Estrutura

| Pasta | Conteúdo |
|---|---|
| `src/modelo` | Pessoa, Cliente, Farmaceutico, Pedido, Medicamento, Capsula, Creme, Xarope, ItemFormula, MateriaPrima |
| `src/servico` | Farmacia (listas, regras de negócio e leitura/escrita dos .txt) |
| `src/ui` | Main (menu em loop no console) |
| `dados` | Arquivos .txt de exemplo |
| `docs` | Diagrama de classes e tabela de rastreabilidade |

## Formato dos arquivos .txt

Codificação UTF-8, um registro por linha, campos separados por ponto e vírgula.

`pessoas.txt`

```
CLIENTE;id;nome;cpf;telefone
FARMACEUTICO;id;nome;cpf;telefone;crf
```

`materias_primas.txt`

```
id;nome;quantidadeEstoque;unidadeMedida;custoPorUnidade;limiteAlerta
```

`pedidos.txt`: as linhas MEDICAMENTO e ITEM pertencem ao PEDIDO e ao MEDICAMENTO que vêm logo antes delas.

```
PEDIDO;id;dataRealizacao;status;cpfCliente;cpfFarmaceutico
MEDICAMENTO;CAPSULA;id;nome;descricaoReceita;quantidadeCapsulas;custoCapsulaVazia
ITEM;idMateriaPrima;quantidadeUtilizada
MEDICAMENTO;CREME;id;nome;descricaoReceita;volumeMl;custoBase
ITEM;idMateriaPrima;quantidadeUtilizada
MEDICAMENTO;XAROPE;id;nome;descricaoReceita;volumeMl;custoFrasco
ITEM;idMateriaPrima;quantidadeUtilizada
```

## Documentação

- [Diagrama de classes](docs/diagrama-classes.pdf)
- [Tabela de rastreabilidade](docs/rastreabilidade.pdf)

## Divisão do trabalho

Cada integrante desenvolveu uma parte em uma branch própria, integrada na `main`.

| Integrante | Modelo | Farmacia e Main | Arquivo de dados | Branch |
|---|---|---|---|---|
| Vitor | MateriaPrima, Medicamento, Capsula, Creme, Xarope, ItemFormula | Matérias-primas e montagem de medicamento | materias_primas.txt | `estoque-medicamentos` |
| Júlia | Pessoa, Cliente, Farmaceutico | Pessoas, carregamento e gravação dos dados, menu principal | pessoas.txt | `pessoas` |
| Jair | Pedido | Pedidos | pedidos.txt | `pedidos` |