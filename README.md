# secure-files

Encrypt and decrypt files on the command line.

## Getting started
```
# Check out the project
git clone https://github.com/codyromano/secure-files.git
cd secure-files

# Update to use your own salt
mv fileManager.properties.example fileManager.properties
open fileManager.properties 

# Build the project
javac FileManager.java
```
## Encrypt
```
echo "Super secret" > secret.txt
java FileManager encrypt secret.txt
```
## Decrypt
```
java FileManager decrypt ./encrypt/secret.txt
