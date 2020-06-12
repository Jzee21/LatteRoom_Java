drop table ALARMDATA;
drop table ALARM;
drop table CONTROLDATA;
drop table SENSORDATA;
drop table SENSOR;
drop table DEVICE;
drop table RESERVATION;
drop table ROOM;
drop table HOPE;
drop table GUEST;

CREATE TABLE GUEST (
    userno 	varchar2(12 char) CONSTRAINT GUSET_PK PRIMARY KEY,
    loginid 	varchar2(15 char),
    loginpw 	varchar2(15 char) NOT NULL,
    authcode 	varchar2(12 char) NOT NULL,
    role 	varchar2(8 char) DEFAULT 'USER'
);

CREATE TABLE HOPE (
    hopeno 	varchar2(12 char) CONSTRAINT HOPE_PK PRIMARY KEY,
    temp 	varchar2(8 char) DEFAULT '26',
    light 	varchar2(8 char) DEFAULT '70',
    bed 	varchar2(8 char) DEFAULT '0',
    blind 	varchar2(8 char) DEFAULT 'open',
    CONSTRAINT HOPE_FK_USER FOREIGN KEY(hopeno) REFERENCES GUEST(userno) ON DELETE CASCADE
);

CREATE TABLE ROOM (
    roomno 	varchar2(12 char) CONSTRAINT ROOM_PK PRIMARY KEY,
    roomname 	varchar2(15 char) NOT NULL,
    roomssid 	varchar2(15 char) NOT NULL,
    imgurl      varchar2(128 char)
);

CREATE TABLE RESERVATION (
    reservno 	varchar2(12 char) CONSTRAINT RESERV_PK PRIMARY KEY,
    userno    	varchar2(12 char) NOT NULL,
    roomno 	varchar2(12 char) NOT NULL,
    startdate 	date NOT NULL,
    enddate 	date NOT NULL,
    CONSTRAINT RESERV_FK_USER FOREIGN KEY(userno) REFERENCES GUEST(userno) ON DELETE CASCADE,
    CONSTRAINT RESERV_FK_ROOM FOREIGN KEY(roomno) REFERENCES ROOM(roomno) ON DELETE CASCADE
);

CREATE TABLE DEVICE (
    deviceno 	varchar2(12 char) CONSTRAINT DEVICE_PK PRIMARY KEY,
    roomno 	varchar2(12 char) NOT NULL,
    CONSTRAINT DEVICE_FK_ROOM FOREIGN KEY(roomno) REFERENCES ROOM(roomno) ON DELETE CASCADE
);

CREATE TABLE SENSOR (
    sensorno 	varchar2(12 char) CONSTRAINT SENSOR_PK PRIMARY KEY,
    type 	varchar2(12 char) NOT NULL,
    deviceno 	varchar2(12 char) NOT NULL,
    recentData 	varchar2(12 char),
    CONSTRAINT SENSOR_FK_DEVICE FOREIGN KEY(deviceno) REFERENCES DEVICE(deviceno) ON DELETE CASCADE
);

CREATE TABLE SENSORDATA (
    datano 	varchar2(12 char) CONSTRAINT SENSORDATA_PK PRIMARY KEY,
    sensorno 	varchar2(12 char) NOT NULL,
    time 	date NOT NULL,
    states 	varchar2(12 char) NOT NULL,
    stateDetail 	varchar2(12 char),
    CONSTRAINT SENSORDATA_FK_SENSOR FOREIGN KEY(sensorno) REFERENCES SENSOR(sensorno) ON DELETE CASCADE
);

CREATE TABLE CONTROLDATA (
    datano 	varchar2(12 char) CONSTRAINT CONTROLDATA_PK PRIMARY KEY,
    sensorno 	varchar2(12 char) NOT NULL,
    time 	date NOT NULL,
    states 	varchar2(12 char) NOT NULL,
    stateDetail 	varchar2(12 char),
    CONSTRAINT CONTROLDATA_FK_SENSOR FOREIGN KEY(sensorno) REFERENCES SENSOR(sensorno) ON DELETE CASCADE
);

CREATE TABLE ALARM (
    alarmno 	varchar2(12 char) CONSTRAINT ALARM_PK PRIMARY KEY,
    hour 		varchar2(2 char) DEFAULT '06',
    min 		varchar2(2 char) DEFAULT '00',
    weeks 	    varchar2(30 char) DEFAULT '',
    flag 		char(1) DEFAULT 0,
    CONSTRAINT ALARM_FK_USER FOREIGN KEY(alarmno) REFERENCES GUEST(userno) ON DELETE CASCADE
);

CREATE TABLE ALARMDATA (
    datano 	varchar2(12 char) CONSTRAINT ALARMDATA_PK PRIMARY KEY,
    alarmno 	varchar2(12 char) NOT NULL,
    type 	varchar2(12 char) NOT NULL,
    states 	varchar2(12 char),
    stateDetail 	varchar2(12 char),
    CONSTRAINT ALARMDATA_FK_ALARM FOREIGN KEY(alarmno) REFERENCES ALARM(alarmno) ON DELETE CASCADE
);

