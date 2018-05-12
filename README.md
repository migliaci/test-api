# test-api
Test API sandbox including a Java / Spring / Groovy / Gradle implementation of myRetail.

# requirements
JDK 1.8 - http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
Gradle 4.7 - brew install gradle
Apache Tomcat 7.0.86 (newer versions most likely compatible) - https://tomcat.apache.org/download-70.cgi
Datastax Enterprise 5.0.13 (for Cassandra)- https://academy.datastax.com/all-downloads?field_download_driver_category_tid=910

# setup
Download the project and install all dependencies with default options.

Start Datastax cassandra using the following command:

> sudo ./dse/bin/dse cassandra

Once it's finished loading, populate the keyspace needed for the project by loading the sample data:

>./dse/bin/cqlsh -f ~/test-api-parent/test-api-webapp/src/main/resources/config/data/cql/insert_default_pricing_metadata.cql

From the project directory (~/test-api-parent), run the following to run the unit tests and create the WAR:

> gradle build
> gradle copyWarForIntellij -Pintellij-build

This should create a WAR in the test-api-webapp/build/libs directory that can be deployed to Tomcat (you want the non-snapshot version if you're setting this up in IntelliJ - otherwise, the snapshot version is fine too). 

Copy this WAR into the /apache-tomcat-7.0.86/webapps directory.

Start Tomcat:
>./tomcat_installation_directory/apache-tomcat-7.0.86/bin/startup.sh

# navigation 
http://localhost:8080/test-api-webapp/api/swagger-ui.html

# note on PUT endpoint
Right now the PUT endpoint JSON document mirrors the one returned from GET, with the single exception being the id has moved to the uri, so it isn't duplicated in the request body.  The only field that is being explicitly checked at this time is currentPrice, but this has been designed with future updates in mind.

# troubleshooting

If Cassandra isn't running, navigating to the URL above will produce a 404.

If Cassandra is running but the myretail keyspace has not been set up per loading insert_default_pricing_metadata.cql via the method described above, both the GET and PUT endpoints will return 503 Service Unavailable.



