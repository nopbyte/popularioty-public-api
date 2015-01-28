#!/bin/bash

now=`date +%s%N`
feedback_id=$now
type=$1
id=$2
n=10
u=$(($RANDOM % $n))
u2=$(($RANDOM % $n))

curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "{
        \"title\": \"Great service!\",
        \"text\": \"review about this entity...\",
        \"rating\": $(($RANDOM % 9+1)),
        \"date\": $now,
        \"user_id\": \"user$u\",
        \"user_name\": \"user$u\",
        \"entity_type\":\"$type\",
        \"entity_id\": \"$id\"
    }" \
     http://localhost:9200/feedback/feed/$feedback_id/
   
     
          
meta_id=$(($(date +%s%N)))
curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "{
    \"feedback_id\": \"$feedback_id\",
    \"user_giving_feedback_id\": \"user$u\",
    \"rating\": $(($RANDOM % 9+1)),
    \"date\": $meta_id,
    \"user_id\": \"user$u2\",
    \"user_name\": \"user$u2\" 
}" \
     http://localhost:9200/meta_feedback/feed/$meta_id