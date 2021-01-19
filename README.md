# Voting-API
> Serviço voltado para a gestão de pautas e suas seções de votação.

[![Java Version][java-image]][npm-url]
[![spring Version][spring-image]][travis-url]

Está API é executa a gestão de pautas as quais passaram por um período de votação chamado de sessão, a qual tem data de início e fim.

Além de gerir as pautas e a duração das sessões, esta API também controla os votos recebidos, executando validações dos dados do votante e da sessão de votação.

![](header.png)

## Casos de Uso

Esta API pode ser usada para gerir votos em variadas situações, como por exemplo, gerir votos de associados de uma empresa em determinado assunto.

## Setup de Desenvolvimento

### Maven
Para executar esta aplicação primeiramente realize a complicação do projeto com o Maven usando o segunte comando.

```sh
npm install
```
### Banco Relacional
A aplicação utiliza um banco de dados SQLServer para gravação das informações, o banco foi disponibilizado na AWS, logo não é necessário subir um banco de dados local.

Para versionamento da estrutura de banco de dados foi utilizado o Liquibase, então caso queira recriar as estruturas basta alterar a propriedade abaixo para **TRUE** no arquivo application.properties

```sh
spring.liquibase.drop-first: true
```
### Menssageria
Foi utilizado o RabbitMQ como nosso message-broker, para facilitar na utilização por terceiros, foi utilizado um serviço gratuito que disponibiliza uma instância do RabbitMQ online. 

Você pode acessar este serviço em https://www.cloudamqp.com/

## Releases

* 0.0.1
    * Commit inicial, contendo o esboço do projeto.
* 1.0.0
    * Lançamento da primeira versão do projeto contendo todas as funcionalidades básicas.

## Próximos Passos

Algumas evoluções desejadas:

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