# FeldipProject

A custom OSRS (Old School RuneScape) Private Server with enhanced farming mechanics and 1:1 OSRS fidelity.

## Project Structure

```
FeldipProject/
├── server/              # Java-based game server (Aelous210)
├── client/              # Modified RuneLite client (Cryptic-Game-Client-Dev2)
└── cache-server/        # JS5 cache server (Cryptic-JS5-Dev)
```

## Server

The game server is built with Java and Maven.

### Running the Server

```bash
cd server
mvnw clean package
java -jar target/Aelous-1.0.jar
```

Or use the provided batch file:
```bash
cd server
run_server.bat
```

### Server Features
- Custom farming system with fruit trees, allotments, herbs, and more
- Dynamic client-side menu options based on farming states
- RSMod2 pathfinding integration
- Theatre of Blood (TOB) content
- Wilderness bosses and areas

## Client

The client is a modified RuneLite client built with Gradle.

### Building the Client

```bash
cd client
./gradlew build
```

### Running the Client

```bash
cd client
./gradlew runClient
```

### Client Features
- Custom varbit tracking for farming patches
- Dynamic object menu options
- OSRS 1:1 visual fidelity
- Debug commands for development

## Cache Server

The JS5 cache server provides game assets to the client.

### Running the Cache Server

```bash
cd cache-server
# Follow cache-server specific instructions
```

## Development

### Prerequisites
- **Java 11+** for server and cache-server
- **Gradle 7+** for client
- **Maven 3.6+** for server

### Key Technologies
- **Server**: Java, Maven, Netty, Lombok
- **Client**: Java, Gradle, RuneLite API
- **Cache**: JS5 protocol

## Recent Improvements

- ✅ Fruit tree farming with accurate OSRS varbit states
- ✅ Dynamic menu options (Check-health, Pick-fruit, Chop down)
- ✅ Client-side varbit synchronization
- ✅ RSMod2 pathfinding system
- ✅ Professional farming engine refactor

## Contributing

This is a private development project. All changes should be tested thoroughly before committing.

## License

Private project - All rights reserved.
