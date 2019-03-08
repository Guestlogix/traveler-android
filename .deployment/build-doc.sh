#!/bin/bash
# Builds an aggregate of JavaDocs.

echo "Creating an aggregate directory"
rm -rf Javadoc 
mkdir JavaDocDir
mkdir JavaDoc

# Add or remove any extra files for the docs here.

echo "Copying travelercorekit packages..."
cp -r traveler_core_kit/src/main/java/com/guestlogix/travelercorekit/models ./JavaDocDir
cp -r traveler_core_kit/src/main/java/com/guestlogix/travelercorekit/callbacks ./JavaDocDir 

echo "Copying traveleruikit packages..."
cp -r traveler_ui_kit/src/main/java/com/guestlogix/traveleruikit/forms ./JavaDocDir
cp -r traveler_ui_kit/src/main/java/com/guestlogix/traveleruikit/widgets ./JavaDocDir

echo "Done copying Packages! Generating JavaDocs"
# For some reason the Javadoc command does not recursively look for package names. Add folders you want to see manually.
javadoc -d JavaDoc JavaDocDir/models/*.java JavaDocDir/callbacks/*.java JavaDocDir/forms/*.java JavaDocDir/forms/cells/*.java JavaDocDir/forms/descriptors/*.java JavaDocDir/widgets/*.java
rm -r JavaDocDir
