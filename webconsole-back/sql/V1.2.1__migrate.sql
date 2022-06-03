SET autocommit = 0;
DELIMITER $$
 
CREATE PROCEDURE `migrate`()

BEGIN
     DECLARE `_rollback` BOOL DEFAULT 0;
     DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET `_rollback` = 1;
     START TRANSACTION;
     
     -- DomainLoadbalancer
     drop table domainloadbalancer;
     
     -- Domain Type
     create table domaintype (doty_id varchar(255) not null, primary key (doty_id)) engine=InnoDB;
     insert into domaintype (`doty_id`) values ('MAIN'),('REDIRECTION'),('CONTRIBUTION');
     
     -- Domain
     alter table domain add column doma_qualys_webapp_id bigint, add column doma_qualys_webauth_id varchar(255), add column doma_is_monitor_enable bit, add column doma_monitor_keyword varchar(255), add column doty_id varchar(255) not null, add column parent_id bigint, add column wede_id bigint not null;
     alter table domain add constraint UK_ko81i082x3jwceww6cfgg6m5f unique (doma_qualys_webapp_id);
     alter table domain add constraint FK3ct7nwiju032vunvfy6xo7abg foreign key (doty_id) references domaintype (doty_id);
     alter table domain add constraint FKq6505qui3fp8004ccp9mh64jo foreign key (wede_id) references websitesdeployed (wede_id);
     alter table domain add constraint FKad0yta8wenpkrlp55xmrfdac8 foreign key (parent_id) references domain (doma_id);
     
     -- Docroot
     alter table docroot add column docr_is_mutualized bit, add column docr_provider_internal_id varchar(255);
     alter table docroot add constraint UK_1r4txati86cryvg5io6wp1w91 unique (docr_provider_internal_id);
     
     -- Docrootenvironment
     alter table docrootenvironment add column doen_is_qualys_enable bit, add column doen_qualys_webapp_id bigint, add column doen_qualys_webauth_id varchar(255), add column doen_provider_internal_id varchar(255);
     alter table docrootenvironment add constraint UK_ej79bonj9g3iaqf22bnt10ypb unique (doen_qualys_webapp_id);
     alter table docrootenvironment add constraint UK_7jdlmw79desblewi3v738ov1j unique (doen_provider_internal_id);
     
     -- Website
      alter table website add column webs_is_multi_main_domain bit, add column webs_is_to_be_disabled bit;
     
     IF `_rollback` THEN
         ROLLBACK;
     ELSE
         COMMIT;
     END IF;
END$$

CALL migrate()$$

DROP PROCEDURE `migrate`$$

DELIMITER ;
SET autocommit = 1;