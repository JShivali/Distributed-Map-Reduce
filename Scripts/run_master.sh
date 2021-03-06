#!/bin/bash

# Copyright 2015 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# [START startup_script]
sudo touch /var/log/worker.log
sudo chmod 777 /var/log/worker.log
sudo apt update
sudo mkdir /usr/app6
sudo chmod 777 /usr/app6
cd /usr/app6
rm -rf Distributed-Map-Reduce
git clone https://github.com/JShivali/Distributed-Map-Reduce.git
TEXT=$(curl http://metadata/computeMetadata/v1/instance/attributes/text -H "Metadata-Flavor: Google")
instancename=$(curl http://metadata/computeMetadata/v1/instance/name -H "Metadata-Flavor: Google")
MAPPER_NAME=$(curl http://metadata/computeMetadata/v1/instance/attributes/mapperName -H "Metadata-Flavor: Google")
KV_STORE_IP=$(curl http://metadata/computeMetadata/v1/instance/attributes/kvstoreip -H "Metadata-Flavor: Google")
OUTPUT_FILE_LOC=$(curl http://metadata/computeMetadata/v1/instance/attributes/outputFileLoc -H "Metadata-Flavor: Google")
INPUT_FILE_LOC=$(curl http://metadata/computeMetadata/v1/instance/attributes/inputFileLoc -H "Metadata-Flavor: Google")
REDUCER_COUNT=$(curl http://metadata/computeMetadata/v1/instance/attributes/reducercount -H "Metadata-Flavor: Google")

case "$instancename" in
   "mapper-instance")
   java -jar /usr/app6/Distributed-Map-Reduce/Outputs/WordMapperProject.jar $MAPPER_NAME $KV_STORE_IP $OUTPUT_FILE_LOC $INPUT_FILE_LOC $REDUCER_COUNT ;;
   "reducer-instance") java -jar /usr/app6/Distributed-Map-Reduce/Outputs/MapReduceMaster.jar ;;
esac
# Create a Google Cloud Storage bucket.


#
## Store the image in the Google Cloud Storage bucket and allow all users
## to read it.
#gsutil cp -a public-read output.png gs://$CS_BUCKET/output.png
# [END startup_script]


