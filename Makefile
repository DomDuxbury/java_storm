all:
	mvn clean install -Dskiptests=true

run:
	docker run --network=host -v $(shell pwd)/target:/target duckzbug/storm-run jar storm-starter-1.1.1.jar starter.WordCountTopology
