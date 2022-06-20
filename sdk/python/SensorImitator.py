import random as rd


class SensorImitator:
    """
    Class is used to imitate real device
    """
    def __init__(self, device_id: str, type: str, name: str):
        """

        :param device_id: uuid of device
        :param type: device type, starts with "ACTUATOR__" or "SENSOR__"
        :param name: device name
        :return:
        """
        self.device_id: str = device_id
        self.type: str = type
        self.name: str = name

    def get_data(self) -> dict:
        """
        Returns device data
        :return: dict of values
        """
        return {
            'value': rd.uniform(-100, 100)
        }
