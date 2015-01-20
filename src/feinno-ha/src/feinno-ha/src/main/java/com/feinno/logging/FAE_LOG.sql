drop table if exists FAE_LOG
/*
 * 日志表
 */
--mysql
create table FAE_LOG
(
	ID            int(11) auto_increment primary key comment '日志id', --bigint identity
	NAME          varchar(64) not null comment '日志名称',
	LEVEL         int(11) not null comment '日记级别', --不指定大小
	TAG           varchar(64) comment '',
	MESSAGE       varchar(2048) comment '日志信息',
	ERROR         varchar(2048) comment '错误信息',
	OCCUR_TIME    datetime not null comment '产生日志时间',
	THREAD_NAME   varchar(64) not null comment '线程名称',
	COMPUTER      varchar(32) comment '计算机名称',
	PROCESS       varchar(32) comment '',
	ENROLL_TIME   timestamp not null default current_timestamp comment '记录日志时间'	
);

--存储过程
--sqlserver
CREATE TABLE dbo.FAE_LOG(
	ID bigint identity(1,1) primary key NOT NULL,
	NAME varchar(64) COLLATE Chinese_PRC_BIN NOT NULL,
	LEVEL int NOT NULL,
	TAG varchar(64) COLLATE Chinese_PRC_BIN NULL,
	MESSAGE varchar(256) COLLATE Chinese_PRC_BIN NOT NULL,
	ERROR varchar(64) COLLATE Chinese_PRC_BIN NULL,
	OCCUR_TIME datetime NOT NULL,
	THREAD_NAME varchar(64) COLLATE Chinese_PRC_BIN NOT NULL,
	COMPUTER varchar(64) COLLATE Chinese_PRC_BIN NULL,
	PROCESS varchar(64) COLLATE Chinese_PRC_BIN NULL,
	ENROLL_TIME datetime NULL DEFAULT (getdate())
) 

procedure [dbo].[ADD_FAE_LOG_PROCEDURE]
@id  bigint,
@name  varchar(64),
@level  varchar(64),
@tag  varchar(64),
@message  varchar(256),
@error  varchar(64),
@occurTime  datetime,
@threadName  varchar(64),
@computer  varchar(64),
@process  varchar(64)
as
begin
INSERT INTO FAE_LOG(ID,NAME,LEVEL,TAG,MESSAGE,ERROR,OCCUR_TIME,
				  THREAD_NAME,COMPUTER,PROCESS,ENROLL_TIME) VALUES(@id,@name,@level,
				  @tag,@message,@error,@occurTime,@threadName,@computer,@process,current_timestamp)
end
