# Path to where your src is at so you can run this in a diff folder
cd ~/private/MancalaMania/MancalaMania/src/

# Gets the date for the file name. You can run cat *.txt > name.txt to combine all the files to one later
DATE=$(date +%Y-%m-%d:%H:%M:%S)

# Run the smart test $* passes along all the comand line args
java smartTest $* > "testResults/$DATE.txt"
