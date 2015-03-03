Popularioty
===========


# Installation

## Requirements

	Java sun SDK 1.7
	gradle version 11.1 or higher
	gradle 1.11 or higher (building only)
	ElasticSeach	
	git (checkout)


## Quick Start

### Getting the code

To get the code run: 

	$ git clone 
	


### Installing 

 
#### Configurations

Modify the file located in  src/main/resources/application.properties to set port to run popularioty if required. this only applies when you build a jar file...


#### Run the popuarioty-api as a Jar or as a war file

TODO: Adjust compile_war and jar...

You can compile automatically a war file by executing this script:

	$ ./compile_war.sh
	
Afterwards you will find the war file in build/libs/.

Alternatively you can compile the project as a jar file in this way.
	
	$ ./compile_jar.sh
	$ java -jar build/libs/


### Testing from the command line

To test this component there is a set of curl command lines available in the curl/popularioty-calls folder.

## Documentation 

The links to the documentation for specific sections of the API is available here:

* http://docs.popularioty.apiary.io/

Please keep in mind, we try to keep the documentation as up-to-date as possible. However, we recommend to use the curl lines in the 'curl' folder for testing and experimentation with popularioty. 


## Importing the project as an eclipse java project

To import the project execute the following commands from a shell:

	$ git clone https://github.com/<repo>
	$ cd popularioty-public-api
	$ gradle eclipse

This will generate the proper eclipse files. Afterwards, just execute the 'import existing project into workspace' feature from eclipse.

