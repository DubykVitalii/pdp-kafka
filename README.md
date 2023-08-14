# Kafka with microservices

## Overview

This project consists of the following components:

- Product Service
- Order Service
- Kafka Message Service
- Delivery Service
- Frontend (Angular)

## Prerequisites

- Java 17
- PostgreSQL
- Apache Kafka
- Node.js

## Setup & Running

### Step 1: Install Prerequisites

Make sure you have Java 17, PostgreSQL, Apache Kafka, and Node.js(v16.17.0) installed on your system.

### Step 2: Start PostgreSQL

1. **Run your PostgreSQL server**: Start your PostgreSQL service.
2. **Create the Databases**: Connect to PostgreSQL and run the following commands to create the necessary databases:

```sql
CREATE DATABASE productdb;
CREATE DATABASE orderdb;
CREATE DATABASE deliverydb;
```
### Step 3: Start Apache Kafka

### Windows
##### Start ZooKeeper
Open a command prompt and navigate to the Kafka installation directory. Run the following command to start ZooKeeper:
```cmd
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
##### Start Kafka Server
Open a new command prompt and navigate to the Kafka installation directory. Run the following command to start the Kafka server:
```cmd
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

### MacOs

##### Start ZooKeeper
```cmd
zookeeper-server-start.sh /usr/local/etc/kafka/zookeeper.properties
```
##### Start Kafka
```cmd
kafka-server-start.sh /usr/local/etc/kafka/server.properties
```

### Step 4: Start Backend Services
Start each backend service (Product, Order, Kafka Message, Delivery) 

### Step 5: Start the Frontend
First, ensure that Node.js(v16.17.0) is installed on your machine.

Navigate to the frontend directory, then run:

Install Dependencies
```bash
npm install
```
Start Frontend
```bash
ng serve
```

The frontend will be accessible at http://localhost:4200
