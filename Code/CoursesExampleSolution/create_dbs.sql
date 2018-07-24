CONNECT 'jdbc:derby:courses;create=true';
CREATE TABLE course (Name VARCHAR(100), Credits INT);
CREATE TABLE offering (ID INT, Course VARCHAR(100), DateTime VARCHAR(255));
CREATE TABLE schedule (Name VARCHAR(100),OfferingId INT);