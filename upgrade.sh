#!/bin/bash

NEW_VERSION_NAME=$(./version.sh)

for directory in */
do
    for FILE_NAME in $directory*
    do
        if [[ $FILE_NAME == *"build.gradle"* ]]; then
            echo "------------------------------------------"
            echo "Upgrading version in $FILE_NAME"
            NEW_VERSION_CODE=$(grep versionCode $FILE_NAME | awk '{ print ++$2 }')
            echo "Incrementing version code to: $NEW_VERSION_CODE"

            echo "Updating version to: $NEW_VERSION_NAME"

            echo "Running sed on file: $FILE_NAME"
            sed -i "" -e "s/versionCode [0-9]*/versionCode $NEW_VERSION_CODE/" $FILE_NAME
            sed -i "" -e "s/versionName \"[0-9]*.[0-9]*.*[0-9]*\"/versionName \"$NEW_VERSION_NAME\"/" $FILE_NAME
        fi
    done
done





