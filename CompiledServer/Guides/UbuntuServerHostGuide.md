Ubuntu 18.04 LTS:

1. Get SQL: https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-18-04
2. Remove the password: https://stackoverflow.com/questions/44890331/how-do-i-setup-mysql-with-a-blank-empty-password-on-ubuntu-16-04
3. Install Java: sudo apt install default-jre
4. Create global & server database:
* Login:   mysql -u root -p
* Create:
*  CREATE DATABASE server;
*  CREATE DATABASE global;
5. Import global & server databases:
* mysql -u root -p server < server.sql
* mysql -u root -p global < global.sql
