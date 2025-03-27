FROM gradle:8.8.0-jdk21

COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .
COPY gradlew .
COPY ./frogs ./frogs

RUN ./gradlew --no-daemon dependencies

COPY ./src ./src

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS "-Xmx512M -Xms512M"

CMD java -jar "./build/libs/predskazbutpositive-1.4.0.jar"