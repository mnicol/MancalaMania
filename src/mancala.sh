# Change to your directory for the java files. Lets you have this script in another file and still run it
cd ~/private/MancalaMania/MancalaMania/src/

# Gets the date and time when you start running for the filename
DATE=$(date +%Y-%m-%d:%H:%M:%S)

#allows for comand line args with $* and prints to the testResutls directory
java smartTest $* > "testResults/$DATE.txt"
