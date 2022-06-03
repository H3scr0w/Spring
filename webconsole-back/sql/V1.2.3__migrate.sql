SET autocommit = 0;
DELIMITER $$
 
CREATE PROCEDURE `migrate`()

BEGIN
     DECLARE `_rollback` BOOL DEFAULT 0;
     DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET `_rollback` = 1;
     START TRANSACTION;
     
     -- Users
      alter table users add column user_isactive bit;
      
     -- Website
      alter table website add column webs_islive bit default 0;
      
     -- Roles
     insert into roles (`role_label`) values ('WASO'), ('Application_Contact'), ('Technical_Contact');
     
      -- Docrootenvironment
     alter table docrootenvironment add column doen_acquia_environment_id varchar(255);
     
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