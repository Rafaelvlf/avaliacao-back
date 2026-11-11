# API de Avaliações

Spring Boot 4 + Java 25 + PostgreSQL + Auth0, com deploy automático na AWS via GitHub Actions.

## Rotas

| Método | Rota                 | Quem pode     |
|--------|----------------------|---------------|
| POST   | /avaliacoes          | ADMIN         |
| GET    | /avaliacoes          | ADMIN e USER  |
| GET    | /avaliacoes?emailAvaliado=x | ADMIN e USER (extra) |
| GET    | /avaliacoes/{id}     | ADMIN e USER (extra) |
| PUT    | /avaliacoes/{id}     | ADMIN (extra) |
| DELETE | /avaliacoes/{id}     | ADMIN (extra) |

Exemplo de corpo do POST (o `emailAvaliador` vem do token):

```json
{
  "emailAvaliado": "fulano@empresa.com",
  "nota": 8,
  "comentarios": "Muito colaborativo"
}
```

## O que trocar antes de usar

1. `src/main/resources/application.properties` → `issuer-uri` e `audience` do **seu** Auth0.
2. `.github/workflows/deploy.yml` → `SEU_USUARIO_DOCKERHUB` (aparece 2 vezes).
3. Secrets no GitHub (Settings → Secrets and variables → Actions):
   - `DOCKERHUB_TOKEN` – token de acesso do DockerHub
   - `HOST_TEST` – IP da máquina AWS
   - `KEY_TEST` – conteúdo inteiro do arquivo `.pem`
   - `DB_PASSWORD` – senha usada no container `postgres-aula`

## Pré-requisitos na máquina AWS

```bash
docker network create -d bridge rede
docker run -d --name postgres-aula --restart unless-stopped \
  -e POSTGRES_DB=auladb -e POSTGRES_USER=usuario -e POSTGRES_PASSWORD=SUA_SENHA \
  --network rede -p 5432:5432 postgres
```

## Rodar local

```bash
docker run -d --name postgres-local -e POSTGRES_DB=auladb -e POSTGRES_USER=usuario -e POSTGRES_PASSWORD=senha -p 5432:5432 postgres
./mvnw spring-boot:run
```
