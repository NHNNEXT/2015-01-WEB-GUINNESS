/* DB 생성 */

Create DATABASE GUINNESS DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

/* User 생성 및 DB권한 주기 */
Create User link413@'localhost' identified by 'link413';

Grant all privileges on GUINNESS.* to link413@'localhost' identified by 'link413';
