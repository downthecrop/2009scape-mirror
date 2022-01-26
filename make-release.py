#!/usr/bin/env python3

import subprocess
import datetime
import os


WEB_REPO = '../2009scape.github.io'
NEWS_DIR = 'services/m=news/archives'
LOG_DELIMITER = ';;;;;'
DEBUG = False


class Tag:
    def __init__(self, tag_name, last_tag):
        self.tag_name = tag_name
        self.last_tag = last_tag


def make_tag() -> Tag:
    tag_name = datetime.datetime.now().strftime('%b-%d-%Y')
    print('new release tag:', tag_name)
    last_tag = subprocess.check_output(['git', 'describe', '--tags', '--abbrev=0']).decode('utf8').strip()
    print('last release tag:', last_tag)
    if not DEBUG:
        subprocess.run(['git', 'tag', tag_name])
    return Tag(tag_name, last_tag)


def get_changelog_html(tag: Tag) -> str:
    if DEBUG:
        log_period = f'{tag.last_tag}..HEAD'
    else:
        log_period = f'{tag.last_tag}..{tag.tag_name}'
    changelog = subprocess.check_output(['git', 'log', log_period, f'--format=%B{LOG_DELIMITER}']).decode('utf8').split(LOG_DELIMITER)
    changelog_html = ''.join(['<li>' + change.strip().replace('\n', '<br />\n') + '</li>\n' for change in changelog if change.strip()])
    changelog_html = f'<ul>\n{changelog_html}</ul>'
    print('generated changelog:', changelog_html, sep='\n')
    return changelog_html


def make_news_post(tag: Tag) -> None:
    changelog_html = get_changelog_html(tag)
    os.chdir(os.path.join(WEB_REPO, NEWS_DIR))
    current_date = datetime.datetime.now().strftime('%Y-%m-%d')
    news_filename = current_date + '.html' 
    print('news filename:', news_filename)
    news_post = news_template(current_date, changelog_html)
    if DEBUG:
        print('generated news post:', news_post)
    else:
        if news_filename in os.listdir():
            raise FileExistsError(news_filename)
        with open(news_filename, 'wt') as news_handle:
            news_handle.write(news_post)


def news_template(current_date: str, changelog: str) -> str:
    return '''---
title: More Changes in Gielinor
tags: news
layout: newspost
collection: Game Updates
date: '''+ current_date + ''' 00:00:00 +0000
authors: ryannathans
excerpt: "There have been some more changes in Gielinor..."
---
<div id="content">
    <div id="article">
        <div class="sectionHeader">
            <div class="left">
                <div class="right">
                    <h1 class="plaque">
                        {{ page.date | date: '%d-%B-%Y' }}
                    </h1>
                </div>
            </div>
        </div>
        <div class="section">
            <div class="brown_background">
            </div>
            <div id="contrast_panel">
                <div id="infopane">
                    <div class="title thrd">{{ page.title }}
                    </div>
                </div>
                <div class="phold" id="nocontrols"></div>
                <div class="actions" id="top">
                    <table>
                        <tbody>
                            <tr>
                                <td class="commands center">
                                    <ul class="flat first-child">
                                        <li><a href="./archives.html"><img alt=""
                                                    src="../../../site/img/forum/cmdicons/backtoforum.gif"> Up to Legacy
                                                Update List</a></li>
                                        <li>
                                            <a href=""><img alt="" src="../../../site/img/forum/cmdicons/refresh.gif">
                                                Refresh</a>
                                        </li>
                                    </ul>
                                </td>
                            </tr>
                            <tr>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="" id="contentmsg">
                    <a class="msgplace" name="0"></a>
                    <table cellspacing="0" class="message jmod">
                        <tbody>
                            <tr>
                                <td class="leftpanel J-Mod">
                                    <div class="msgcreator uname">
                                        {{ page.authors }}
                                    </div>
                                    <img alt="" class="avatar" src="../../m=avatar-rs/avatar8fa9.gif">
                                    <div class="modtype">???</div>
                                    <div class="msgcommands">

                                        <br>
                                    </div>
                                </td>
                                <td class="rightpanel">
                                    <div class="msgtime">
                                        {{ page.date | date: '%d-%B-%Y' }}
                                        <br>
                                    </div>
                                    <div class="msgcontents">
                                        <p>Greetings Explorers</p>
                                        <p>There have been some more changes in Gielinor:</p>''' + changelog + '''
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="actions" id="bottom">
                    <table>
                        <tbody>
                            <tr>
                                <td class="commands center">
                                    <ul class="flat first-child">
                                        <li><a href="./archives.html"><img alt=""
                                                    src="../../../site/img/forum/cmdicons/backtoforum.gif"> Up to Legacy
                                                Update List</a></li>
                                        <li>
                                            <a href=""><img alt="" src="../../../site/img/forum/cmdicons/refresh.gif">
                                                Refresh</a>
                                        </li>
                                    </ul>
                                </td>
                            </tr>
                            <tr>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>'''


def main() -> None:
    tag = make_tag()
    make_news_post(tag)


if __name__ == '__main__':
    main()
