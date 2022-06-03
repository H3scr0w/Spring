CREATE SCHEMA website4sg DEFAULT CHARACTER SET utf8;

USE website4sg;

create table accessright (acri_id bigint not null auto_increment, proj_id bigint, role_id bigint, user_id bigint, primary key (acri_id)) engine=InnoDB;
create table certificate (cert_id bigint not null auto_increment, cert_code varchar(255), cert_name varchar(255), cert_created date, cert_csr longblob, cert_intermediate longblob, cert_key longblob, cert_lastupdate date, cert_passphrase varchar(255), cert_updatedby varchar(255), cert_value longblob, primary key (cert_id)) engine=InnoDB;
create table cms (cms_id bigint not null auto_increment, cms_bin_repo_url varchar(255), cms_code varchar(255), cms_code_repo_url varchar(255), cms_enable bit, cms_name varchar(255), primary key (cms_id)) engine=InnoDB;
create table deployment (depl_id bigint not null auto_increment, depl_created date, depl_deliverable_code varchar(255), depl_deliverable_version varchar(255), dest_id varchar(255), dety_id varchar(255), depl_docroot_code varchar(255), depl_environment_code varchar(255), proj_id bigint, depl_requester varchar(255), depl_rundeck_jobid varchar(255), depl_lastupdate date, depl_validatedby varchar(255), primary key (depl_id)) engine=InnoDB;
create table deploymentcommand (dcmd_id bigint not null auto_increment, dcmd_command varchar(255), depl_id bigint, deps_id varchar(255), dcmd_order integer, primary key (dcmd_id)) engine=InnoDB;
create table deploymentstatus (dest_id varchar(255) not null, dest_enable bit, dest_label varchar(255), primary key (dest_id)) engine=InnoDB;
create table deploymentstep (deps_id varchar(255) not null, deps_enable bit, deps_label varchar(255), primary key (deps_id)) engine=InnoDB;
create table deploymenttype (dety_id varchar(255) not null, dety_enable bit, dety_label varchar(255), primary key (dety_id)) engine=InnoDB;
create table docroot (docr_id bigint not null auto_increment, docr_code varchar(255), hopr_id bigint, docr_name varchar(255), docr_rundeck_job_api_url varchar(255), primary key (docr_id)) engine=InnoDB;
create table docrootenvcertif (fk_doen_id bigint not null, fk_cert_id bigint not null) engine=InnoDB;
create table docrootenvdomain (fk_doen_id bigint not null, fk_doma_id bigint not null) engine=InnoDB;
create table docrootenvironment (doen_id bigint not null auto_increment, doen_can_auto_deploy bit default 0, cms_id bigint, doen_cmsversion varchar(255), docr_id bigint, ddc_id bigint, doen_drupaldocrootcoreversion varchar(255), envi_id bigint, doen_is_basic_auth bit, doen_password varchar(255), doen_realm varchar(255), doen_user varchar(255), primary key (doen_id)) engine=InnoDB;
create table docrootenvloadblancer (fk_doen_id bigint not null, fk_loba_id bigint not null) engine=InnoDB;
create table docrootenvservers (fk_doen_id bigint not null, fk_serv_id bigint not null) engine=InnoDB;
create table domain (doma_id bigint not null auto_increment, doma_code varchar(255), doma_httpsenable bit, doma_is_basic_auth bit, doma_is_qualys_enable bit, doma_name varchar(255), doma_password varchar(255), doma_realm varchar(255), regi_id bigint, doma_use_doen_auth bit, doma_user varchar(255), doma_wafid varchar(255), primary key (doma_id)) engine=InnoDB;
create table domainloadbalancer (dolb_id bigint not null auto_increment, doma_id bigint, loba_id bigint, webs_id bigint, primary key (dolb_id)) engine=InnoDB;
create table drupaldocrootcore (ddc_id bigint not null auto_increment, ddc_bin_repo_url varchar(255), ddc_code varchar(255), ddc_code_repo_url varchar(255), ddc_name varchar(255), primary key (ddc_id)) engine=InnoDB;
create table environment (envi_id bigint not null auto_increment, envi_code varchar(255), envi_name varchar(255), primary key (envi_id)) engine=InnoDB;
create table hostingprovider (hopr_id bigint not null auto_increment, hopr_code varchar(255), hopr_name varchar(255), primary key (hopr_id)) engine=InnoDB;
create table loadbalancer (loba_id bigint not null auto_increment, loba_code varchar(255), loba_fqdn varchar(255), hopr_id bigint, loba_ip varchar(255), loba_ip2 varchar(255), loba_name varchar(255), primary key (loba_id)) engine=InnoDB;
create table project (proj_id bigint not null auto_increment, drupaldocrootcore_ddc_id bigint, website_webs_id bigint, primary key (proj_id)) engine=InnoDB;
create table registar (regi_id bigint not null auto_increment, regi_code varchar(255), regi_name varchar(255), primary key (regi_id)) engine=InnoDB;
create table report (report_id bigint not null auto_increment, report_created date, file longblob, scan_scan_id bigint, primary key (report_id)) engine=InnoDB;
create table roles (role_id bigint not null auto_increment, role_label varchar(255), primary key (role_id)) engine=InnoDB;
create table scan (scan_id bigint not null auto_increment, scan_created date, proj_id bigint, ref_id1 varchar(255), ref_id2 varchar(255), ref_id3 varchar(255), report_tool_id varchar(255), scst_id varchar(255), scan_tool_id varchar(255), scty_id varchar(255), primary key (scan_id)) engine=InnoDB;
create table scanstatus (scst_id varchar(255) not null, primary key (scst_id)) engine=InnoDB;
create table scantype (scty_id varchar(255) not null, primary key (scty_id)) engine=InnoDB;
create table server (serv_id bigint not null auto_increment, serv_created date, serv_domain varchar(255), serv_enable bit, serv_hostname varchar(255), serv_lastupdate date, serv_login varchar(255), serv_sshserver bit, primary key (serv_id)) engine=InnoDB;
create table users (user_id bigint not null auto_increment, user_company varchar(255), user_email varchar(255), user_firstname varchar(255), user_isadmin bit, user_lastname varchar(255), primary key (user_id)) engine=InnoDB;
create table website (webs_id bigint not null auto_increment, webs_bin_repo_url varchar(255), webs_code varchar(255), webs_code_repo_url varchar(255), webs_created date, webs_enable bit, webs_home_dir varchar(255), webs_is_qualys_enable bit, webs_lastupdate date, webs_name varchar(255), webs_qualys_webapp_id bigint, primary key (webs_id)) engine=InnoDB;
create table websitesdeployed (wede_id bigint not null auto_increment, doen_id bigint, webs_id bigint, wede_website_version varchar(255), primary key (wede_id)) engine=InnoDB;
alter table certificate add constraint UK_nyhj07jf5myiwbbxurawu4f8f unique (cert_code);
alter table cms add constraint UK_2be2w0qake0krae5sjyldqgs4 unique (cms_code);
alter table docroot add constraint UK_qkniu8fq5rwg6raoxd27tt7ii unique (docr_code);
alter table domain add constraint UK_6uabvm75kom15s6nax33whjlw unique (doma_code);
alter table drupaldocrootcore add constraint UK_15949feyb1nk9rtnfspewx0bn unique (ddc_code);
alter table environment add constraint UK_1g1tfxr4e39cs7hkha8fwcelf unique (envi_code);
alter table hostingprovider add constraint UK_9p9qxm2plbbljojkgt252u8hp unique (hopr_code);
alter table loadbalancer add constraint UK_7a7t9y36xx1cmdmvgpc5brkau unique (loba_code);
alter table registar add constraint UK_27mgvwinwmm6ry00bvm9jt6fg unique (regi_code);
alter table scan add constraint UK_3eu721ypfvgf05ityxsc7cn5j unique (report_tool_id);
alter table scan add constraint UK_4d68h7yk4qq1yw8kvsi0rnfgu unique (scan_tool_id);
alter table server add constraint UK_tlm37i24shgf65y6asv2e4k75 unique (serv_hostname);
alter table users add constraint UK_33uo7vet9c79ydfuwg1w848f unique (user_email);
alter table website add constraint UK_e638k719r8wgh0skkspjgjyvv unique (webs_code);
alter table website add constraint UK_c2b22pfepca1jxdaw0n1l3snv unique (webs_qualys_webapp_id);
alter table accessright add constraint FKbupagttgvulmgallch66pi62k foreign key (proj_id) references project (proj_id);
alter table accessright add constraint FKh2k5o90w974ugyn8twx8xu06w foreign key (role_id) references roles (role_id);
alter table accessright add constraint FK5y7oh4pit6qnv0b6xth1nxu2b foreign key (user_id) references users (user_id);
alter table deployment add constraint FKpd8r8h3tlkxhwu70m0rcxyrsx foreign key (dest_id) references deploymentstatus (dest_id);
alter table deployment add constraint FKcucu9c4dlqvweju82nb1x0pog foreign key (dety_id) references deploymenttype (dety_id);
alter table deployment add constraint FK35wolb852aui16ie83ib29ips foreign key (proj_id) references project (proj_id);
alter table deploymentcommand add constraint FKbc1us1xtue7006ryuc23kpat6 foreign key (depl_id) references deployment (depl_id);
alter table deploymentcommand add constraint FKfm5kicuq1oxnvteuwnt5ide0k foreign key (deps_id) references deploymentstep (deps_id);
alter table docroot add constraint FK51uawjlddm3o5a0qrhnyypagx foreign key (hopr_id) references hostingprovider (hopr_id);
alter table docrootenvcertif add constraint FKe6rhojwxts1m23iebv2ld4wd3 foreign key (fk_cert_id) references certificate (cert_id);
alter table docrootenvcertif add constraint FK6h2jmse6o0x42ettb36st3a4q foreign key (fk_doen_id) references docrootenvironment (doen_id);
alter table docrootenvdomain add constraint FKquoh0f3cbjx0776xnjlqrf1nu foreign key (fk_doma_id) references domain (doma_id);
alter table docrootenvdomain add constraint FKefiin6aahi99qvu4c7lmhnafl foreign key (fk_doen_id) references docrootenvironment (doen_id);
alter table docrootenvironment add constraint FKghi3vhretkv99jwwt6uew4pcp foreign key (cms_id) references cms (cms_id);
alter table docrootenvironment add constraint FK4r23rftmur6rcn53nyo06y73h foreign key (docr_id) references docroot (docr_id);
alter table docrootenvironment add constraint FK471lay4dmlt7dxey8ba5m3d47 foreign key (ddc_id) references drupaldocrootcore (ddc_id);
alter table docrootenvironment add constraint FKf3095fvqexrf919orgg9v2xd8 foreign key (envi_id) references environment (envi_id);
alter table docrootenvloadblancer add constraint FKhlilk8o4omwcyxeqs3bug7p5j foreign key (fk_loba_id) references loadbalancer (loba_id);
alter table docrootenvloadblancer add constraint FK3fpi0omd6vr6ot1h0cedrgwp5 foreign key (fk_doen_id) references docrootenvironment (doen_id);
alter table docrootenvservers add constraint FKp03u6j2dljpgpo6od0292cha4 foreign key (fk_serv_id) references server (serv_id);
alter table docrootenvservers add constraint FKq2dsb368ni6h64t11w3mmwrj0 foreign key (fk_doen_id) references docrootenvironment (doen_id);
alter table domain add constraint FKkj0ntwyubwvi5tuhbdutb063a foreign key (regi_id) references registar (regi_id);
alter table domainloadbalancer add constraint FKl21gf8vxxpnnqkualg8be49yy foreign key (doma_id) references domain (doma_id);
alter table domainloadbalancer add constraint FK92d0nnvsg0w6jw11349jv1tih foreign key (loba_id) references loadbalancer (loba_id);
alter table domainloadbalancer add constraint FK5gxjg6eckty46u6avuoal4tel foreign key (webs_id) references website (webs_id);
alter table loadbalancer add constraint FKj7giymvqrj20ma9fg255s81fs foreign key (hopr_id) references hostingprovider (hopr_id);
alter table project add constraint FKtg7j1hlljk7hpup3tpyb7o3ur foreign key (drupaldocrootcore_ddc_id) references drupaldocrootcore (ddc_id);
alter table project add constraint FKr6eprpe0u0gnr3m94kq4sbyfh foreign key (website_webs_id) references website (webs_id);
alter table report add constraint FKoc55xe795hyvrgsgfvbeqf1y1 foreign key (scan_scan_id) references scan (scan_id);
alter table scan add constraint FKbgod6b1serfh7o6re2xc9tkdu foreign key (proj_id) references project (proj_id);
alter table scan add constraint FKgt31397gpqsrhowfk0nmwhyx1 foreign key (scst_id) references scanstatus (scst_id);
alter table scan add constraint FKh4g287s0lot4yauf2spdgbu5b foreign key (scty_id) references scantype (scty_id);
alter table websitesdeployed add constraint FKmkmao4gd5dwxw4h6j4agj8g6t foreign key (doen_id) references docrootenvironment (doen_id);
alter table websitesdeployed add constraint FKtoh5im3qnmiu1rgf7n6gxwub7 foreign key (webs_id) references website (webs_id);

