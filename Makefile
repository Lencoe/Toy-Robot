all: pre_test ref_server_test


pre_test:
	gnome-terminal -- java -jar libs/reference-server-0.1.0.jar
	mvn clean
	mvn compile
	mvn verify

ref_server_test:
	java -jar libs/reference-server-0.2.3.jar &
	mvn test
	fuser -k 5000/tcp

server_test:
	gnome-terminal -- mvn exec:java -pl Server
	mvn test
	pkill -f "mvn exec:java -pl Server"


package_projet:
	mvn package
