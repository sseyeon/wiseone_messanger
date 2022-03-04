# wiseone_messanger

create table msg_template
(
template_id   char(8)          not null comment '템플릿 ID lpad(값, 8, 0)'
primary key,
type          char             not null comment 'E: Email , M : Message',
title         varchar(200)     not null comment '템플릿 제목',
message_body  text             null comment 'Message 본문',
template_name varchar(250)     null comment 'Email 템플릿 파일명',
use_yn        char default 'Y' not null comment '사용여부'
);

create table msg_channel
(
channel_id char(8)  not null comment '체널 ID' primary key,
channel_name     varchar(200)  not null comment '체널 명',
use_yn char(1) not null default('Y') comment '사용여부'
);

create table msg_channel_menber
(
channel_id char(8)  not null comment '체널 ID',
email  varchar(200) not null comment '체널 첨석자',
use_yn char(1) not null default('Y') comment '사용여부'
);