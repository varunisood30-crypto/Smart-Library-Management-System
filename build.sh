#!/bin/bash
# Compile and run the Smart Library Management System (Linux/Mac)
mkdir -p out
javac -d out $(find src -name "*.java") || { echo "Compilation failed."; exit 1; }
java -cp out com.library.Main
