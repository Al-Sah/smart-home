# Smart home

![smart-home](docs/smart-home-system.png)


## Communication between components

### Hub messages

Hub can produce several types of messages:
1. [Hub start](#Hub start)
2. [Hub stop](#Hub stop)
3. [Hub message](#Hub message)
4. [Heart beat](#Heart beat)
5. [Devices connected](#Devices connected)
6. [Devices messages](#Devices messages)
7. [Devices disconnected](#Devices disconnected)

<b> Note that each message contains "hub-id" header ! </b>
<p> Field "action" is a shortcut for the MessageAction </p>

#### Hub start
This message must be sent firstly to identify hub. <br>
Field "data" contains hub description and extra properties like heart beat period (hb)
```json
    {
        "action":"start",
        "data": "{\"description\":\"test-hub\", \"hb\":30}"
    }
```

#### Hub stop
Field "data" contains disconnection reason 
```json
    {
        "action":"off",
        "data":"Hub is shutting down"
    }
```

#### Hub message
Reserved for the future ... <br>
Notify about some changes in configuration or handle some user request and send response 
```json
    {
        "action":"hub-msg",
        "data": "important message"
    }
```

#### Heart beat
Hub must produce heart beat message with fixed period. For instance, each 30 seconds
```json
    {
        "action":"alive"
    }
```

#### Devices connected
Hub must send notification on devices connection
1. field 'id' - const device uuid 
2. field 'type' - device type. Starts with "ACTUATOR__" or "SENSOR__"
3. field 'name' - custom device name
4. field 'data' - additional information can pe passed here as string (in json format)
```json
    {
        "action": "devices-connected",
        "messages":
            [{
                "id":"device1",
                "type":"SENSOR__thermometer",
                "name":"temperature imitator1",
                "data":"{\"unit\":\"celsius\"}"
            },
            {
                "id":"device2",
                "type":"SENSOR__thermometer",
                "name":"temperature imitator2",
                "data":"{\"unit\":\"celsius\"}"
            }]
    }
```

#### Devices messages
1. field 'id' - const device uuid
2. field 'data' - parsed information given from device and presented as a string in json format
```json
    {
        "action":"msg",
        "messages":
            [{
                "id":"device1",
                "data":"23"
            },
            {
                "id":"device2",
                "data": "21"
            }]
    }
```


#### Devices disconnected
Hub must send notification on devices disconnection
1. field 'id' - const device uuid
2. field 'data' - disconnection reason
```json
    {
        "action":"devices-disconnected",
        "messages":
            [{
                "id":"device1",
                "data":"Connection lost"
            },
            {
                "id":"device2",
                "data":"Connection lost"
            }]
    }
```