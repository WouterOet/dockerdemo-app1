Runnen met java6 of java7:
java -Dspring.profiles.active=dev -jar target/app1-1.0-SNAPSHOT.jar
java -Dspring.profiles.active=docker -jar target/app1-1.0-SNAPSHOT.jar

dev profile
file://${user.home}/quintor/app1.properties (optioneel)

docker profile:
file:/etc/quintor-docker/app1.properties
