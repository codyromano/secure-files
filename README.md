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
mvn package

# Create a shortcut for easy use
sh ./create-secure-files-shortcut.sh
```
## Encrypt
```
echo "Super secret" > secret.txt
sf encrypt secret.txt
```
## Decrypt
```
sf decrypt ./encrypt/secret.txt
