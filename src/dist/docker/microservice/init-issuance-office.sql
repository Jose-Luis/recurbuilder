create table office
(
  id bigint not null
    constraint pk_office
      primary key,
  code varchar(255)
);

alter table office owner to issuance_office;

create table client
(
  id bigint not null
    constraint pk_client
      primary key,
  code varchar(255)
    constraint uk_cli_code
      unique,
  office_id bigint
    constraint fk_cli_office
      references office
);

alter table client owner to issuance_office;

create table market
(
  id bigint not null
    constraint pk_market
      primary key,
  code varchar(255)
    constraint uk_mar_code
      unique,
  description varchar(255),
  name varchar(255),
  office_id bigint
    constraint fk_mar_office
      references office
);

alter table market owner to issuance_office;

create table country
(
  id bigint not null
    constraint pk_country
      primary key,
  code varchar(255)
    constraint uk_con_code
      unique,
  name varchar(255),
  market_id bigint
    constraint fk_cou_market
      references market,
  office_id bigint
    constraint fk_cou_office
      references office
);

alter table country owner to issuance_office;


