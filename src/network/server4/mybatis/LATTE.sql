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

drop sequence SD_SEQ;
drop sequence CD_SEQ;
drop sequence AD_SEQ;
drop TRIGGER insert_sensorData;
drop TRIGGER insert_guest;

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
    flag 		varchar2(1 char) DEFAULT 'N' CHECK(flag IN ('Y', 'N')),
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
-- Sequence, Insert Test
CREATE SEQUENCE SD_SEQ 
INCREMENT BY 1 
START WITH 100 
MAXVALUE 99999 
CYCLE NOCACHE;

CREATE SEQUENCE CD_SEQ 
INCREMENT BY 1 
START WITH 100 
MAXVALUE 99999 
CYCLE NOCACHE;

CREATE SEQUENCE AD_SEQ 
INCREMENT BY 1 
START WITH 100 
MAXVALUE 99999 
CYCLE NOCACHE;

-- Trigger Test
CREATE TRIGGER insert_sensorData AFTER INSERT on Latte.sensordata 
REFERENCING NEW as NEW
FOR EACH ROW 
BEGIN 
UPDATE Latte.sensor SET recentdata=:NEW.datano WHERE sensorno=:NEW.sensorno; 
END;
/

CREATE TRIGGER insert_guest AFTER INSERT on Latte.guest 
REFERENCING NEW as NEW 
FOR EACH ROW 
BEGIN 
    INSERT INTO HOPE VALUES (:NEW.userno, '26', '70', '0', 'OPEN');
    INSERT INTO ALARM VALUES (:NEW.userno, '06', '00', '', 'N');
    INSERT INTO ALARMDATA VALUES ('AD'||LPAD(AD_SEQ.nextval, 5, 0), :NEW.userno, 'LIGHT', 'OFF', '70');
    INSERT INTO ALARMDATA VALUES ('AD'||LPAD(AD_SEQ.nextval, 5, 0), :NEW.userno, 'BED', '0', null);
    INSERT INTO ALARMDATA VALUES ('AD'||LPAD(AD_SEQ.nextval, 5, 0), :NEW.userno, 'BLIND', 'OPEN', null);
END;
/

--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
-- USER (admin)
--INSERT INTO GUEST (userno, loginid, loginpw, authcode, role) VALUES ('GU0000000001', 'admin', 'admin', 'BC5451FC5006', 'ADMIN');
INSERT INTO GUEST (userno, loginid, loginpw, authcode, role) VALUES ('ADMIN0001', 'admin', 'admin', 'BC5451FC5006', 'ADMIN');
--INSERT INTO HOPE (hopeno) VALUES ('ADMIN0001');
--INSERT INTO ALARM (alarmno) VALUES ('ADMIN0001');
-- USER (user)
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GUEST0001', 'latte1', 'latte1', 'BC5451FC5006');
--INSERT INTO HOPE (hopeno) VALUES ('GUEST0001');
--INSERT INTO ALARM (alarmno) VALUES ('GUEST0001');
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GUEST0002', 'latte2', 'latte2', 'BC5451FC5006');
--INSERT INTO HOPE (hopeno) VALUES ('GUEST0002');
--INSERT INTO ALARM (alarmno) VALUES ('GUEST0002');
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GUEST0003', 'latte3', 'latte3', 'BC5451FC5006');
--INSERT INTO HOPE (hopeno) VALUES ('GUEST0003');
--INSERT INTO ALARM (alarmno) VALUES ('GUEST0003');

-- ROOM
INSERT INTO ROOM (roomno, roomname, roomssid, imgurl) VALUES ('ROOM0001', '101', 'MULTI_GUEST', 'https://i.imgur.com/4LQp6RH.jpg');
INSERT INTO ROOM (roomno, roomname, roomssid, imgurl) VALUES ('ROOM0002', '102', 'M602', 'https://i.imgur.com/T7KCxZj.jpg');

-- RESERV
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV0001', 'GUEST0002', 'ROOM0001', TO_DATE('2020-06-10 14:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2020-06-11 11:00:00', 'yyyy-mm-dd hh24:mi:ss'));
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV0002', 'GUEST0002', 'ROOM0002', TO_DATE('2020-06-14 14:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2020-06-15 11:00:00', 'yyyy-mm-dd hh24:mi:ss'));
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV0003', 'GUEST0003', 'ROOM0002', TO_DATE('2020-06-12 14:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2020-06-13 11:00:00', 'yyyy-mm-dd hh24:mi:ss'));

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
-- Room 1
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01101', 'TEMP', 'DEVICE011');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01102', 'HUMI', 'DEVICE011');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01103', 'HEAT', 'DEVICE011');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01104', 'COOL', 'DEVICE011');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01201', 'LIGHT', 'DEVICE012');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01202', 'DOOR', 'DEVICE012');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01301', 'BED', 'DEVICE013');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN01401', 'BLIND', 'DEVICE014');
-- Room 1
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN02101', 'TEMP', 'DEVICE021');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN02102', 'HUMI', 'DEVICE021');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN02103', 'HEAT', 'DEVICE021');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN02104', 'COOL', 'DEVICE021');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN02201', 'LIGHT', 'DEVICE022');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN02202', 'DOOR', 'DEVICE022');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN02301', 'BED', 'DEVICE023');
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN02401', 'BLIND', 'DEVICE024');
--.....
--INSERT INTO SENSORDATA VALUES ('SD90001', 'SN01101', sysdate, '70', null);
--UPDATE sensor SET recentdata='SD90001' WHERE sensorno='SN01101';
--INSERT INTO SENSORDATA VALUES ('SD90002', 'SN01102', sysdate, '45', null);
--UPDATE sensor SET recentdata='SD90002' WHERE sensorno='SN01102';
--INSERT INTO SENSORDATA VALUES ('SD90003', 'SN01103', sysdate, 'off', null);
--UPDATE sensor SET recentdata='SD90003' WHERE sensorno='SN01103';
--INSERT INTO SENSORDATA VALUES ('SD90004', 'SN01104', sysdate, 'on', null);
--UPDATE sensor SET recentdata='SD90004' WHERE sensorno='SN01104';
--INSERT INTO SENSORDATA VALUES ('SD90005', 'SN01201', sysdate, 'on', '70');
--UPDATE sensor SET recentdata='SD90005' WHERE sensorno='SN01201';
--INSERT INTO SENSORDATA VALUES ('SD90006', 'SN01202', sysdate, 'close', null);
--UPDATE sensor SET recentdata='SD90006' WHERE sensorno='SN01202';

