#!/usr/bin/env python3

import subprocess
import datetime
import os


WEB_REPO = '../2009scape.github.io'
NEWS_DIR = 'updates/_posts'
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
    changelog_html = ''.join(['- ' + change.strip().replace('\n', '<br />\n') + '\n' for change in changelog if change.strip()])
    changelog_html = f'\n{changelog_html}'
    print('generated changelog:', changelog_html, sep='\n')
    return changelog_html


def make_news_post(tag: Tag) -> None:
    changelog_html = get_changelog_html(tag)
    os.chdir(os.path.join(WEB_REPO, NEWS_DIR))
    current_date = datetime.datetime.now().strftime('%Y-%m-%d')
    news_filename = f'{current_date}-More-changes-in-Gielinor.md'
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
    return f'''---
title: More changes in Gielinor
tags: news
layout: newspost
date: {current_date} 00:00:00 +0000
authors: ryannathans
excerpt: "There have been some more changes in Gielinor..."
modtype: "Lead Developer"
avatar: avatar8fa9.gif
---
Greetings Explorers

There have been some more changes in Gielinor:

{changelog}
'''


def main() -> None:
    tag = make_tag()
    make_news_post(tag)


if __name__ == '__main__':
    main()
