# RS 2009
Open source 2009 Runescape Remake


# Setting up the project
Requirements:
- General knowledge of git
- General knowledge of SQL
- Intellij

Be sure to check the debugging section when something goes wrong.

At any point, if you need help, you are free to join our Discord server at: https://discord.gg/4zrA2Wy

## Step 1:
- Fork this repository to your repo

## Step 2: Setting up IntelliJ
- Open IntelliJ
- Click `File` > `New` > `Project from Version Control`
  - Paste the URL of your forked repository in the URL field, click Done
  - Wait for the repository to clone
- Click `File` > `Project Structure` > `Modules` > `09HDscape` > `Paths` > `Inherit project compile output path`
  - Hit `OK`
- Click `File` > `Project Structure` > `Project` > Change the Project Compiler Output to the folder where the project exists on your computer
  
## Step 3: Setting up the Database
Since this portion of the guide is operating-system specific, you will either need basic database knowledge or a bit of help. Below are the things that need to be configured.
### General:
- Configure root@localhost to have the password "password123"
- Create databases in root named `server` and another named `global`
- Import `Server/server.sql` and `Server/global.sql` into their respective databases
- Start the database

### Windows:
- Download and install [xampp](https://www.apachefriends.org/download.html)
- IMPORTANT TODO: Make the Windows guide include adding the password "password123"
- Start the `Apache` and `MySQL` modules
- Navigate to http://localhost/phpmyadmin/
- Create 2 new tables named `server` and `global`
- Import `Server/server.sql` and `Server/global.sql` into their respective databases
  - _Refer [here](https://www.thecodedeveloper.com/import-large-sql-files-xampp/) for help importing the `.sql` files_

## Step 4: Running the Server & Client
- Run the management-server
  - In IntelliJ, navigate to `09HDscape-management-server/src/org/keldagrim/Main.java`, right click > Run
- Run the server
  - In IntelliJ, navigate to `09HDscape-server/src/org/crandor/Main.java`, right click > Run
    - It will fail
  - Top right there should be `Main (1)` in a dropdown, click it then select `Edit Configurations`
  - Change the working directory to `09HDscape-server` so it can find `server.properties`
- Run the client
  - In IntelliJ, navigate to `09HDscape-client/src/org/runite/GameLaunch.java`, right click > Run
  
You should now be set up!

## Debugging

Errors could not find library:
- Click `File` > `Project Structure` > `Modules` > `09HDscape` > `Paths` > `Inherit project compile output path`
  - Now click `Dependencies`
  - Click the `+` on the right-hand side > `JARs or directories`
  - Add the following things:
    - `09HDscape-client/lib` directory
    - `09HDscape-client/clientlibs.jar` file
    - `09HDscape-management-server/lib` director 
