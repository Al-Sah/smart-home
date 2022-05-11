#! /bin/bash




docker-compose up -d
sleep 2  # Waits 2 seconds  (Broker may not be available)


if [[ $(docker ps | grep bitnami -c) != 2 ]]; then
  echo "ERROR: There are must be 2 containers (bitnami/kafka, bitnami/zookeeper)"
  exit
fi



function create_topic(){

  local topic_name="$1" # must be passed
  if [[ "${topic_name}" == "" ]]; then
    echo "ERROR: create_topic failure: topic name is empty"
    exit
  fi

  local server_ip="${2:-localhost}" # default value: localhost
  local r_factor="${3:-1}"          # default value: 1
  local partitions="${4:-1}"        # default value: 1

  docker compose exec kafka	/opt/bitnami/kafka/bin/kafka-topics.sh  \
      --create		                                                  \
      --bootstrap-server "${server_ip}":9092	                      \
      --replication-factor "${r_factor}"                            \
      --partitions "${partitions}"                                  \
      --topic "${topic_name}"
}


server_ip="${1:-localhost}"

create_topic hubs-messages "${server_ip}"
create_topic modules-messages "${server_ip}"