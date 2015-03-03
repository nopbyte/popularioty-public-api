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

To compile the a jar file run
	
	$ gradle build

To compile a war file uncomment the following lines in build.gradle:

	//apply plugin: 'war'
	//war {
	//    baseName = 'popularioty-public-api'
	//    version =  '0.0.1'
	//}

And then run:

	$ gradle build



### Testing from the command line

To test this component there is a set of curl command lines available in the curl/popularioty-calls folder.

## Documentation 

The links to the documentation for specific sections of the API is available here:

* http://docs.popularioty.apiary.io/

Please keep in mind, we try to keep the documentation as up-to-date as possible. However, we recommend to use the curl lines in the 'curl' folder for testing and experimentation with popularioty. 


## Importing the project as an eclipse java project


This project requires the popularioty-commons project. Therefore, you must create the eclipse projects from the parent project (i.e. popularioty-api)


