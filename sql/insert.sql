-- Script inserts some starting data for the app

-- By default the password for the 'admin' account is admin;
-- the starting salt for the admin account is "00000000000000000000000000000000"
-- which is obviously stored as a hexstring
INSERT INTO account (username, salt, password_and_salt_hash)
        values ('admin', '3030303030303030303030303030303030303030303030303030303030303030',
        '893C750ABE47D04EA480910A2BC8787D1323CD11D4E6EAA3A6C890DBEB42BFA0');
INSERT INTO admin_account (username) values ('admin');
