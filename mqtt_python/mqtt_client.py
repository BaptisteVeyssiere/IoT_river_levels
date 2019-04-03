import json
import paho.mqtt.client as mqtt
import time
import mysql.connector  # Use this library https://www.w3schools.com/python/python_mysql_insert.asp
import base64
import datetime

def on_connect(client, userdata, flags, rc):
    if(rc != 0):
        mydb = mysql.connector.connect(
            host ="localhost",
            user = "nathanael",
            passwd= "portishead",
            database= "co838",
        )

        mycursor = mydb.cursor()

        sql = "INSERT INTO monitoring_stations (error_type) VALUES (%s)"
        error_code = "MBED stations could not be reached. Error code " + str(rc)
        val = (error_code)
        mycursor.execute(sql, val)
        mydb.commit()
        mydb.close()
    print("Connected with result code : " + str(rc))
    client.subscribe("kentwatersensors/devices/+/up", 0)

def on_message(client, userdata, msg):
    time.sleep(2)
    try:
        json_string = json.loads(msg.payload.decode("utf-8"))
        print("message received " + json_string["payload_raw"])

        level = json_string["payload_raw"]
        levels = base64.b64decode(level)
        base10 = int(levels.hex(), 16)
        float10 = float(base10)
        timestamp = datetime.datetime.now()


        mydb = mysql.connector.connect(
            host ="localhost",
            user = "nathanael",
            passwd= "portishead",
            database= "co838"
        )
         # station_id, timestamp, level


        mycursor = mydb.cursor()

        sql = "INSERT INTO monitoring_stations  (station_id, latitude, longitude, type) " \
              "VALUES (%s, %s, %s, %s)"
        if(json_string["dev_id"] == "lairdc0ee4000010109f3"):
            val = (json_string["dev_id"], json_string["metadata"]["latitude"], json_string["metadata"]["longitude"],
                   "MQTT_API")
        if(json_string["dev_id"] == "lairdc0ee400001012345"):
            val = (json_string["dev_id"], json_string["metadata"]["latitude"], json_string["metadata"]["longitude"],
                   "MQTT_API")
        mycursor.execute(sql, val)
        mydb.commit()
        mydb.close()
    except mysql.connector.Error as err:
        print("Something went wrong: {}".format(err))

    if(base10 > 500 and base10 < 4000):
        print(base10)
        mydb = mysql.connector.connect(
            host="localhost",
            user="nathanael",
            passwd="portishead",
            database="co838"
        )

        mycursor = mydb.cursor()

        sql = "INSERT INTO levels (station_id, timestamp, level) VALUES (%s, %s, %d)"
        val = (json_string["dev_id"], timestamp, float10)
        mycursor.execute(sql, val)
        mydb.commit()
        mydb.close()

def main():
    client = mqtt.Client(client_id="ca9119f2-b51e-40c5-8c66-f67c03ce236a",
                              clean_session=True, userdata=None, transport="tcp")

    client.on_connect = on_connect
    client.on_message = on_message

    client.username_pw_set("kentwatersensors", "ttn-account-v2.mRzaS7HOchwKsQxdj1zD-KwjxXAptb7s9pca78Nv7_U")
    client.connect("eu.thethings.network",1883, 60)
    client.loop_forever()

if __name__ == '__main__':
    main()
