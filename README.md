# secure-files

Encrypt and decrypt files on the command line.

## Getting started
```
git clone https://github.com/codyromano/secure-files.git
cd secure-files
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
