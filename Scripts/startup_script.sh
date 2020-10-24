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
sudo touch /var/log/mylogs.log
sudo chmod 777 /var/log/mylogs.log
sudo apt update
sudo mkdir /usr/app6
sudo chmod 777 /usr/app6
cd /usr/app6
rm -rf Distributed-Map-Reduce
git clone https://github.com/JShivali/Distributed-Map-Reduce.git
TEXT=$(curl http://metadata/computeMetadata/v1/instance/attributes/text -H "Metadata-Flavor: Google")
instancename=$(curl http://metadata/computeMetadata/v1/instance/name -H "Metadata-Flavor: Google")
CS_BUCKET=$(curl http://metadata/computeMetadata/v1/instance/attributes/bucket -H "Metadata-Flavor: Google")
echo "$instancename" >> /var/log/mylogs.log
case "$instancename" in
   "master-instance")
   echo "Jar executed" >> /var/log/mylogs.log
   sudo chmod 777 /var/log/masterlog.out
   java -jar /usr/app6/Distributed-Map-Reduce/Outputs/MapReduceMaster.jar > /var/log/masterlog.out
   ;;
   "kvstore-instance")
   echo "KV executed" >> /var/log/mylogs.log
   java -jar /usr/app6/Distributed-Map-Reduce/Outputs/KVServer.jar
    ;;
   "userprog-instance")
   echo "UserProg executed executed" >> /var/log/mylogs.log
   sudo chmod 777 /var/log/userproglog.out
   java -jar /usr/app6/Distributed-Map-Reduce/Outputs/MapReduceUserProgram.jar 2 3 /usr/app6/Distributed-Map-Reduce/Inputs/InvertedDataSet /usr/app6/Distributed-Map-Reduce/Outputs/InvertedIndexMapper.jar /usr/app6/Distributed-Map-Reduce/Outputs/InvertedIndexReducer.jar /usr/app6/Distributed-Map-Reduce/Outputs
   java -jar /usr/app6/Distributed-Map-Reduce/Outputs/MapReduceUserProgram.jar 2 3 /usr/app6/Distributed-Map-Reduce/Inputs/WordCountData /usr/app6/Distributed-Map-Reduce/Outputs/WordMapperProject.jar /usr/app6/Distributed-Map-Reduce/Outputs/WordReducerProject.jar /usr/app6/Distributed-Map-Reduce/Outputs
  #jar_name, mapper_count,reducer_count, inputFileLocation (Mention the directory), output directory
   ;;
esac
# Create a Google Cloud Storage bucket.
gsutil mb gs://$CS_BUCKET
gsutil cp /usr/app6/Distributed-Map-Reduce/Scripts/start-worker.sh gs://$CS_BUCKET

#
## Store the image in the Google Cloud Storage bucket and allow all users
## to read it.
#gsutil cp -a public-read output.png gs://$CS_BUCKET/output.png
# [END startup_script]

