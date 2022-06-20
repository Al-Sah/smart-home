import logging
import json
import time

from kafka import KafkaProducer
from kafka.errors import KafkaError


class HubProducer:
    def __int__(self, hub_id: str, bootstrap: str, topic: str):
        self.hub_id: str = hub_id
        self.topic: str = topic
        self.kafka_producer = KafkaProducer(
            bootstrap_servers=[bootstrap],
            value_serializer=lambda m: json.dumps(m).encode('ascii'),
            request_timeout_ms=60000
        )

    def send_data(self, data: dict):
        self.kafka_producer.send(topic=self.topic, value=data)
        self.kafka_producer.flush()
