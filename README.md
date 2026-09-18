# Airport Baggage Handling and Routing Simulator

## Author

- Name: YOUR NAME
- Registration Number: YOUR REGISTRATION NUMBER

---

## 1. Project Overview

The Airport Baggage Handling and Routing Simulator is a Java-based command-line application that simulates how passenger baggage is processed inside an airport.

In a real airport, baggage passes through several stages before it is loaded onto the correct flight. This project represents those stages in a simplified form.

The baggage goes through:

1. Check-in
2. Security screening
3. Sorting
4. Flight-specific routing
5. Loading

Multiple baggage items can be processed at the same time. The project therefore uses Java multithreading and blocking queues to simulate concurrent baggage processing.

The application also stores baggage processing history in a database using JDBC and JPA.

---

## 2. Problem Statement

Airport baggage handling involves several connected stages. A bag must be checked in, screened, sorted according to its flight, and finally loaded onto the correct aircraft.

If many bags are being processed at the same time, the system has to manage shared resources and make sure that bags do not exceed the capacity of a flight.

The purpose of this project is to create a simplified simulation of this process using Java programming concepts such as object-oriented programming, inheritance, exception handling, collections, multithreading, synchronization, JDBC and JPA.

---

## 3. Objectives

The main objectives of the project are:

- To simulate the movement of baggage through different airport processing stages.
- To apply object-oriented programming concepts in a practical problem.
- To use inheritance and polymorphism for different types of baggage.
- To process multiple bags concurrently using Java threads.
- To use `BlockingQueue` for communication between processing stages.
- To synchronize access to shared flight loading resources.
- To handle invalid operations using custom exceptions.
- To store baggage processing history in a database.
- To generate a report of the simulation.
- To provide a simple command-line interface for the user.

---

## 4. Main Features

### Baggage Management

The application supports different types of baggage:

- Standard baggage
- Fragile baggage
- Oversized baggage
- Priority baggage

Each type has its own handling instruction.

### Flight Management

Flights have:

- Flight number
- Destination
- Baggage capacity
- Number of bags currently loaded

The system prevents bags from being loaded after the flight reaches its baggage capacity.

### Concurrent Processing

The simulation uses multiple worker threads for:

- Security
- Sorting
- Loading

This allows different baggage items to be processed at the same time.

### Baggage History

The system records important events during baggage processing, such as:

- Check-in
- Security started
- Security passed
- Sorting passed
- Loading started
- Loading passed

### Database Storage

SQLite is used as the database because it does not require a separate database server.

JDBC is used for database operations and JPA is used for entity mapping.

### Simulation Report

A report is generated after the concurrent simulation showing information such as:

- Total baggage
- Successfully loaded baggage
- Security-rejected baggage
- Delayed baggage
- Flight loading information

---

## 5. Java Concepts Used

The project demonstrates several concepts from the Programming in Java course.

| Concept | Application in Project |
|---|---|
| Classes and Objects | Passenger, Flight, Baggage and other classes |
| Encapsulation | Private fields with methods to access data |
| Inheritance | Different baggage classes extend `Baggage` |
| Polymorphism | Different baggage objects are handled through the common `Baggage` type |
| Abstract Class | `Baggage` provides common baggage structure |
| Enumeration | `BaggageStatus` represents baggage states |
| Exception Handling | Custom exceptions handle invalid operations |
| Collections | Lists and other collections manage passengers, flights and baggage |
| Multithreading | Worker threads process baggage concurrently |
| Synchronization | Flight loading is protected when multiple threads access it |
| BlockingQueue | Transfers baggage between processing stages |
| JDBC | Used for SQLite database operations |
| JPA | Used for entity mapping and persistence |
| File I/O | Simulation results are written to a report file |

---

## 6. Project Structure

```text
airport-baggage-simulator
│
├── database
│   └── airport.db
│
├── logs
│   └── simulation-report.txt
│
├── src
│   └── main
│       ├── java
│       │   └── airport
│       │       ├── enums
│       │       │   └── BaggageStatus.java
│       │       │
│       │       ├── exception
│       │       │   ├── AirportException.java
│       │       │   ├── DuplicateBaggageException.java
│       │       │   └── FlightNotFoundException.java
│       │       │
│       │       ├── model
│       │       │   ├── Baggage.java
│       │       │   ├── Flight.java
│       │       │   ├── FragileBaggage.java
│       │       │   ├── OversizedBaggage.java
│       │       │   ├── Passenger.java
│       │       │   ├── PriorityBaggage.java
│       │       │   ├── ProcessingRecord.java
│       │       │   └── StandardBaggage.java
│       │       │
│       │       ├── persistence
│       │       │   ├── DatabaseManager.java
│       │       │   └── JdbcRepository.java
│       │       │
│       │       ├── simulation
│       │       │   ├── Airport.java
│       │       │   └── SimulationEngine.java
│       │       │
│       │       └── Main.java
│       │
│       └── resources
│
├── pom.xml
└── README.md
