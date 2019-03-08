import os
import sys
import shutil
import boto3
from botocore.exceptions import ClientError

S3_BUCKET = os.environ['S3_BUCKET_ANDROID'] # sys.argv[1] # The bucket that hosts the front end
AWS_REGION = os.environ['REGION'] # The AWS region
ACCESS_KEY = os.environ['AWS_ACCESS_KEY'] # AWS Key
SECRET_ACCESS_KEY = os.environ['AWS_SECRET_ACCESS_KEY'] # AWS Secret
ZIP_NAME = 'docs.gz'

def deploy_to_s3(src_dir, bucket_name):
    try:
        s3_resource = boto3.resource('s3', aws_access_key_id=ACCESS_KEY, aws_secret_access_key=SECRET_ACCESS_KEY, region_name=AWS_REGION)
    except ClientError as err:
        print("Failed to create boto3 client.\n" + str(err))
        return False

    if not os.path.isdir(src_dir):
        raise ValueError('src_dir %r not found.' % src_dir)
    all_files = []

    for root, dirs, files in os.walk(src_dir):
        all_files += [os.path.join(root, f) for f in files]

    for file in all_files:
        if not os.path.isfile(file):
            break

        mimetype = 'application/json'
        os.rename(file, file_without_gz)
        s3_resource.Object(bucket_name, os.path.relpath(file_without_gz, src_dir)).put(Body=open(file_without_gz, 'rb'), ACL='public-read', ContentType=mimetype, ContentEncoding='gzip')

if __name__ == "__main__":
    deploy_to_s3('.', S3_BUCKET)