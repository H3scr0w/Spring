# Persistence Layer rules

## Rule 1: Technical and functional primary key

All Tables are managed by a technical primary key (naming convention: tablename + Id). This Id must be keep secret and never be communicated through API.
Beside, a functional primary key should exist. (naming convention: tablename + Code). This is must be communicated through API. a functionnal primary key is unique (do not forget to set ip on persistence layer).
 
## Rule 2: Deletion (enable attribute)

Only relation could be deleted (for example: websitedeployed, docrootenvironment). For all others cases, table must have a enable attribute for logical delete.

## Rule 3: created and lastupdate attributes

created attributes contain the creation date of the entry in table.
lastupdate attribute contrain the date of last update of the entry in the table.