--'AAA' || board_seq.nextval
--LPAD(DEPTNO, 5)
-- Room 1
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01101', sysdate, '70', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01102', sysdate, '0', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01103', sysdate, 'off', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01104', sysdate, 'off', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01201', sysdate, 'on', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01202', sysdate, 'close', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01301', sysdate, '0', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN01401', sysdate, '0', null);
-- Room 2
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN02101', sysdate, '70', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN02102', sysdate, '0', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN02103', sysdate, 'off', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN02104', sysdate, 'off', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN02201', sysdate, 'on', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN02202', sysdate, 'close', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN02301', sysdate, '0', null);
INSERT INTO SENSORDATA VALUES ('SD'||LPAD(SD_SEQ.nextval, 5, 0), 'SN02401', sysdate, '0', null);

-- Trigger result Test
--INSERT INTO SENSORDATA VALUES ('SD90007', 'SN01202', sysdate, 'close', null);
SELECT * FROM sensor, sensordata WHERE sensor.recentdata=sensordata.datano;
SELECT * FROM sensordata;


------------------------------------------------------------------------------------------------
-- Test
------------------------------------------------------------------------------------------------
-- USER (test)
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('TESTER0001', 'A', '1', 'BC5451FC5006');  -- 99
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('TESTER0002', 'B', '1', '24F5AAEC526C');  -- 99
--INSERT INTO HOPE (hopeno) VALUES ('TESTER0001');
--INSERT INTO ALARM (alarmno) VALUES ('TESTER0001');
--INSERT INTO ALARMDATA VALUES ('AD00001', 'TESTER0001', 'LIGHT', 'off', '70');
--INSERT INTO ALARMDATA VALUES ('AD00002', 'TESTER0001', 'BED', '45', null);
--INSERT INTO ALARMDATA VALUES ('AD00003', 'TESTER0001', 'BLIND', 'OPEN', null);

--INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GU1000000002', 'tester2', 'test', '88832211278D');
--INSERT INTO HOPE (hopeno) VALUES ('GU1000000002');
--INSERT INTO ALARM (alarmno) VALUES ('GU1000000002');

-- Reserv
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV9001', 'TESTER0001', 'ROOM0001', TO_DATE('2020-06-13 14:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2020-06-14 11:00:00', 'yyyy-mm-dd hh24:mi:ss'));
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RESERV9002', 'TESTER0001', 'ROOM0002', TO_DATE('2020-06-14 14:00:00', 'yyyy-mm-dd hh24:mi:ss'), TO_DATE('2020-06-15 11:00:00', 'yyyy-mm-dd hh24:mi:ss'));





commit;

------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------
-- TEST Query ----------------------------------------------------------------------------------
SELECT * FROM guest;

-- Login
SELECT * FROM guest WHERE loginid='latte1';

-- Reserv.startdate
SELECT to_char(startdate, 'DD/MM/RR') FROM reservation WHERE reservno='RESERV0001';

-- Reserv list (Reserv + Room)
SELECT r.reservno, r.userno, r.roomno, room.roomname, room.roomssid, room.imgurl, r.startdate, r.enddate 
FROM reservation R, room WHERE r.roomno = room.roomno and r.userno = 'GUEST0002';

SELECT r.reservno, r.userno, r.roomno, room.roomname, room.roomssid, room.imgurl, r.startdate, r.enddate 
FROM reservation R, room WHERE r.roomno = room.roomno and r.userno = 'GUEST0002' and r.enddate>sysdate-1;


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
SELECT * FROM hope WHERE hopeno='GUEST0002';
SELECT * FROM alarm WHERE alarmno='GUEST0002';

SELECT d.deviceno, type FROM device d, sensor s
WHERE s.deviceno = d.deviceno and s.type='TEMP' and d.roomno='ROOM0001';



SELECT r.reservno, r.roomno, room.roomname, room.roomssid, room.imgurl, 
    to_date(startdate, 'yyyy-mm-dd hh24:mi:ss') as startdate, to_date(enddate, 'yyyy-mm-dd hh24:mi:ss') as enddate 
FROM reservation R, room 
WHERE r.userno='GUEST0002' and r.roomno=room.roomno and sysdate-1 < r.endDate;

select * from sensordata where sensorno='SN01101';
select datano, to_char(time, 'yyyy-mm-dd hh24:mi:ss') from sensordata where sensorno='SN01101';
select * from sensor where sensorno='SN01101';

select * from hope where hopeno='GUEST0002';

select * from alarm;
select * from alarm where alarmno='GUEST0002';
--update alarm set hour='7', min='30' where alarmno='GUEST0002';



--
-- instead boolean
--drop table test;
--create table test (
--    no      varchar2(2 char),
--    flag    char(1) DEFAULT 'N' CHECK(flag IN ('Y', 'N'))
--);
--
--insert into test(no) values('a');
--insert into test values('a', 'Y');
--
--select * from test;

select * from alarmdata where alarmno='GUEST0002';