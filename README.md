[![AGPL-3.0 License][license-shield]][license-url]

<br />
<p align="center">
  <a href="https://gitlab.com/2009scape/2009scape">
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

## Table of Contents

* [Live Server Information](#live-server-information)
* [History of this Codebase](#history-of-this-codebase)
* [Our Core Values](#our-core-values) 
* [Contributing](#contributing)
* [Setup for Content Developers](#content-developers-setting-up-the-project)
  * [GitLab Setup](#gitlab-setup)
  * [Prerequisites](#prerequisites)
  * [Project Setup](#project-setup)
  * [Running the project](#running-the-project)
* [License](#license)
* [Contact](#contact)


## Live Server Information

Did you know that the 2009Scape Development Team hosts the main branch of this repo that you can connect to and play? Come join a growing community of people that love to grind out skills, quest, and hangout together. A download link for the launcher can be found [here](https://2009scape.org). Connecting to the live server is also one of the easiest ways to identify bugs/typos/missing features. Identifying these issues help developers, whether already on the project, or are even brand new to the project, fix bugs and issues in an expedited rate.

## History of this Codebase

The 2009Scape you see today has gone through a magnitude of changes. Originally starting its life as Arios498, this server saw a lot of people playing it daily, unfortunately, it was for profit and closed source. It was later upgraded to Arios530, targeting the build 530 of runescape with content in and around January 1st, 2009. Development came to a halt when a developer of the closed source project released the source code. The original developers of this server went on to create Kratos 530 back in 2015.

This project was started out of love for the 530 revision. A small group of developers spent thousands of hours improving on the existing source that was left to the curb. Over the past year, this project has seen many developers coming and going, fixing bugs that they find either through their own server, or bugs that they find in the live game that is currently hosted. We do not accept donations of any kind. The smiles and wonderful compliments are more than enough to keep us going! Content and bugfixes are always number one on our list, and we try our best to answer any questions that you may have, provided you have read through this readme and frequently asked questions on the discord.

## Our Core Values

In the current climate of RuneScape Private Servers in general, we believe it's important to wear our core values on our sleeves and defend them with everything we have! Below are what we hold as our core, most important values:

* **We do NOT believe in profiting off an RSPS.** To many of us here at the 2009Scape team, what we care about most is preserving the wonderful work of the Gower brothers in the most true-to-spirit manner possible. We do NOT do this to make a profit, and in fact **we outright refuse donations.** This is a labor of love and passion for everyone involved, a love for real RS2! This is perhaps the single most central value we have. If you want to "donate" to us, do so with your time and your dedication. That is what we desire most.
  

* **Authenticity is central to the work**. As a remake, one of the most important things to us is being true to the Gower spirit. What the Gowers brought to us in our childhood is what we are driven to preserve for the remainder of the world. 


* **Open Source is crucial to the project**. We believe open source remakes to be crucial to preserving what we loved in our childhood, and we believe for-profit and/or closed-source servers are destined to flounder out and fail. 


* **Be welcoming**. One of our most important goals is to provide a community of friendly and caring people that get along and love to enjoy the game with eachother. For this reason, we do tend to be very strict when it comes to toxicity. We care about quality a whole lot more than quantity! 

## Contributing

**Note: All merge requests MUST be made using the defaut MR template. Merge requests that do not use this template will not be accepted.**

**Note: All new contributions MUST be made in Kotlin unless you are updating or fixing a bug in legacy code. More information on Kotlin can be found [here](https://kotlinlang.org/).**

There are many ways everyone can contribute! From the most seasoned programmers to those who do not have the most remote clue how code works! Below are some things that can always use some love from the community.

* **Content Testers**: I'm putting this one up top because of its importance. We, the contributors and developers, aren't perfect. Sometimes, we make mistakes. This is where you come in - If you want a sneak peek at upcoming content, have a knack for breaking things, or just want to contribute to the project without making code changes, you can become a tester! If you are interested in becoming a tester, reach out in the testing channel of the discord. [Why become a tester?](#why-become-a-tester)


* **Wiki Editors**: Did you know we have a wiki? Well it's always in need of people to fill it out and stay on top of it. Editing the wiki is one of the easiest ways you can contribute to 2009Scape! If you're an active player and have the will, there's so much you could be helping out with over at the wiki. [Click here to go to the wiki](https://cdn.2009scape.org/wiki/doku.php?id=start).


* **JSON editors**: We could always use more JSON editors! Please note that JSON editing **must** be done using the [Thanos Tool](https://gitlab.com/2009scape/rs09-thanos-tool/-/jobs/artifacts/master/raw/build/libs/thanostool.jar?job=build).


* **Authenticity Auditors**: As a remake, authenticity is central to our core values! We could always use someone to go through the game and create large lists of simple tasks that can be done to bring us closer to the authentic 2009 game! The preferred way to do this is one-area-at-a-time. If you want to see an example of some audits we've done in the past, take a look [here](https://gitlab.com/2009scape/2009scape/-/issues/46).


* **Code Contributors**: As a remake, we have massive amounts of content that need to be implemented or corrected. If you know how to program or are willing to learn, this is where you could be extremely helpful! We need everything from quests, to dialogue, to mini-games, to skills that still need to be corrected or implemented. This is perhaps one of the most valuable ways someone could help out the project! If you are interested in developing content, reach out in the development channel of the Discord.

## Content Developers: Setting Up the Project.
### GitLab Setup
**Note: This allows you to commit changes to the main repo (with approval)! Also, always stay up to date with the most recent updates by pulling into your copy when 2009Scape updates!**

1. Create a GitLab account if you haven't done so already.

2. Follow our Git Basics guide [over on the wiki.](https://gitlab.com/2009scape/2009scape/-/wikis/git-basics)

**If at anytime you have an issue with GitLab please refer to the [GitLab help center](https://gitlab.com/help).**

### Prerequisites

These are mandatory. If you don't install **all** of these programs **in order** prior to
the project's setup, things won't work. At all.

*For Windows users* - Turn developer mode on first in Windows developer settings.

* [JDK 11](https://adoptium.net) or the Java SE Development Kit Version 11
* [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/)

### SSH setup

1. [Set up a key if you don't have one (ed25519)](https://docs.gitlab.com/ee/user/ssh.html#generate-an-ssh-key-pair)
2. [Add your public key to your gitlab account](https://docs.gitlab.com/ee/user/ssh.html#add-an-ssh-key-to-your-gitlab-account)
3. [Verify you can connect to git@gitlab.com](https://docs.gitlab.com/ee/user/ssh.html#verify-that-you-can-connect)

### Project Setup

1. If you haven't already, make sure to follow our [Git Basics](https://gitlab.com/2009scape/2009scape/-/wikis/git-basics) guide.
2. Run `git lfs pull` in the 2009scape folder you cloned as part of that guide. This only has to be done once, ever.
3. Follow our [IntelliJ IDEA Setup Guide](https://gitlab.com/2009scape/2009scape/-/wikis/Setup-for-IntelliJ-IDEA-IDE)

### Running the project

1. If you followed the IntelliJ setup guide, which you probably should have, just use the provided run configuration.
***Note: If you choose not to use the provided run scripts or IntelliJ, you *must* run `mvn clean` before it will build correctly.***

#### Linux / OSX

Start the game server with the included run script. Use `./run -h` for more info.

#### Windows

Start the game server with `run-server.bat`

### License

We use the AGPL 3.0 license, which can be found [here](https://www.gnu.org/licenses/agpl-3.0.en.html). Please be sure to read and understand the license. Failure to follow the guidelines outlined in the license will result in legal action. If you know or hear of anyone breaking this license, please send a report, with proof, to Red Bracket#8151, ceikry#2724, or woahscam#8535 on discord or email woahscam@hotmail.com. **We WILL NOT change the license to fit your needs.**

### Contact

**Reminder: There is no official support for setting up your own server. Do not ping/dm developers, or ask in support channels about setting up your server. Support for the live server and 2009Scape single-player is available in the Discord.**


[license-shield]: https://img.shields.io/badge/license-AGPL--3.0-informational
[license-url]: https://www.gnu.org/licenses/agpl-3.0.en.html

### Why become a tester?

Before content hits Live, it has to be tested. We can, but do not want to do this alone, and would love the help! But don't worry, there is something in it for you. Credits! Keep in mind that credits will only be awarded to people who thoroughly test the content and provide detailed reports on them. Like votes or HoF you must claim them in the #claim-to-fame channel on discord or matrix.

* If you test smaller things like bug fixes there is a 2 credit reward.
* If you test full quests or minigames you will be rewarded 5 credits.
* If you find a bug in the content you are testing that hasn't already been found you will earn an extra credit.

These credits can be spent in the 2009Scape Reward Shop. It's important to be clear that credits are gained by contributions to the project. We cannot and will not accept donations (of any kind), especially not in exchange for in-game credits or perks.

Testers are not the only people who can gain credits - other ways of earning credits can be found on [the 2009Scape website](https://2009scape.org/site/game_guide/credits.html).

Please be patient! The Credit system is not fully complete yet, so it will take a long time for credits to be awarded. 
