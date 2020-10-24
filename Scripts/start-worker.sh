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
MAPPER_FUNCTION=$(curl http://metadata/computeMetadata/v1/instance/attributes/mapperfunction -H "Metadata-Flavor: Google")
KV_STORE_IP=$(curl http://metadata/computeMetadata/v1/instance/attributes/kvstoreip -H "Metadata-Flavor: Google")
OUTPUT_FILE_LOC=$(curl http://metadata/computeMetadata/v1/instance/attributes/outputFileLoc -H "Metadata-Flavor: Google")
INPUT_FILE_LOC=$(curl http://metadata/computeMetadata/v1/instance/attributes/inputFileLoc -H "Metadata-Flavor: Google")
REDUCER_COUNT=$(curl http://metadata/computeMetadata/v1/instance/attributes/reducercount -H "Metadata-Flavor: Google")
echo $MAPPER_FUNCTION, $instancename, $KV_STORE_IP, $OUTPUT_FILE_LOC, $INPUT_FILE_LOC, $REDUCER_COUNT >> /var/log/worker.log
componentMapper='mapper'
if [[ "$instancename" == *"mapper"* ]] ; then
    sudo touch /var/log/WCMapperlog.out
   sudo chmod 777 /var/log/WCMapperlog.out
   echo "Inside mapper" >> /var/log/WCMapperlog.out
   echo "running mapper jar " >> /var/log/WCMapperlog.out
   java -jar $MAPPER_FUNCTION $instancename $KV_STORE_IP $OUTPUT_FILE_LOC $INPUT_FILE_LOC $REDUCER_COUNT > /var/log/worker.log
else
  echo "Inside reducer" >> /var/log/WCMapperlog.out
  java -jar $MAPPER_FUNCTION $instancename $KV_STORE_IP $OUTPUT_FILE_LOC $INPUT_FILE_LOC $REDUCER_COUNT > /var/log/worker.log
fi
