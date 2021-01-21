#!/bin/bash

ORIGINAL_DIR=`pwd`

MCPDIR="mcp62"
OUTPUT_FILE="$ORIGINAL_DIR/actionmc-mod.zip"
ERROR_FILE="$ORIGINAL_DIR/$MCPDIR/logs/mcperr.log"

# Check for existence of MCP
if [[ ! -d "$MCPDIR" ]]; then
	echo "MCP not found! Please download MCP 6.2 and extract it to $MCPDIR/"
	exit 1
fi

cd mcp62

# Decompile Minecraft JAR if no src dir exists
if [[ $(find "src/" -type f | wc -l) -eq 0 ]]; then
	echo "-= Decompiling... =-"
	> $ERROR_FILE
	python2 runtime/decompile.py
	if [[ $? -ne 0 || $(wc -l <"$ERROR_FILE") -ge 2 ]]; then
		echo "Failed to decompile! Make sure you have the Minecraft JAR file and libraries present in $MCPDIR/jars/bin"
		exit 1
	fi
fi

# Copy source files to MCP directory
cp -r ../src/* "src/minecraft/"

# Compile mod
echo "-= Compiling... =-"
> $ERROR_FILE
python2 runtime/recompile.py
if [[ $? -ne 0 || $(wc -l <"$ERROR_FILE") -ge 2 ]]; then
	echo "Failed to compile, aborting"
	exit 1
fi

# Re-obfuscate mod files
echo "-= Obfuscating... =-"
> $ERROR_FILE
python2 runtime/reobfuscate.py
if [[ $? -ne 0 || $(wc -l <"$ERROR_FILE") -ge 2 ]]; then
	echo "Failed to obfuscate, aborting"
	exit 1
fi

# Create ZIP file with modified classes and required files
echo "-= Creating ZIP file... =-"
cd reobf/minecraft
zip -r $OUTPUT_FILE *.class
cd $ORIGINAL_DIR/jarfiles
zip -ur $OUTPUT_FILE *
