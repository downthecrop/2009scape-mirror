[![AGPL-3.0 License][license-shield]][license-url]

<br />
<p align="center">
  <a href="https://github.com/woahscam/2009Scape">
    <img src="https://i.imgur.com/RsfVfkB.png" alt="Logo" width="300" height="67">
  </a>
  <h3 align="center">An open source MMORPG emulation server</h3>

  <h1 align="center"><a href="https://2009scape.org/"><strong>Play the live server »</strong></a></h1>
  <p align="center">
    <br />
    <br />
    <a href="https://2009scape.org/">Community Hosted Server</a>
    ·
    <a href="https://discord.gg/43YPGND">Discord Invite</a>
    ·    <a href="https://gitlab.com/2009scape/2009scape/-/issues">Report Bug</a>
  </p>
<h3>NOTE: Bug reports and support are only accepted by/offered to players of our live hosted server. We will not provide support to those running their own copies.</h3>
</p>


## Table of Contents

* [Live Server Information](#live-server-information)
* [History of this Codebase](#history-of-this-codebase)
* [Our Core Values](#our-core-values) 
* [Contributing](#contributing)
* [Setup for Content Developers](#content-developers-setting-up-the-project)
  * [Github Setup](#github-setup)
  * [Prerequisites](#prerequisites)
  * [Prereq Installation](#prereq-installation)
  * [Project Setup](#project-setup)
  * [Running the project](#running-the-project)
* [License](#license)
* [Contact](#contact)


## Live Server Information

Did you know that the 2009Scape Development Team hosts the main branch of this repo that you can connect to and play? Come join a growing community of people that love to grind out skills, quest, and hangout together. A download link for the launcher can be found <a href="https://2009scape.org"><strong>here.</strong></a> Connecting to the live server is also one of the easiest ways to identify bugs/typos/missing features. Identifying these issues help developers, whether already on the project, or are even brand new to the project, fix bugs and issues in an expedited rate.

## History of this Codebase

The 2009Scape you see today has gone through a magnitude of changes. Originally starting its life as Arios498, this server saw a lot of people playing it daily, unfortunately, it was for profit and closed source. It was later upgraded to Arios530, targeting the build 530 of runescape with content in and around January 1st, 2009. Development came to a halt when a developer of the closed source project released the source code. The original developers of this server went on to create Kratos 530 back in 2015.

This project was started out of love for the 530 revision. A small group of developers spent thousands of hours improving on the existing source that was left to the curb. Over the past year, this project has seen many developers coming and going, fixing bugs that they find either through their own server, or bugs that they find in the live game that is currently hosted. We do not accept donations of any kind. The smiles and wonderful compliments are more than enough to keep us going! Content and bugfixes are always number one on our list, and we try our best to answer any questions that you may have, provided you have read through this readme and frequently asked questions on the discord.

## Our Core Values

In the current climate of RSPS in general, we believe it's important to wear our core values on our sleeves and defend them with everything we have! Below are what we hold as our core, most important values:

* **We do NOT believe in profiting off RSPS.** To many of us here at the 2009scape team, what we care about most is preserving the wonderful work of the Gower brothers in the most true-to-spirit manner possible. We do NOT do this to make a profit, and in fact **we outright refuse donations.** This is a labor of love and passion for everyone involved, a love for real RS2! This is perhaps the single most central value we have. If you want to "donate" to us, do so with your time and your dedication. That is what we desire most.
  

* **Authenticity is central to the work**. As a remake, one of the most important things to us is being true to the Gower spirit. What the Gowers brought to us in our childhood is what we are driven to preserve for the remainder of the world. 


* **Open Source Is Crucial**. We believe open source remakes to be crucial to preserving what we loved in our childhood, and we believe for-profit and/or closed-source servers are destined to flounder out and fail. 


* **Be Welcoming**. One of our most important goals is to provide a community of friendly and caring people that get along and love to enjoy the game with eachother. For this reason, we do tend to be very strict when it comes to toxicity. We care about quality a whole lot more than quantity! 

## Contributing

<h4>Note: All merge requests MUST be made using the defaut MR template you can select from the dropdown when creating a new merge request. Merge requests that do not use this template will not be accepted. </h4>
<h4>More information on Kotlin can be found <a href="https://kotlinlang.org/">here.</a></h4>

There are many ways everyone can contribute! From the most seasoned programmers to those who do not have the most remote clue how code works! Below are some things that can always use some love from the community.

* **Wiki Editors**: Did you know we have a wiki? Well it's always in need of people to fill it out and stay on top of it. Editing the wiki is one of the easiest ways you can contribute to 2009scape! If you're an active player and have the will, there's so much you could be helping out with over at the wiki. <a href="http://play.2009scape.org/wiki">Click here to go to the wiki.</a>


* **JSON editors**: Did you know that the vast majority of our drop tables, stats, examines, item info, npc info, etc is all stored in a very easy-to-modify format called JSON? This is something almost anyone can help with, especially with the tool made by Ceikry to make it simple and streamlined. If you want to know what can be done to help here, get in touch in the development channel in the discord! **JSON editors are always in need and always appreciated!!**.


* **Auditors**: As a remake, authenticity is central to our core values! We could always use someone to go through the game and create large lists of simple tasks that can be done to bring us closer to the authentic 2009 game! The preferred way to do this is one-area-at-a-time. If you want to see an example of some audits we've done in the past, take a look <a href="https://gitlab.com/2009scape/2009scape/-/issues/46">here.</a>


* **Content Developers**: As a remake, we have massive amounts of content that need to be implemented or corrected. If you know how to program or are willing to learn, this is where you could be extremely helpful! We need everything from quests to dialogue to minigames to skills that still need to be corrected or implemented, and this is perhaps one of the most valuable ways someone could help out the project! If you are interested in developing content, reach out in the development channel of the Discord.

## Content Developers: Setting Up the Project.
### Gitlab Setup

<h4>Note: This allows you to commit changes to the main repo (with approval)! Also, always stay up to date with the most recent updates by pulling into your copy when 2009Scape updates!</h4>

1. Create a gitlab account if you haven't done so already

3. Click "Fork" in the top right hand side of our gitlab page.

**If at anytime you have an issue with gitlab please refer to the** <a href="https://gitlab.com/help">Gitlab help center.</a>

### Git LFS Setup

To obtain large binary files for the repo such as the cache, make sure you have git lfs installed: https://git-lfs.github.com/

After setting up git lfs, you may need to run `git lfs pull` in the root of the cloned repo to download essential binaries.

### Prerequisites
* Java SE Development Kit Version 11/OpenJDK 11 (preferred)
* Xampp

## Prereq Installation

<h3>Windows</h3>
<details>
<summary> </summary>

1. Install <a href="https://www.oracle.com/java/technologies/downloads/#java11-windows">JDK version 11</a>
    * Scroll down until you see Windows x86 and Windows x64
    * If you are running a 64bit verison of Windows (standard), select Windows x64
    * Accept the terms and conditions, after reading them of course, and login to oracle
    * Download and install like any normal application
    
2. Install <a href="https://www.apachefriends.org/index.html">Xampp</a>
    * Click on "Xampp for Windows"
    * Download
    * Install as Administrator
</details>  
    
<h3>MacOS</h3>
<details>
<summary> </summary>

1. Install <a href="https://www.oracle.com/java/technologies/downloads/#java11-mac">JDK version 11</a>
    * Scroll down until you see macOS x64
    * select macOS x64
    * Accept the terms and conditions, after reading them of course, and login to oracle
    * Download and install like any normal mac application
    
2. Install <a href="https://www.apachefriends.org/index.html">Xampp</a>
    * Click on "Xampp for OS X"
    * Download
    * Install as Administrator
</details>

<h3>Linux</h3>
<details>
<summary> </summary>

1. Install JDK version 11 through <a href="https://www.oracle.com/java/technologies/downloads/#java11-linux">Oracle</a> or through <a href="https://openjdk.java.net/install/">command line</a>
    * Debian, Ubuntu, etc.
    ```sh
    sudo apt install openjdk-11-jdk
    ```
    * Fedora, Oracle Linux, Red Hat Enterprise Linux, etc.
    ```sh
    su -c "yum install java-11-openjdk"
    ```
    
2. Install <a href="https://www.apachefriends.org/index.html">Xampp</a> OR Set up DB server of your choice.
    * Click install "XAMPP for Linux"
    * Install like any normal Linux program
</details>


### Project Setup

- Open Xampp (Skip if you roll your own database and know what you're doing.)
    * On Windows make sure you run Xampp as administrator
    * On the left-hand side make sure you tick the two "Service" boxes for Apache and MySQL
    * For both Apache and MySQL click "Start" under Actions
    * After doing that navigate to the <a href="https://localhost/phpmyadmin/">PHP My Admin LOCAL SITE</a>
    * Once opened, on the left-hand side click the three "disks" that say "New"
    * In the "Database name" bar type: global
    * Press the "Create" button
    * A three disk "global" should appear on the left hand side
    * Click it and on the top bar select "Import"
    * Under **FILE TO IMPORT** click "Browse...." 
    * Navigate to your 2009Scape project folder
    * Go to Server/db_exports/ and import global.sql
    * It may take a moment to import, when It is done importing Xampp is all set up!
    
### Running the project
***Note: If you choose not to use the provided run scripts, you *must* run `mvn clean` before it will build correctly.***

#### Linux / OSX
1. Make sure your database of choice is running (see above)
2. Start the Management Server with run-ms.sh
3. Start the game server with run-server.sh

#### Windows
1. Make sure your database is running (see above)
2. Start the Management Server with run-ms.bat
3. Start the game server with run-server.bat

### IDE Integration: IntelliJ IDEA (Recommended)
1. Click the "Project" tab on the left hand side.
2. In the top left where the "Project" drop down is, click Project Files
3. Expand the Management-Server folder
4. Right click pom.xml and click "Add Maven Project"
5. Expand the Server folder
6. Right click pom.xml and click "Add Maven Project"
7. In IntelliJ, File -> Invalidate Caches -> Invalidate Caches and Restart
8. You should now have ready-made run configurations in the top right: [Windows] Run MS, [Windows] Run Server, [Linux] Run MS, [Linux] Run Server
9. Go ahead and use the Run Server configuration for your OS to make sure everything worked.

### IDE Integration: Eclipse
Note: This section could use improvement. If you have a better way to integrate the project with eclipse, feel free to open an MR!
1. Make sure you have the [Eclipse Maven Plugin](https://stackoverflow.com/a/25993960/1971003) installed.
2. In Eclipse, click File -> Import
3. Type Maven in the search box
4. Under "Select an Import Source," select "Existing Maven Project."
5. Next
6. Click browse, and select the Management-Server folder.
7. Repeat steps 2-6 for the Server folder.

### IDE Integration: Netbeans
Note: This section could use improvement. If you have a better way to integrate the project with netbeans, feel free to open an MR!

1. Select File -> Open Project
2. Select the Management-Server folder to open the MS project.
3. Select the Server folder to open the Server project.

### License

We use the AGPL 3.0 license, which can be found <a href="https://www.gnu.org/licenses/agpl-3.0.en.html">here.</a> Please read and understand the license. Failure to follow the guidelines outlined in the license will result in legal action. If you know or hear of anyone breaking this license, please send a report, with proof, to Red Bracket#8151, ceikry#2724, or woahscam#8535 on discord or email woahscam@hotmail.com. **We WILL NOT change the license to fit your needs.**

### Contact

<h4>Reminder: There is no official support for setting up your own server. Do not ping/dm developers about setting up your server. Community support is available in the discord server.</h4>


[license-shield]: https://img.shields.io/badge/license-AGPL--3.0-informational
[license-url]: https://www.gnu.org/licenses/agpl-3.0.en.html
