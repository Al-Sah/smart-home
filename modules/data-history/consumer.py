import json
import os
import yaml

from kafka import KafkaConsumer
from pymongo import MongoClient


def load_config(fn: str):
    with open(fn, 'r') as f:
        return yaml.load(f, yaml.BaseLoader)


class Consumer:
    def __init__(self, bootstrap_servers: list[str], topic: str, group_id: str, conn_str: str, db: str, col: str):
        self.topic: str = topic
        self.consumer = KafkaConsumer(
            bootstrap_servers=bootstrap_servers,
            group_id=group_id,
            value_deserializer=lambda v: json.loads(v))

        self.mc = MongoClient(conn_str)
        self.db = db
        self.col = col

    def run(self):
        self.consumer.subscribe([self.topic])
        col = self.mc[self.db][self.col]
        for msg in self.consumer:
            value = msg.value
            if value['action'] != 'd-msg':
                continue
            data = value['data'] | {'hub': value['hub'], 'ts': msg.timestamp}
            print(data)
            col.insert_one(data)


if __name__ == '__main__':
    config_fn = os.getenv('CONFIG_FN', None)
    if not config_fn:
        raise RuntimeError('CONFIG_FN variable is not exist')

    config = load_config(config_fn)

    consumer = Consumer(
        bootstrap_servers=[config['bootstrap_server']],
        topic=config['topic'],
        group_id=config['group_id'],
        conn_str=config['conn_str'],
        db=config['db'],
        col=config['col']
    )

    consumer.run()