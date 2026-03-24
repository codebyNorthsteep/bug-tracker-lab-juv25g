# Bug Tracker Lab

En Spring Boot-baserad webbapplikation för att hantera och spåra buggar i utvecklingsprojekt, likt en mini Jira.

## Funktioner

- 🐛 **CRUD-operationer**: Skapa, läs, uppdatera och ta bort buggar
- 🔍 **Sök**: Sök buggar efter titel eller beskrivning
- 📊 **Filtrering**: Filtrera buggar efter prioritet eller utvecklingsområde
- 📄 **Pagination**: Effektiv hantering av stora mängder buggar
- 🗓️ **Sortering**: Sortera buggar efter datum eller prioritet
- 📋 **Formulärvalidering**: Säkerställer dataintegritet

## Teknikstack

- **Java 25** med Spring Boot 4.0.3
- **Spring Data JPA** för databasoperationer
- **Thymeleaf** för templates
- **PostgreSQL** databas (Docker-stöd via compose.yaml)
- **Maven** för bygghantering

## Installation och Körning

### Förutsättningar

- Java 25+
- Maven 3.6+
- Docker & Docker Compose (valfritt)

### Lokal Körning

```bash
# Klona eller navigera till projektet
cd bug-tracker-lab-juv25g

# Bygg projektet
mvn clean package

# Starta applikationen
mvn spring-boot:run
```

Öppna webbläsaren på `http://localhost:8080`

### Med Docker

```bash
docker-compose up
```

## Projektstruktur

```
src/main/
├── java/org/example/bugtrackerlabjuv25g/
│   ├── controller/      # HTTP-endpoints
│   ├── service/         # Affärslogik
│   ├── model/           # Entiteter (Bug, Priority, Development)
│   ├── dto/             # Data Transfer Objects
│   ├── mapper/          # Entity-DTO mappning
│   ├── repository/      # Databasåtkomst
│   └── exception/       # Custom exceptions
├── resources/
│   ├── templates/       # Thymeleaf HTML-sidor
│   └── static/css/      # Stilar
└── test/                # Enhetstester
```

## API/Funktioner

- **Hemskärm**: Visa alla buggar med pagination
- **Skapa Bug**: Lägg till ny buggrapport
- **Visa Detaljer**: Se fullständig information om en bugg
- **Redigera**: Uppdatera befintlig bugg
- **Ta bort**: Radera bugg med bekräftelse
- **Sök**: Hitta buggar snabbt

## Systemkrav

- Databasberoenden hanteras automatiskt via Hibernate
- Loggning på DEBUG-nivå för felsökning
- Support för Docker Compose för enkel deployment

## Utvecklare

Caroline Nordbrandt & Daniel Engvall Fahlén


