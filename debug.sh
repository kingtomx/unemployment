mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=local -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
