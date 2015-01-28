#!/bin/bash

types=(service_instance service_object_stream)


for i in "${types[@]}"
do
	./generate_entity_data.sh  date=`date +%s` $i "yes"
	echo $i
done


types=( service_object user )
for i in "${types[@]}"
do
	./generate_entity_data.sh  date=`date +%s` $i "no"
	echo $i
done


