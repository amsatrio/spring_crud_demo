.SILENT: build
.SILENT: build-no-test
.SILENT: start
.SILENT: run
.SILENT: tes
.SILENT: clean
.SILENT: ngrok


build:
	JAVA_HOME=/opt/openjdk-bin-25 ./mvnw package -f pom.xml

build-no-test:
	JAVA_HOME=/opt/openjdk-bin-25 ./mvnw package -f pom.xml -Dmaven.test.skip=true

start: build-no-test
	/opt/openjdk-bin-25/bin/java -Xms96m -Xmx256m -XX:+UseG1GC -XX:+UseStringDeduplication -jar ./target/*.jar

test:
	JAVA_HOME=/opt/openjdk-bin-25 ./mvnw test -f pom.xml

clean:
	rm -rf ./target
	rm -rf ./logs
	rm -rf ./mobile_logs
	rm -rf ./db
	rm -rf ./tmp
	JAVA_HOME=/opt/openjdk-bin-25 mvn clean install -U

remove_logs:
	rm -rf ./logs

code-quality:
	JAVA_HOME=/opt/openjdk-bin-25 mvn checkstyle:check
	JAVA_HOME=/opt/openjdk-bin-25 mvn spotbugs:check
code-quality-gui:
	JAVA_HOME=/opt/openjdk-bin-25 mvn spotbugs:gui

docker:
	docker image rm spring_hospital-app --force
	docker compose up