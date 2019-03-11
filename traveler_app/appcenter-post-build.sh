#!/usr/bin/env bash

# Building JavaDocs
mkdir Documentation
javadoc -d Documentation ../traveler_core_kit/src/main/java/com/guestlogix/travelercorekit/models/*.java ../traveler_core_kit/src/main/java/com/guestlogix/travelercorekit/callbacks/*.java ../traveler_ui_kit/src/main/java/com/guestlogix/traveleruikit/activities/*.java ../traveler_ui_kit/src/main/java/com/guestlogix/traveleruikit/fragments/*.java

# Do something with the docs.
brew install s3cmd

s3Bucket="s3://$S3_BUCKET_ANDROID"
s3cmd --access_key=$AWS_ACCESS_KEY
s3cmd --secret_key=$AWS_SECRET_ACCESS_KEY
s3cmd --region=$REGION
s3cmd put --delete-removed --recursive --acl-public ./Documentation/ $s3Bucket --region=$REGION --access_key=$AWS_ACCESS_KEY --secret_key=$AWS_SECRET_ACCESS_KEY