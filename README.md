# Voting-API
> Serviço voltado para a gestão de pautas e suas seções de votação.

[![Java Version][java-image]][npm-url]
[![spring Version][spring-image]][travis-url]

Esta API executa a gestão de pautas as quais possuem por um período de votação chamado de sessão. Cada sessão possui uma data de início e fim.

Além de gerir as pautas e a duração das sessões, esta API também controla os votos recebidos, executando validações dos dados do votante e da sessão de votação.

![](header.png)

## Casos de Uso

Esta API pode ser usada para gerir votos em variadas situações, como por exemplo, gerir votos de associados de uma empresa em determinado assunto.

## Setup de Desenvolvimento

### Maven
Para executar esta aplicação primeiramente realize a complicação do projeto com o Maven usando o segunte comando.

```sh
mvn install
```
### Banco Relacional
A aplicação utiliza um banco de dados SQLServer para gravação das informações, o banco foi disponibilizado na AWS, logo, não é necessário subir um banco de dados local.

Para versionamento da estrutura de banco de dados foi utilizado o Liquibase, então caso queira recriar as estruturas basta alterar a propriedade abaixo para **TRUE** no arquivo application.properties

```sh
spring.liquibase.drop-first: true
```
### Menssageria
Foi utilizado o RabbitMQ como nosso message-broker, para facilitar na utilização por terceiros, foi utilizado um serviço gratuito que disponibiliza uma instância do RabbitMQ online. 

Você pode acessar este serviço em https://www.cloudamqp.com/

### Documentação (Swagger)

A documentação dos serviços desta API estão disponiveis em uma pagina com o swagger da mesma, para acesso primerio é necessario subir a aplicação e em seguida acessar o link http://localhost:8081/api/voting/swagger-ui/

## Releases

* 0.0.1-SNAPSHOT
    * Commit inicial, contendo o esboço do projeto.
* 0.0.2-SNAPSHOT
    * Lançamento da primeira versão do projeto contendo todas as funcionalidades básicas.
    * Esta versão ainda necessida de ajustes, como citados nos próximos passos.

## Próximos Passos

Algumas evoluções desejadas para as próximas versões:

* Utilizar o RabbitMQ em um servidor que permita a instalação de plugins

* Com o novo RabbitMQ modificar o método de enfileiramento para usar o x-delay.

* Incluir testes de performance.


<!-- Markdown link & img dfn's -->
[java-image]: https://img.shields.io/badge/java-v14-orange
[spring-image]: https://img.shields.io/badge/spring-v2.3.8.RELEASE-green
[npm-url]: https://npmjs.org/package/datadog-metrics
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/yourname/yourproject/wiki
