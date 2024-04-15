create table course
(
    id         bigint generated by default as identity,
    title      varchar(255) not null,
    creator_id bigint       not null,
    created_at timestamp    not null,
    updated_at timestamp,
    primary key (id)
);

create table ns_user
(
    id         bigint generated by default as identity,
    user_id    varchar(20) not null,
    password   varchar(20) not null,
    name       varchar(20) not null,
    email      varchar(50),
    created_at timestamp   not null,
    updated_at timestamp,
    primary key (id)
);

create table question
(
    id         bigint generated by default as identity,
    created_at timestamp    not null,
    updated_at timestamp,
    contents   clob,
    deleted    boolean      not null,
    title      varchar(100) not null,
    writer_id  bigint,
    primary key (id)
);

create table answer
(
    id          bigint generated by default as identity,
    created_at  timestamp not null,
    updated_at  timestamp,
    contents    clob,
    deleted     boolean   not null,
    question_id bigint,
    writer_id   bigint,
    primary key (id)
);

create table delete_history
(
    id            bigint not null,
    content_id    bigint,
    content_type  varchar(255),
    created_date  timestamp,
    deleted_by_id bigint,
    primary key (id)
);

create table cover_image
(
    id       bigint generated by default as identity,
    name     varchar(255) not null,
    capacity double precision,
    width    double precision,
    height   double precision,
    type     varchar(10),
    primary key (id)
);

create table session
(
    id                       bigint generated by default as identity,
    title                    varchar(255) not null,
    description              varchar(255) not null,
    max_number_of_enrollment bigint       not null default 0,
    fee                      bigint       not null default 0,
    status                   varchar(255) not null,
    gathering_status         varchar(255) not null,
    started_at               timestamp    not null,
    ended_at                 timestamp    not null,
    cover_image_id           bigint,
    course_id                bigint,
    created_at               timestamp    not null,
    updated_at               timestamp,
    primary key (id),
    foreign key (course_id) references course (id),
    foreign key (cover_image_id) references cover_image (id)
);

create table enrollment
(
    id         bigint generated by default as identity,
    user_id    bigint,
    session_id bigint,
    created_at timestamp not null,
    primary key (id),
    unique (user_id, session_id),
    foreign key (user_id) references ns_user (id),
    foreign key (session_id) references session (id)
);