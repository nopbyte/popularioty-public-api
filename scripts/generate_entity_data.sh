#!/bin/bash
types=(service_instance service_object_stream)
#Number of users, service_instances, service_object_streams
n=20
date=`date +%s`
reputations=(popularity feedback activity)
feedback=$3

 for i in `seq 1 $n`;
 do
		id=`echo $2$i`	
		r=$(($RANDOM % 9+1))
		echo "Adding entity of type $2 with id $id, and with feedback: $3 rep:$r"
		./create_aggregation_rep.sh $2 $id $r
		if [ "$3" != 'no' ]; then
        		./create_feedback_rep.sh $2 $id 
        	fi
 		for j in `seq 1 $n`;
 		do
 		
 			for l in "${reputations[@]}"
			do
				r=$(($RANDOM % 9+1))
   				./create_subreputation.sh $2 $id $r $l
 			done 
 		done	
	
 done  



echo '''{
    "entity_type": "service_instance",
    "entity_id": "656eee98-0717-4f2c-8c28-a5fd8030fc5f",
    "reputation": 5,
    "date": 1395078803000
}

SO

{
    "entity_type": "service_object",
    "entity_id": "4f2c-8c28-a5fd8-656eee98-0717030fc5f",
    "reputation": 3,
    "date": 1395078802000
}

REST



USER

{
    "entity_type": "user",
    "entity_id": "f81d4fae-7dec-11d0-a765-00a0c91e6bf6",
    "userName": "username",
    "developer_reputation": 5,
    "end_user_reputation": 5,
    "date": 1395078803000
}''' >>/dev/null
