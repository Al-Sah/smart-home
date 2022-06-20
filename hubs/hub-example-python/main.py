import time
from uuid import uuid4

from sdk.python.HubProducer import HubProducer
from sdk.python.SensorImitator import SensorImitator

if __name__ == '__main__':
    producer = HubProducer(
        hub_id=str(uuid4()),
        bootstrap='188.166.82.71:9093',
        topic='hub-messages'
    )

    sensors = [SensorImitator(
        device_id=str(uuid4()),
        type='SENSOR__temperature',
        name=f'sensor-temperature-{x}'
    ) for x in range(1, 4)]

    producer.send_start()
    time.sleep(3)

    for _ in range(3):
        producer.send_heartbeat()
        for s in sensors:
            producer.send_data(s.get_data())
        time.sleep(5)
