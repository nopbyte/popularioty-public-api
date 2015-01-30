#!/bin/bash

now=`date +%s%N`
type=$1
id=$2
rep=$3
reptype=$4
total=$(($RANDOM % 100+1))
data="{  
		 \"entity_type\": \""$type"\",
		    \"entity_id\": \""$id"\",
		    \"sub_reputation_type\": \""$reptype"\",
		    \"value\": "$rep",
		    \"total_count\": "$total",
		    \"date\": $now

    }"

echo '\n\n' $data '\n\n'

curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "$data" \
     http://localhost:9200/subreputation/rep/$now/


#curl --include \
#     --request PUT \
#     --header "Content-Type: application/json" \
#     --data-binary "{  
#		 \"entity_type\": \""$type"\",
#		    \"entity_id\": \""$id"\",
#		    \"reputation\": "$rep",
#		    \"date\": $now
#
#   }" \
#     http://localhost:9200/reputation_aggregations/rep/$rep/

  
