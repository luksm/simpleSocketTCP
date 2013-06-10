simpleSocketTCP
===============

Objetivo do trabalho
--------------------

Implementar um sistema que corresponda à arquitetura descrita abaixo:

![Modelo de domíno]luksm.github.com/simpleSocketTCP/docs/img/dominio.jpg)
![Arquitetura de distribuição]luksm.github.com/simpleSocketTCP/docs/img/arquitetura.jpg)

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

