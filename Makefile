#
# maven Makefile

package: 
	mvn package
.PHONY: package

install: package
	mvn install
.PHONY: install

clean: 
	mvn clean
.PHONY: clean
