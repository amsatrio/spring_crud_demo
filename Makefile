.SILENT: build
.SILENT: build_skip_test
.SILENT: start
.SILENT: run
.SILENT: tes
.SILENT: clean
.SILENT: ngrok


build:
	JAVA_HOME=/opt/openjdk-bin-25 ./mvnw package -f pom.xml

build_skip_test:
	JAVA_HOME=/opt/openjdk-bin-25 ./mvnw package -f pom.xml -Dmaven.test.skip=true

start: build_skip_test
	/opt/openjdk-bin-25/bin/java -Xms96m -Xmx256m -XX:+UseG1GC -XX:+UseStringDeduplication -jar ./target/*.jar

tes:
	JAVA_HOME=/opt/openjdk-bin-25 ./mvnw test -f pom.xml

clean:
	rm -rf ./target
	rm -rf ./logs
	rm -rf ./mobile_logs
	rm -rf ./db
	rm -rf ./tmp

remove_logs:
	rm -rf ./logs


code-quality:
	mvn checkstyle:check
	mvn spotbugs:check
code-quality-gui:
	mvn spotbugs:gui

docker:
	docker image rm spring_hospital-app --force
	docker compose up