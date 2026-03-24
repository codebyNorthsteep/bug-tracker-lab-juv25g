# Bug Tracker Lab

En Spring Boot-baserad webbapplikation för att hantera och dokumentera buggar i utvecklingsprojekt. En mini Jira-klon
med stöd för prioriteringar, utvecklingsområden och fullständig bugdokumentation.

## Funktioner

- 📝 **Bugdokumentation**: Dokumentera buggar med titel, beskrivning, utvecklingsområde och prioritet
- 📊 **Prioriteter & Utvecklingsområden**: Klassificera buggar efter allvarlighetsgrad (HIGH/MEDIUM/LOW) och
  utvecklingsområde (BACKEND/FRONTEND)
- 🔍 **Sök & Filtrering**: Hitta och filtrera buggar efter prioritet eller utvecklingsområde
- 📄 **Pagination**: Effektiv hantering av stora mängder buggrapporter
- 🗓️ **Sortering**: Sortera buggar efter datum eller prioritet
- 📋 **Detaljer & Historik**: Se fullständig information och historik för varje bugrapport
- ✅ **Validering**: Säkerställer dataintegritet och korrekt dokumentation

## Teknikstack

- **Java 25** med Spring Boot 4.0.3
- **Spring Data JPA** för databasoperationer
- **Thymeleaf** för templates
- **PostgreSQL** databas (Docker-stöd via compose.yaml)
- **Maven** för bygghantering

## Installation och Körning

### Förutsättningar

- Java 25
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

```text
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


