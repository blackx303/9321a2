-- Script inserts some starting data for the app

-- By default the password for the 'admin' account is admin;
-- the starting salt for the admin account is "00000000000000000000000000000000"
-- which is obviously stored as a hexstring
INSERT INTO account (username, salt, password_and_salt_hash)
        values ('admin', '3030303030303030303030303030303030303030303030303030303030303030',
        '893c750abe47d04ea480910a2bc8787d1323cd11d4e6eaa3a6c890dbeb42bfa0');
INSERT INTO admin_account (username) values ('admin');
