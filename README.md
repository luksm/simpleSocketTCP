simpleSocketTCP
===============

Objetivo do trabalho
--------------------

Implementar um sistema que corresponda à arquitetura descrita abaixo:

*Modelo de domíno*

![Modelo de domíno](/docs/img/dominio.png)

*Arquitetura de distribuição*

![Arquitetura de distribuição](/docs/img/arquitetura.png)

* Cliente – Aplicativo modo CONSOLE que se comunica com o servidor de regras de negócio.
* Servidor de Regras de Negócio – Servidor Socket TCP que recebe comandos do cliente e consulta o banco de dados. Implementar o acesso usando Hibernate e, no caso de quem não faz TP3, implementar usando JDBC.
* Banco de Dados HSQLDb – Máquina onde está o HSQLDB.
* O sistema deverá obrigatoriamente funcionar distribuido em 3 máquinas (3 IPs diferentes).

O sistema deverá implementar uma interface com o usuário em modo console que atenda aos seguintes requisitos:

*Regras gerais*
* Deve ser feito em Java.
* Considere que são obrigatórios todos os campos para os quais não existam regras especiais.
* Campos Date são “data e hora”.
* Chave-primária é ID, caso não haja regra específica.
* Implementem corretamente as associações.
* Respeitar as cardinalidades.
* Todos os campos devem ser persistidos.
* É responsabilidade do grupo definir os modelos de interação com o usuário.

*Regras específicas*

1.  Cadastro de cliente
	1.  CRUD
		1.  Create
			1.  Escolha um tipo de dados adequado para CPF. CPF não pode se repetir entre dois clientes.
		2.  Retrieve
			1.  Implementar pesquisa por ID, NOME (trecho inicial) e CPF. Retornar todos os dados, sempre em ordem alfabética.
		1.  Update
			1.  Não permitir alterar o CPF de clientes que possuam pedidos.
		1.  Delete
			1.  Não permitir excluir clientes que possuam pedidos.
1.  Cadastro de Produto (CRUD)
	1.  Gerais
		1.  Estoque nunca pode ser negativo. 1.   Preçodevesermaiorquezero.
	1.  CRUD
		1.  Create
			A. Estoque inicial tem que ser pelo menos 10.
		1.   Retrieve
			A. Pesquisar produto por ID e trecho inicial da DESCRIÇÃO. Retornar todos os dados, sempre em ordem alfabética.
		1.     Update
			A. Sem regras especiais.
		1.   Delete
			A. Não permitir excluir produtos para os quais existam pedidos (de qualquer tipo).
￼￼
3. Cadastro de Pedido e ItemPedido
	1.  Gerais
		1.  Um pedido pode ser criado, salvo, editado, salvo, editado... etc... até que seja feito o processamento (ver “Processamento do Pedido”) 
		1.   ItemPedido
			A. Quantidade sempre maior que zero.
			B. Valor deve ser o valor do produto naquele momento.
	1.  CRUD
		1.  Create
			1. A data do pedido é sempre a data atual.
			1. A data de processamento inicialmente é NULL.
			1. A chave-primária de ItemPedido deve ser {Pedido,Produto}.
		1.  Update
			1. Permitir incluir, editar e excluir APENAS itens de pedidos não processados (Data de processamento = null).
			1. Cliente não pode ser alterado após o pedido ser salvo.
		1.  Retrieve
			1. Pesquisar todos os dados de um pedido pelo seu número (retornar ID, data de emissão (dd/mm/aaaa), data de processamento (dd/mm/aaaa), total do pedido e quantidade de produtos)
			1. Pesquisar todos os pedidos de um certo cliente (retornar ID, data de emissão (dd/mm/aaaa), data de processamento (dd/mm/aaaa), total do pedido e quantidade de produtos)
		1.  Delete
			1. Não podem ser excluidos pedidos processados.
	1.  REGRAS ADICIONAIS
		1.  Processamento do Pedido
			1. Deve haver um comando para processar o pedido. Processar o pedido define a data e hora de processamento e impede que sejam feitas alterações no pedido.
			1. Se a pessoa comprar mais que 10 unidades de um produto, ganha 2% de desconto naquele item.
			1. Ao processar um pedido, o estoque dos produtos deve ser atualizado.
			1. O processamento de um pedido não pode deixar o Produto com estoque negativo. Se isto for ocorrer, colocar NULL na quantidade do ItemPedido correspondente. Isto indica que aquele item não pode ser atendido.
			
*Deverão ser entregues*

1. Arquivo ZIP contendo:
	1.  Os códigos-fonte do projeto (incluindo arquivos de mapeamento e de configuração do Hibernate);
	1.  JARs executáveis.
		1.  A única dependência dos aplicativos deverá ser da JVM e a biblioteca de classes do Hibernate. Não deverá ser exigido nem NetBeans nem Eclipse;
	1.  Arquivo alunos.txt contendo:
		1.  Identificação da TURMA;
		1.  Identificaçãodecadaalunointegrantedogrupo(RM,Nome) 
		1.  Descrição de ajustes no modelo, se foi feita alguma.
￼
2. Como entregar:
	1.  Encaminhar, até a data determinada, e-mail para fabian.martins@gmail.com contendo no assunto 2012.NAC20.<turma>.<RM do integrante do grupo>.<nome do integrante do grupo>. O integrante do grupo constante no nome do arquivo será o daquele responsável por enviar o trabalho.
	1.  O arquivo zip deverá ser nomeado da mesma forma que o assunto da mensagem.
		1.  Exemplo: 2012.NAC20.4SIS.99199.Francisco Bento.zip
			1. veja que é apenas um (1) RM e Nome que será utilizado para nomear o arquivo ZIP.
	1.  Não será permitida a adição de integrantes dos grupos posteriormente à entrega. 
	2.  Serão considerados no processo de avaliação:
		* Existência de cópia de trabalho entre grupos;
		* Qualidade do código (identação, documentação, qualidade na definição das responsabilidades das classes);
		* Execução.
		* Funcionalidades entregues (entrega mais funcionalidades corretas, ganha mais pontos);
		* Funcionalidades não solicitadas contam como ponto negativo (over scoping);
