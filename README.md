Fabric3 Maven Test Plugins
==========================

This repository hosts the Fabric3 maven test plugins source. Information on Fabric3 can be found at http://www.fabric3.org.


Building the Source
------------------------

Requirements are JDK 7 and Maven 3.1.1+.

To build the source of a tagged release:

1. Execute: mvn clean install

To build the source of a development branch:

1. Checkout and build https://github.com/Fabric3/fabric3-core so that the SNAPSHOT runtime dependencies are installed in the machine's local Maven repository.

2. Execute: mvn clean install
