# Web Crawler Service

Este é um serviço de web crawler que permite buscar URLs que contenham palavras-chave específicas, começando a partir de uma URL base.

## Funcionalidades

- Crawling assíncrono de páginas web
- Busca por palavras-chave em páginas HTML
- API REST para iniciar buscas e verificar resultados
- Limitação de busca ao domínio da URL base

## Tecnologias Utilizadas

- Java 14
- Maven
- Spark Framework
- Docker
- JUnit 5 para testes

## Endpoints da API

### Iniciar uma busca
```http
POST /crawl
Content-Type: application/json

{
    "keyword": "palavra-chave"
}
```

### Verificar status de uma busca
```http
GET /crawl/{id}
```

## Como Executar

### Usando Docker

1. Construa a imagem Docker:
```bash
docker build -t web-crawler .
```

2. Execute o container:
```bash
docker run -p 4567:4567 -e BASE_URL=https://sua-url-base.com web-crawler
```

### Usando Maven

1. Compile o projeto:
```bash
mvn clean package
```

2. Execute a aplicação:
```bash
mvn exec:java -DBASE_URL=https://sua-url-base.com
```

## Testes

Para executar os testes:
```bash
mvn test
```

## Variáveis de Ambiente

- `BASE_URL`: URL base para iniciar o crawling (obrigatória)

## Limitações

- O crawler só busca URLs dentro do mesmo domínio da URL base
- A palavra-chave deve ter entre 4 e 32 caracteres
- Apenas páginas HTML são processadas

## Estrutura do Projeto

```
src/
├── main/
│   └── java/
│       └── com/
│           └── axreng/
│               └── backend/
│                   ├── controller/
│                   ├── model/
│                   └── util/
└── test/
    └── java/
```

## Respostas da API

### Sucesso ao iniciar busca
```json
{
    "id": "abc123",
    "error": false
}
```

### Status da busca
```json
{
    "id": "abc123",
    "status": "active|done",
    "urls": ["http://..."],
    "error": false
}
```

## Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'feat: adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Crie um Pull Request

## Licença

Este projeto está sob a licença MIT.
