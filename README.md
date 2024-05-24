
Budget Buddy Account Service
============================

This is a Spring application that is meant to run in conjunction with all of the other Budget Buddy services. To build, download java 17 and maven, and run this command:

    mvn clean package -DskipTests

This service expects to have its own database running, which can be configured with the `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` environment variables. The service also communicates with the [transactions service](https://github.com/My-Budget-Buddy/Budget-Buddy-TransactionService). For that, you need the transactions service to also be running, as well as the [eureka discovery service](https://github.com/My-Budget-Buddy/Budget-Buddy-DiscoveryService), and possibly to configure the `EUREKA_URL` environment variable.