create view CAS_VIEW as 
SELECT 'accessRights' as attrname, (SELECT concat( 
                                        (CASE WHEN p.drupaldocrootcore_ddc_id IS NOT NULL 
                                            THEN ( concat('ddc', ':', ddc.ddc_code)) 
                                            ELSE ( concat('w', ':', w.webs_code))
                                       END), ':', r.role_label)) AS attrvalue,
                u.user_email FROM accessright AS a 
                LEFT JOIN roles AS r ON a.role_id = r.role_id
                LEFT JOIN project AS p ON a.proj_id = p.proj_id 
                LEFT JOIN drupaldocrootcore AS ddc ON ddc.ddc_id = p.drupaldocrootcore_ddc_id
                LEFT JOIN website AS w ON w.WEBS_ID = p.website_webs_id
                LEFT JOIN users as u ON a.user_id = u.user_id
UNION
SELECT 'accessRights' as attrname, (SELECT concat('*', ':' ,'*', ':', 'ADMIN'))
  as attrvalue, u.user_email  FROM  users u where u.user_isadmin = 1;

INSERT INTO users
(`user_id`,
`user_company`,
`user_email`,
`user_firstname`,
`user_isadmin`,
`user_lastname`)
VALUES
(1,
'Saint-Gobain',
'wsip-admin@saint-gobain.com',
'admin',
1,
'admin');

