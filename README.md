# CloudPhotosApp
This is a simple web application, in which users can store photos in web folders sharing them with each other.

Basically, I've made the app just to study Java Spring MVC and Spring Security and Mockito tests (Junit 4). So I think everyone can use this code for any purposes. 

If the code really helped you, please leave any kind of citation linking to this github repository named CloudPhotosApp and so far written solely by Vladislav D. Ustinov.

Technologies used:
View is written in html together with Thymeleaf and its Bootstrap library (I also used just a bit of java script to make "upload files" button look better);
Spring MVC is used to write the server;
Spring Security is chosen to authorize users having roles USER or ADMIN;
Mockito with JUnit 4.0;
Mysql connection or h2 in-memory database are available at your choice.

In order to use h2 just uncomment onApplicationEvent method in BootStrap class and also comment connection in application.properties.

At /index, user is able to register account and login
At /photos user is able to:
upload files (maximum size is set in application.properties file);
create new folders as well as navigate through their subfolders;
select photos and folders in order to copy-paste or delete them;
rename photos and folders;
search photos and folders;
share folders with other users (if the folder is shared and owners want to delete it, they all should delete it separately);
in "myaccount" button - sign out, delete account, change password.

Also /test view is made just to test some technologies like rest requests, post requests with @ModelAttribute, etc.

Best regards,
Vladislav D. Ustinov,
Ph.D. from Lomonosov Moscow State University
e-mail: vladustinov90@gmail.com





