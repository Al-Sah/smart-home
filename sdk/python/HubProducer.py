import logging
import json
import time

from kafka import KafkaProducer
from kafka.errors import KafkaError


class HubProducer:
    def __init__(self, hub_id: str, bootstrap: str, topic: str):
        """

        :param hub_id: hub uuid
        :param bootstrap: bootstrap 'host:port'
        :param topic: topic to send data
        :return:
        """
        self.hub_id: str = hub_id
        self.topic: str = topic
        self.kafka_producer = KafkaProducer(
            bootstrap_servers=[bootstrap],
            value_serializer=lambda m: json.dumps(m).encode('ascii'),
            request_timeout_ms=60000
        )

    def send(self, action: str, data: dict):
        self.kafka_producer.send(topic=self.topic, value={
            'hub_id': self.hub_id,
            'action': action,
            'data': data
        })
        self.kafka_producer.flush()

    def send_start(self):
        self.send(action='hub-start', data={'hb': 30, 'description': 'test'})

    def send_shutdown(self):
        self.send(action='hub-shutdown', data={'hb': 30, 'description': 'test'})

    def send_data(self, data: dict) -> None:
        self.send(action='hub-message', data=data)

    def send_heartbeat(self):
        self.send(action='hub-heartbeat', data={'ts': time.time()})
