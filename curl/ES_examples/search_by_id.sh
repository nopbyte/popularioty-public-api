#!/bin/bash

now=$(($(date +%s%N)/1000000))
id="656eee9807174f2c8c28a5fd8030fc5d"
size=10
type="service_instance"
#type="so_stream"
getUrl="http://localhost:9200/reputation_aggregations/rep/_search?q=entity_type:$type%20AND%20entity_id:$id&sort=date:desc&size=$size"

echo "trying: $getUrl"
echo "\n"
curl -XGET $getUrl

echo "trying post"
echo "\n"
curl -XGET -d "{
  \"query\": {
    \"bool\": {
      \"must\": [
        {
          \"term\": {
            \"rep.entity_id\": \"$id\"
          }
        }
      ],
      \"must_not\": [],
      \"should\": []
    }
  },
  \"from\": 0,
  \"size\": $size,
  \"sort\": {\"date\":{\"order\":\"desc\"}},
  \"facets\": {}
}"  http://localhost:9200/reputation_aggregations/rep/_search 
