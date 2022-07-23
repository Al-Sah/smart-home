#! /bin/bash


export SERVER_IP="${1:-localhost}"
# SERVER_IP is used in the docker-compose

docker-compose up -d
sleep 5  # Waits 5 seconds  (Broker may not be available)


if [[ $(docker ps | grep -E bitnami/'kafka|zookeeper' -c) != 2 ]]; then
  echo "ERROR: There are must be 2 containers (bitnami/kafka and bitnami/zookeeper)"
  exit
fi



function create_topic(){

  local topic_name="$1" # must be passed
  if [[ "${topic_name}" == "" ]]; then
    echo "ERROR: create_topic failure: topic name is empty"
    exit
  fi

  local r_factor="${3:-1}"          # default value: 1
  local partitions="${4:-1}"        # default value: 1

  docker compose exec kafka	/opt/bitnami/kafka/bin/kafka-topics.sh  \
      --create		                                                  \
      --bootstrap-server "${SERVER_IP}":9093	                      \
      --replication-factor "${r_factor}"                            \
      --partitions "${partitions}"                                  \
      --topic "${topic_name}"
}

create_topic hubs-messages "${SERVER_IP}"
create_topic modules-messages "${SERVER_IP}"