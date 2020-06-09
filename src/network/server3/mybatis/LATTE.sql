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
    roomimg 	varchar2(128 char)
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
    hour 		number(2) DEFAULT 06,
    min 		number(2) DEFAULT 00,
    weeks 	varchar2(30 char) DEFAULT '',
    flag 		char(1) DEFAULT 0,
    CONSTRAINT ALARM_FK_USER FOREIGN KEY(alarmno) REFERENCES GUEST(userno) ON DELETE CASCADE
);

CREATE TABLE ALARMDATA (
    datano 	varchar2(12 char) CONSTRAINT ALARMDATA_PK PRIMARY KEY,
    alarmno 	varchar2(12 char) NOT NULL,
    sensorno 	varchar2(12 char) NOT NULL,
    states 	varchar2(12 char),
    stateDetail 	varchar2(12 char),
    CONSTRAINT ALARMDATA_FK_ALARM FOREIGN KEY(alarmno) REFERENCES ALARM(alarmno) ON DELETE CASCADE
);

--------------------------------------------------------------------------------------------------
-- USER
INSERT INTO GUEST (userno, loginid, loginpw, authcode, role) VALUES ('GU0000000001', 'admin', 'admin', 'BC5451FC5006', 'ADMIN');
INSERT INTO HOPE (hopeno) VALUES ('GU0000000001');
INSERT INTO ALARM (alarmno) VALUES ('GU0000000001');
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GU0000000002', 'latte1', 'latte1', 'BC5451FC5006');
INSERT INTO HOPE (hopeno) VALUES ('GU0000000002');
INSERT INTO ALARM (alarmno) VALUES ('GU0000000002');
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GU0000000003', 'latte2', 'latte2', 'BC5451FC5006');
INSERT INTO HOPE (hopeno) VALUES ('GU0000000003');
INSERT INTO ALARM (alarmno) VALUES ('GU0000000003');
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GU0000000004', 'latte3', 'latte3', 'BC5451FC5006');
INSERT INTO HOPE (hopeno) VALUES ('GU0000000004');
INSERT INTO ALARM (alarmno) VALUES ('GU0000000004');
-- USER for test
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GU1000000001', 'tester1', 'test', '24F5AAEC526C');
INSERT INTO HOPE (hopeno) VALUES ('GU1000000001');
INSERT INTO ALARM (alarmno) VALUES ('GU1000000001');
INSERT INTO GUEST (userno, loginid, loginpw, authcode) VALUES ('GU1000000002', 'tester2', 'test', '88832211278D');
INSERT INTO HOPE (hopeno) VALUES ('GU1000000002');
INSERT INTO ALARM (alarmno) VALUES ('GU1000000002');

-- ROOM
INSERT INTO ROOM (roomno, roomname, roomssid, roomimg) VALUES ('RM0000000001', '101', 'MULTI_GUEST', 'https://i.imgur.com/4LQp6RH.jpg');
INSERT INTO ROOM (roomno, roomname, roomssid, roomimg) VALUES ('RM0000000002', '102', 'M602', 'https://i.imgur.com/T7KCxZj.jpg');

-- RESERV
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RE0000000001', 'GU0000000002', 'RM0000000001', TO_DATE('2020-06-10', 'YYYY-MM-DD'), TO_DATE('2020-06-12', 'YYYY-MM-DD'));
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RE0000000002', 'GU0000000002', 'RM0000000002', TO_DATE('2020-06-15', 'YYYY-MM-DD'), TO_DATE('2020-06-20', 'YYYY-MM-DD'));
INSERT INTO RESERVATION (reservno, userno, roomno, startdate, enddate) 
VALUES ('RE0000000003', 'GU0000000003', 'RM0000000002', TO_DATE('2020-06-12', 'YYYY-MM-DD'), TO_DATE('2020-06-13', 'YYYY-MM-DD'));

-- DEVICE
INSERT INTO DEVICE VALUES ('DEVICE001001', 'RM0000000001');
INSERT INTO DEVICE VALUES ('DEVICE001002', 'RM0000000002');
INSERT INTO DEVICE VALUES ('DEVICE002001', 'RM0000000001');
INSERT INTO DEVICE VALUES ('DEVICE002002', 'RM0000000002');
INSERT INTO DEVICE VALUES ('DEVICE003001', 'RM0000000001');
INSERT INTO DEVICE VALUES ('DEVICE003002', 'RM0000000002');
INSERT INTO DEVICE VALUES ('DEVICE004001', 'RM0000000001');
INSERT INTO DEVICE VALUES ('DEVICE004002', 'RM0000000002');

-- SENSOR
INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN0000000001', 'TEMP', 'DEVICE001001');
--INSERT INTO SENSOR (sensorno, type, deviceno) VALUES ('SN0000000002', 'LIGHT', 'DEVICE001001');
--.....


commit;

-- TEST Query ----------------------------------------------------------------------------------

-- Login
SELECT * FROM guest WHERE loginid='latte1';

-- Reserv.startdate
SELECT to_char(startdate, 'DD/MM/RR') FROM reservation WHERE reservno='RE0000000001';

-- Reserv list (Reserv + Room)
SELECT r.reservno, r.userno, r.roomno, room.roomname, room.roomssid, room.roomimg, r.startdate, r.enddate 
FROM reservation R, room WHERE r.roomno = room.roomno and r.userno = 'GU0000000002';

-- DeviceNo from SenserData.sensorNo
SELECT s.deviceno FROM sensor S WHERE s.sensorno = 'SN0000000001';