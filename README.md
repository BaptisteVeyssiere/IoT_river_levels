co808 assessment 3

MariaDB [co838]> describe monitoring_stations;
+------------+--------------+------+-----+---------+-------+
| Field      | Type         | Null | Key | Default | Extra |
+------------+--------------+------+-----+---------+-------+
| station_id | varchar(12)  | NO   | PRI | NULL    |       |
| latitude   | decimal(8,6) | YES  |     | NULL    |       |
| longitude  | decimal(8,6) | YES  |     | NULL    |       |
+------------+--------------+------+-----+---------+-------+

MariaDB [co838]> describe subscriber;
+--------------+--------------+------+-----+---------+-------+
| Field        | Type         | Null | Key | Default | Extra |
+--------------+--------------+------+-----+---------+-------+
| phone_number | int(11)      | NO   | PRI | NULL    |       |
| latitude     | decimal(8,6) | YES  |     | NULL    |       |
| longitude    | decimal(8,6) | YES  |     | NULL    |       |
+--------------+--------------+------+-----+---------+-------+

username@129.12.44.32