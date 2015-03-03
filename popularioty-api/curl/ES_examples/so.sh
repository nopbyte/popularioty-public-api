#!/bin/bash

now=$(($(date +%s%N)/1000000))
#id="656eee98-0717-4f2c-8c28-a5fd8030fc5d"
id="656eee9807174f2c8c28a5fd8030fc5d"
curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "{  
		 \"entity_type\": \"so_stream\",
		    \"entity_id\": \"$id\",
		    \"reputation\": 8,
		    \"date\": $now

    }" \
     http://localhost:9200/reputation_aggregations/rep/$(($(date +%s%N)/1000000))/
