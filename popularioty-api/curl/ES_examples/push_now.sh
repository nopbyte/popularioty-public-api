#!/bin/bash

now=$(($(date +%s%N)/1000000))
#id="656eee98-0717-4f2c-8c28-a5fd8030fc5d"
id="656eee9807174f2c8c28a5fd8030fc5d"
curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "{  
		 \"entity_type\": \"service_instance\",
		    \"entity_id\": \"$id\",
		    \"reputation\": 8,
		    \"date\": $now

    }" \
     http://localhost:9200/reputation_aggregations/rep/$(($(date +%s%N)/1000000))/

     
feedback_id=$(($(date +%s%N)/1000000))
     
curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "{
        \"title\": \"Great service!\",
        \"text\": \"review about this service instance\",
        \"rating\": 4,
        \"date\": $(date +%s%N),
        \"user_id\": \"sdo3p23p-po2f-23ff-fwef-3ff3f3fwpfkg\",
        \"user_name\": \"username\",
        \"entity_type\":\"service_instance\",
        \"entity_id\": \"$id\"
    }" \
     http://localhost:9200/feedback/feed/$feedback_id/
     
meta_id=$(($(date +%s%N)))
curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "{
    \"feedback_id\": \"$feedback_id\",
    \"user_giving_feedback_id\": \"ecf8ef50-20da-486e-ab8d-39a269c064d1\",
    \"rating\": 4,
    \"date\": $meta_id,
    \"user_id\": \"sdo3p23p-po2f-23ff-fwef-3ff3f3fwpfkg\",
    \"user_name\": \"username\" 
}" \
     http://localhost:9200/meta_feedback/feed/$meta_id

curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "{
        \"title\": \"Great service!\",
        \"text\": \"review about this service instance\",
        \"rating\": 4,
        \"date\": $(date +%s%N),
        \"user_id\": \"sdo3p23p-po2f-23ff-fwef-3ff3f3fwpfkg\",
        \"user_name\": \"username\",
        \"entity_type\":\"service_instance\",
        \"entity_id\": \"$id\"
    }" \
     http://localhost:9200/feedback/feed/$(($(date +%s%N)/1000000))/
     
     
     1415717800856