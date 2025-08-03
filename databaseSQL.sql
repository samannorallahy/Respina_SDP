create table jiraConfig
(
    id             integer
        constraint jiraConfig_pk
            primary key autoincrement,
    serviceAddress TEXT,
    username       TEXT,
    password       TEXT,
    itmsGroupField TEXT
);

create table request
(
    id              INTEGER default id not null
        constraint request_pk
            primary key autoincrement,
    reqID_SDP       TEXT,
    reqID_JIRA      TEXT,
    inoutJSON       TEXT,
    outputJSON      TEXT,
    jiraOutPut      TEXT,
    jiraUpdateCount integer default 0  not null,
    sdpUpdateCount  integer default 0
);

create table sdpConfig
(
    id                      integer
        constraint sdpConfig_pk
            primary key autoincrement,
    serviceAddress          TEXT,
    authtoken               TEXT,
    statusNameForJiraUpdate text
);

