# Place this file inside the JavaSQL/ directory.
# Builds and runs the console app with the MySQL Connector/J driver,
# with no Maven/Gradle needed.

FROM eclipse-temurin:17-jdk

WORKDIR /app

# Fetch the JDBC driver once at build time (cached in the image layer)
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar /app/lib/mysql-connector-j.jar

COPY . .

# package JavaSQL; -> javac places classes at out/JavaSQL/*.class automatically
RUN javac -cp .:lib/mysql-connector-j.jar -d out *.java

CMD ["java", "-cp", "out:lib/mysql-connector-j.jar", "JavaSQL.Student"]