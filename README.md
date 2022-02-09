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
* [Video Setup Tutorial](#video-setup-tutorial)
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

<h4>Note: It is required for a developer submitting a PR to use Intellij IDEA as your integrated development environment.</h4>

* Intellij IDEA
* Java SE Development Kit Version 1.8/OpenJDK 1.8 (preferred)
* Gradle (optional - Installs with Intellij on project build)
* Xampp

## Prereq Installation

<h3>Windows</h3>
<details>
<summary> </summary>

1. Install <a href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">JDK version 1.8</a>
    * Scroll down until you see Windows x86 and Windows x64
    * If you are running a 64bit verison of Windows (standard), select Windows x64
    * Accept the terms and conditions, after reading them of course, and login to oracle
    * Download and install like any normal application
    
2. Install <a href="https://www.jetbrains.com/idea/">Intellij IDEA</a>
    * Click "Download" on the main page
    * Ensure "Windows" is selected at the top
    * Select "Community download"
    * Download and install like any normal application
    
3. Install <a href="https://gradle.org/">Gradle</a>
    * Click on "Install Gradle" 
    * Scroll down to "Installing manually" 
    * Download "Binary-only"
    * Scroll down until you see "Microsoft Windows users"
    * Follow the instructions listed on the website
    
4. Install <a href="https://www.apachefriends.org/index.html">Xampp</a>
    * Click on "Xampp for Windows"
    * Download
    * Install as Administrator
</details>  
    
<h3>MacOS</h3>
<details>
<summary> </summary>

1. Install <a href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">JDK version 1.8</a>
    * Scroll down until you see macOS x64
    * select macOS x64
    * Accept the terms and conditions, after reading them of course, and login to oracle
    * Download and install like any normal mac application

2. Install <a href="https://www.jetbrains.com/idea/">Intellij IDEA</a>
    * Click "Download" on the main page
    * Select "Mac" towards the top of the main page
    * Select "Community download"
    * Download and install like any normal application
    
3. Install <a href="https://gradle.org/">Gradle</a>
    * Click on "Install Gradle" 
    * Scroll down to "Installing manually" 
    * Download "Binary-only"
    * Scroll down until you see "Linux & MacOS users"
    * Follow the instructions listed on the website
    
4. Install <a href="https://www.apachefriends.org/index.html">Xampp</a>
    * Click on "Xampp for OS X"
    * Download
    * Install as Administrator
</details>

<h3>Linux</h3>
<details>
<summary> </summary>

1. Install JDK version 1.8 through <a href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">Oracle</a> or through <a href="https://openjdk.java.net/install/">command line</a>
    * Debian, Ubuntu, etc.
    ```sh
    sudo apt install openjdk-8-jdk
    ```
    * Fedora, Oracle Linux, Red Hat Enterprise Linux, etc.
    ```sh
    su -c "yum install java-1.8.0-openjdk"
    ```  
  
2. Install Intellij IDEA through <a href="https://www.jetbrains.com/idea/">Intellij Website</a> or through <a href="https://www.jetbrains.com/help/idea/installation-guide.html#snap">Command Line</a>
    * Debian, Ubuntu, etc.
    ```sh
    sudo snap install intellij-idea-community --classic
    ```

3. Install Gradle through the <a href="https://gradle.org/">Gradle Website</a> or through <a href="https://gradle.org/">Command Line</a>
    * Click install Gradle
    * If you're installing through Command Line follow : Installing with a package manager
    * If you're installing through Download follow : Installing manually : Linux & MacOS users
    ```sh
    sdk install gradle 6.6.1
    ```
    
4. Install <a href="https://www.apachefriends.org/index.html">Xampp</a>
    * Click install "XAMPP for Linux"
    * Install like any normal Linux program
</details>


### Project Setup

1. Open Xampp
    * On Windows make sure you run Xampp as administrator
    * On the left hand side make sure you tick the two "Service" boxes for Apache and MySQL
    * For both Apache and MySQL click "Start" under Actions
    * After doing that navigate to the <a href="https://localhost/phpmyadmin/">PHP My Admin LOCAL SITE</a>
    * Once opened, on the left hand side click the three "disks" that say "New"
    * In the "Database name" bar type: global
    * Press the "Create" button
    * A three disk "global" should appear on the left hand side
    * Click it and on the top bar select "Import"
    * Under **FILE TO IMPORT** click "Browse...." 
    * Navigate to your 2009Scape project folder
    * Go to Server/db_exports/ and import global.sql
    * It may take a moment to import, when It is done importing Xampp is all set up!

2. Open Intellij
    * Select Get from Version Control
    * Click on "Github", and you will be asked to login
    * Change your directory to wherever you want the project to load, or keep it default
    * On the right hand side you should see your Github Repository for 2009Scape
    * Select it and hit "Clone"
    * The project should instantly start building on import. Give it some time because it is going through and compiling alot of files
    * In Intellij go to File -> Project Structure -> Project and verify your Project SDK is set to JDK "1.8"
    * Setup through Intellij should now be finished!
    
### Running the project

1. Navigate to the right hand side of Intellij where it says "Gradle"
    * Gradle is very useful when it comes to running and compiling the project
    * The only tabs we are concerned about are "Management-Server" and "Server"
    * Each of these have a "Tasks" folder and an "application" folder inside of "Tasks"
    <br>
2. Click on the "application" folder for the Management-Server and double-click "run"
    * The management server is used for things such as player data(not saves), highscores, and world information
    <br>
3. Click on the "application" folder for the Server and double-click "run"
    * Please note this may take a minute or two to build, it is compiling a lot of files!
    * If you receive an error on server start, check and make sure that your worldprops/default.conf is pointing to the correct paths
    * * Server debug mode/other information can be changed in worldprops/default.conf
    <br>
4. Clone the <a href="https://gitlab.com/2009scape/legacy-client">legacy-client</a> repo and follow the same Intellij setup steps
    * Click on the "application" folder for the Client and double-click "run"
    * If you receive an error on client run open your build.gradle to verify that the mainClassName is set to the correct location
    * Due to client work that is on going a lot of files get changed and new paths for the main class are created
    * **FIX** In your build.gradle change the mainClassName and Main-Class attributes to 'org.runite.jagex.Client'
    * If Gradle run still does not work, launch the Client by navigating to Client/src/main/java/org.runite/jagex/Client
    * Right click on the client and select Run 'Client.main()'
    * Client debugging options can be found inside of config.json

### License

We use the AGPL 3.0 license, which can be found <a href="https://www.gnu.org/licenses/agpl-3.0.en.html">here.</a> Please read and understand the license. Failure to follow the guidelines outlined in the license will result in legal action. If you know or hear of anyone breaking this license, please send a report, with proof, to Red Bracket#8151, ceikry#2724, or woahscam#8535 on discord or email woahscam@hotmail.com. **We WILL NOT change the license to fit your needs.**

### Contact

<h4>Reminder: There is no official support for setting up your own server. Do not ping/dm developers about setting up your server. Community support is available in the discord server.</h4>

### Video Setup Tutorial
Occexe made a nice video showing you how to set it up if you'd prefer that. Please note many things have changed since this was filmed and the steps are no longer strictly accurate. Check it out here:

[![View Tutorial](http://img.youtube.com/vi/3oQcsZdrRcE/0.jpg)](http://www.youtube.com/watch?v=3oQcsZdrRcE "2009Scape Setup")



[license-shield]: https://img.shields.io/badge/license-AGPL--3.0-informational
[license-url]: https://www.gnu.org/licenses/agpl-3.0.en.html