--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
-- USER (admin)
--INSERT INTO GUEST (userno, loginid, loginpw, authcode, role) VALUES ('GU0000000001', 'admin', 'admin', 'BC5451FC5006', 'ADMIN');
INSERT INTO GUEST (userno, loginid, loginpw, authcode, role) VALUES ('ADMIN0001', 'admin', 'admin', 'BC5451FC5006', 'ADMIN');
INSERT INTO HOPE (hopeno) VALUES ('ADMIN0001');
INSERT INTO ALARM (alarmno) VALUES ('ADMIN0001');
-- USER (user)
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GUEST0001', 'latte1', 'latte1', 'BC5451FC5006');
INSERT INTO HOPE (hopeno) VALUES ('GUEST0001');
INSERT INTO ALARM (alarmno) VALUES ('GUEST0001');
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GUEST0002', 'latte2', 'latte2', 'BC5451FC5006');
INSERT INTO HOPE (hopeno) VALUES ('GUEST0002');
INSERT INTO ALARM (alarmno) VALUES ('GUEST0002');
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GUEST0003', 'latte3', 'latte3', 'BC5451FC5006');
INSERT INTO HOPE (hopeno) VALUES ('GUEST0003');
INSERT INTO ALARM (alarmno) VALUES ('GUEST0003');

-- ROOM
INSERT INTO ROOM (roomno, roomname, roomssid, imgurl) VALUES ('ROOM0001', '101', 'MULTI_GUEST', 'https://i.imgur.com/4LQp6RH.jpg');
INSERT INTO ROOM (roomno, roomname, roomssid, imgurl) VALUES ('ROOM0002', '102', 'M602', 'https://i.imgur.com/T7KCxZj.jpg');

-- RESERV
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV0001', 'GUEST0002', 'ROOM0001', TO_DATE('2020-06-10', 'YYYY-MM-DD'), TO_DATE('2020-06-11', 'YYYY-MM-DD'));
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV0002', 'GUEST0002', 'ROOM0002', TO_DATE('2020-06-15', 'YYYY-MM-DD'), TO_DATE('2020-06-20', 'YYYY-MM-DD'));
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV0003', 'GUEST0003', 'ROOM0002', TO_DATE('2020-06-12', 'YYYY-MM-DD'), TO_DATE('2020-06-13', 'YYYY-MM-DD'));

-- DEVICE
INSERT INTO DEVICE VALUES ('DEVICE011', 'ROOM0001');
INSERT INTO DEVICE VALUES ('DEVICE012', 'ROOM0001');
INSERT INTO DEVICE VALUES ('DEVICE013', 'ROOM0001');
INSERT INTO DEVICE VALUES ('DEVICE014', 'ROOM0001');
INSERT INTO DEVICE VALUES ('DEVICE021', 'ROOM0002');
INSERT INTO DEVICE VALUES ('DEVICE022', 'ROOM0002');
INSERT INTO DEVICE VALUES ('DEVICE023', 'ROOM0002');
INSERT INTO DEVICE VALUES ('DEVICE024', 'ROOM0002');

-- SENSOR
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01101', 'TEMP', 'DEVICE011');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01102', 'HUMI', 'DEVICE011');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01103', 'HEAT', 'DEVICE011');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01104', 'COOL', 'DEVICE011');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01201', 'LIGHT', 'DEVICE012');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01202', 'DOOR', 'DEVICE012');
--.....
INSERT INTO SENSORDATA VALUES ('SD90001', 'SN01101', sysdate, '70', null);
UPDATE sensor SET recentdata='SD90001' WHERE sensorno='SN01101';
INSERT INTO SENSORDATA VALUES ('SD90002', 'SN01102', sysdate, '45', null);
UPDATE sensor SET recentdata='SD90002' WHERE sensorno='SN01102';
INSERT INTO SENSORDATA VALUES ('SD90003', 'SN01103', sysdate, 'off', null);
UPDATE sensor SET recentdata='SD90003' WHERE sensorno='SN01103';
INSERT INTO SENSORDATA VALUES ('SD90004', 'SN01104', sysdate, 'on', null);
UPDATE sensor SET recentdata='SD90004' WHERE sensorno='SN01104';
INSERT INTO SENSORDATA VALUES ('SD90005', 'SN01201', sysdate, 'on', '70');
UPDATE sensor SET recentdata='SD90005' WHERE sensorno='SN01201';
INSERT INTO SENSORDATA VALUES ('SD90006', 'SN01202', sysdate, 'close', null);
UPDATE sensor SET recentdata='SD90006' WHERE sensorno='SN01202';

