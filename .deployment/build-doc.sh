#!/bin/bash
# Builds an aggregate of JavaDocs.

echo "Creating an aggregate directory"
rm -rf Javadoc 
mkdir JavaDocDir
mkdir JavaDoc
echo "Copying travelercorekit packages..."
cp -r ../traveler_core_kit/src/main/java/com/guestlogix/travelercorekit/models ./JavaDocDir
cp -r ../traveler_core_kit/src/main/java/com/guestlogix/travelercorekit/callbacks ./JavaDocDir 

echo "Copying traveleruikit packages..."
cp -r ../traveler_ui_kit/src/main/java/com/guestlogix/traveleruikit/forms ./JavaDocDir
cp -r ../traveler_ui_kit/src/main/java/com/guestlogix/traveleruikit/widgets ./JavaDocDir

echo "Done copying Packages! Generating JavaDocs"
javadoc -d JavaDoc JavaDocDir/models/*.java JavaDocDir/callbacks/*.java JavaDocDir/forms/*.java JavaDocDir/forms/cells/*.java JavaDocDir/forms/descriptors/*.java JavaDocDir/widgets/*.java
tar -zcvf docs.gz JavaDoc/
rm -r JavaDocDir
rm -r JavaDoc
