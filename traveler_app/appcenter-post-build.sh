#!/usr/bin/env bash

# Building JavaDocs
mkdir Documentation
javadoc -d Documentation ../traveler_core_kit/src/main/java/com/guestlogix/travelercorekit/models/*.java ../traveler_core_kit/src/main/java/com/guestlogix/travelercorekit/callbacks/*.java ../traveler_ui_kit/src/main/java/com/guestlogix/traveleruikit/activities/*.java ../traveler_ui_kit/src/main/java/com/guestlogix/traveleruikit/fragments/*.java

# Do something with the docs.
echo "Test Test Test"