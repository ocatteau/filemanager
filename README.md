File upload with Dropwizard.
Files are encypted when uploaded on the server and decrypted when downloaded from the server.

config.yml contains properties to define where to upload files and where to store encrypted files.

To create the schema, use the following commands :
flyway:clean
flyway:migrate
