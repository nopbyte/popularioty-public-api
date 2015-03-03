curl --include \
     --request PUT \
     --header "Content-Type: application/json" \
     --data-binary "{  \"src\": {
    		\"soid\": \"14159588582451b737fe105084da7b7314321af4c7d23\",
    		\"streamid\": \"aboveSeventy\"
 		 },
 		 \"dest\": {
 		   \"user_id\": \"ad7407bb-758d-462f-a47c-db685adaea08\"
  		},
  		\"event\": false,
 		 \"date\": 1416242989746,
 		 \"discard\": \"none\",
 		 \"user_timestamp\": 1199192627
	}" \
     http://localhost:9200/sdata/rep/$(($(date +%s%N)/1000000))/
