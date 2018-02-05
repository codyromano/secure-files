set -e

# Use Maven to generate the classes
mvn compile

# Path to main application .jar
JAR_PATH="$(pwd)/target/secure-files-1.0-SNAPSHOT.jar"

# Add an alias to the bash profile
printf "\nalias sf='java -cp $JAR_PATH com.securefiles.app.App'\n" >> ~/.bash_profile
echo "Created 'sf' alias"
