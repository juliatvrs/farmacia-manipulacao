# Sistema de Farmácia de Manipulação (PharmaVida)

Trabalho final de Programação Orientada a Objetos 1, UEMG Divinópolis, 4º período, 2026.
Grupo 8: Jair Silveira Mesquita, Júlia Tavares Magalhães e Vitor Oliveira Chaves.

## Como abrir e executar

1. Abra a pasta do projeto no VS Code (com a extensão Java instalada).
2. Execute a classe `src/ui/Main.java`.
3. Os arquivos de dados ficam na pasta `dados/`, e o programa é executado a partir da raiz do projeto.

## Estrutura

| Pasta | Conteúdo |
|---|---|
| `src/modelo` | Pessoa, Cliente, Farmaceutico, Pedido, Medicamento, Capsula, Creme, Xarope, ItemFormula, MateriaPrima |
| `src/servico` | Farmacia (listas, operações e leitura/escrita dos .txt) |
| `src/ui` | Main (menu em loop no console) |
| `dados` | Arquivos .txt com os dados |
| `docs` | Diagrama de classes, tabela de rastreabilidade e declaração de uso de IA |

## Divisão do trabalho

| Quem | Modelo | Farmacia e Main | Arquivo |
|---|---|---|---|
| Vítor | MateriaPrima, Medicamento, Capsula, Creme, Xarope, ItemFormula | Matérias-primas e montagem de medicamento | materias_primas.txt |
| Júlia | Pessoa, Cliente, Farmaceutico | Pessoas, carregarDados, salvarDados, main e menu principal | pessoas.txt |
| Jair | Pedido | Pedidos | pedidos.txt |

Cada pessoa edita apenas os métodos da sua seção em `Farmacia.java` e `Main.java` (veja os comentários `===== NOME: ... =====`).

## Combinados de código

- Indentação correta, nomes em português e em camelCase.
- Comentários explicando por que o trecho existe, e não só o que ele faz.
- Marcar onde cada pilar foi aplicado: `[ABSTRAÇÃO]`, `[ENCAPSULAMENTO]`, `[HERANÇA]` e `[POLIMORFISMO]`.
- Setters validam o valor e lançam `IllegalArgumentException` se ele for inválido. O menu trata o erro sem encerrar o programa.
- Os métodos do esqueleto lançam `UnsupportedOperationException` até serem implementados.
- Os construtores já chamam os setters, para a validação valer na criação dos objetos.

## Fluxo no Git

1. Cada pessoa trabalha na sua branch:
    - `estoque-medicamentos`: Vítor
    - `pessoas`: Júlia
    - `pedidos`: Jair
2. Ao terminar um trecho, junta na branch principal e avisa o grupo para todos atualizarem.
3. Antes de começar a trabalhar, sempre atualizar a branch com o que está na principal.

## Formato dos arquivos .txt

Um registro por linha, campos separados por ponto e vírgula.

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
```

## Entregas

- Código-fonte completo e comentado.
- Arquivos .txt de exemplo com dados já cadastrados.
- Diagrama de classes e justificativa das mudanças em relação à Etapa 1.
- Tabela de rastreabilidade (arquivo e linha de cada item).
- Declaração de uso de ferramentas de IA, se houver.
