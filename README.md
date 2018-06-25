# File upload with Dropwizard.
Files are encypted when uploaded on the server and decrypted when downloaded from the server.

config.yml contains properties to define where to upload files and where to store encrypted files.

To create the schema, use the following commands :

\# flyway:clean

\# flyway:migrate

To build the project :

\#  mvn clean install

To run the server :

\# java -jar filemanager.jar server config.yml

To access to the application, the URL is : http://localhost:8080/filemanager/user1



# Docker
To build the container :

\# docker build -t filemanager:1 .

To run the container :

\# docker run -p 8080:8080 -t filemanager:1