INSERT INTO roles
(`role_id`,
`role_label`)
VALUES
(1,'Admin'),
(2,'LocalIT'),
(3,'External'),
(4,'Owner'),
(5,'Business');

INSERT INTO deploymentstatus
(`dest_id`,
`dest_enable`,
`dest_label`)
VALUES
('REQUESTED',
1,
'REQUESTED'),
('IN_PROGRESS',
1,
'IN_PROGRESS'),
('ACCEPTED',
1,
'ACCEPTED'),
('SUCCEEDED',
1,
'SUCCEEDED'),
('FAILED',
1,
'FAILED'),
('ABORTED',
1,
'ABORTED');

INSERT INTO deploymenttype
(`dety_id`,
`dety_enable`,
`dety_label`)
VALUES
('D_DOCROOTCORE',
1,
'D_DOCROOTCORE'),
('D_WEBSITE',
1,
'D_WEBSITE');

INSERT INTO scanstatus
(`scst_id`)
VALUES
('SUBMITTED'),
('RUNNING'),
('FINISHED'),
('TIME_LIMIT_EXCEEDED'),
('SCAN_NOT_LAUNCHED'),
('SCANNER_NOT_AVAILABLE'),
('ERROR'),
('CANCELED');

INSERT INTO scantype
(`scty_id`)
VALUES
('QUALYS');

