#!/bin/bash

now=`date +%s%N`
type=$1
id=$2
rep=$3
data="{  
		 \"entity_type\": \""$type"\",
		    \"entity_id\": \""$id"\",
		    \"reputation\": "$rep",
		    \"date\": $now

    }"

if [ "$1" == 'user' ]; then
 data="{  
		 \"entity_type\": \""$type"\",
		    \"entity_id\": \""$id"\",
		    \"userName\": \""$id"\",
		    \"developer_reputation\": "$rep",
		    \"end_user_reputation\": "$rep",
		    \"date\": $now

    }"

fi

curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "$data" \
     http://localhost:9200/reputation_aggregations/rep/$now/


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

  