-- Trigger Test
CREATE TRIGGER insert_sensorData AFTER INSERT on Latte.sensordata 
REFERENCING NEW as NEW
FOR EACH ROW 
BEGIN 
UPDATE Latte.sensor SET recentdata=:NEW.datano WHERE sensorno=:NEW.sensorno; 
END;

commit;

-- Trigger result Test
INSERT INTO SENSORDATA VALUES ('SD90007', 'SN01202', sysdate, 'close', null);
SELECT * FROM sensor, sensordata WHERE sensor.recentdata=sensordata.datano;
SELECT * FROM sensordata;

-- Sequence, Insert Test
CREATE SEQUENCE SD_SEQ 
INCREMENT BY 1 
START WITH 10 
MAXVALUE 99999 
CYCLE NOCACHE;

INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01202', sysdate, 'open', null);
--'AAA' || board_seq.nextval
--LPAD(DEPTNO, 5)

------------------------------------------------------------------------------------------------
-- USER (test)
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('TESTER0001', 'tester1', 'test', '24F5AAEC526C');  -- 99
INSERT INTO HOPE (hopeno) VALUES ('TESTER0001');
INSERT INTO ALARM (alarmno) VALUES ('TESTER0001');
INSERT INTO ALARMDATA VALUES ('AD00001', 'TESTER0001', 'LIGHT', 'off', '70');
INSERT INTO ALARMDATA VALUES ('AD00002', 'TESTER0001', 'BED', '45', null);
INSERT INTO ALARMDATA VALUES ('AD00003', 'TESTER0001', 'BLIND', 'OPEN', null);
--INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GU1000000002', 'tester2', 'test', '88832211278D');
--INSERT INTO HOPE (hopeno) VALUES ('GU1000000002');
--INSERT INTO ALARM (alarmno) VALUES ('GU1000000002');

-- Reserv
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV9001', 'TESTER0001', 'ROOM0001', TO_DATE('2020-06-13', 'YYYY-MM-DD'), TO_DATE('2020-06-14', 'YYYY-MM-DD'));
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV9002', 'TESTER0001', 'ROOM0002', TO_DATE('2020-06-14', 'YYYY-MM-DD'), TO_DATE('2020-06-15', 'YYYY-MM-DD'));







-- TEST Query ----------------------------------------------------------------------------------
SELECT * FROM guest;

-- Login
SELECT * FROM guest WHERE loginid='latte1';

-- Reserv.startdate
SELECT to_char(startdate, 'DD/MM/RR') FROM reservation WHERE reservno='RESERV0001';

-- Reserv list (Reserv + Room)
SELECT r.reservno, r.userno, r.roomno, room.roomname, room.roomssid, room.imgurl, r.startdate, r.enddate 
FROM reservation R, room WHERE r.roomno = room.roomno and r.userno = 'GUEST0002';

-- DeviceNo from SenserData.sensorNo
SELECT s.deviceno FROM sensor S WHERE s.sensorno = 'SN01101';


-------
-- Hope
SELECT * FROM hope WHERE hopeno='TESTER0001';
-- Alarm
SELECT * FROM alarm WHERE alarmno='TESTER0001';
-- AlarmData
SELECT * FROM alarmData WHERE alarmno='TESTER0001';

SELECT * FROM sensor, sensordata WHERE sensor.sensorno='SN01101';
SELECT * FROM sensor, sensordata WHERE sensor.sensorno='SN01101' and sensor.recentdata=sensordata.datano;
SELECT s.sensorno, s.type, s.deviceno, sd.time, sd.states, sd.statedetail FROM sensor s, sensordata sd WHERE s.sensorno='SN01101' and s.recentdata=sd.datano;

SELECT * FROM reservation;
SELECT * FROM reservation WHERE startdate<=sysdate and enddate<sysdate+1;
select to_char(sysdate-1, 'YYYY-MM-DD HH:MM') from dual;

select to_char(r.enddate, 'YYYY-MM-DD HH24:MM') from reservation r;

SELECT * FROM device, sensor WHERE sensor.deviceno = device.deviceno;
SELECT * FROM device d, sensor s WHERE s.deviceno = d.deviceno and d.deviceno='DEVICE011';
SELECT s.type, s.sensorno FROM device D, sensor S WHERE d.deviceno='DEVICE011' and s.deviceno = d.deviceno;

SELECT d.deviceno, s.sensorno, s.type, sd.states, sd.statedetail FROM device d, sensor s, sensordata sd 
WHERE d.deviceno=s.deviceno and s.recentdata=sd.datano;
-- RoomDetail
SELECT d.deviceno, s.sensorno, s.type, sd.states, sd.statedetail FROM device d, sensor s, sensordata sd 
WHERE d.roomno='ROOM0001' and d.deviceno=s.deviceno and s.recentdata=sd.datano;

SELECT d.deviceno, type FROM device d, sensor s
WHERE s.deviceno = d.deviceno and s.type='TEMP' and d.roomno='ROOM0001';
