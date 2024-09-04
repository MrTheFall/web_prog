FROM maven:3-openjdk-17

WORKDIR /fastcgi-app

COPY pom.xml .
RUN mvn dependency:copy-dependencies

COPY . .

RUN javac -cp .:fastcgi-lib.jar:target/dependency/* Server.java

CMD ["java", "-cp", ".:fastcgi-lib.jar:target/dependency/*", "-DFCGI_PORT=9000", "Server"]
