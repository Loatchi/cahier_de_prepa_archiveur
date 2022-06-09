#!/usr/bin/python
import sys

import requests
import argparse
import os
import re
import os.path
from anytree import Node

parser = argparse.ArgumentParser()

parser.add_argument(
    "--cookie",
    default=None,
    help="Le cookie CDP_SESSION_PERM (il faut cocher 'se souvenir de moi' dans cahier de prépas "
         "(En étant connecté, sur Chromium F12: dans Application/Cookie: "
         "la colonne \"value\"). Après la fin du téléchargement, vous pouvez supprimer CDP_SESSION_PERM."
)

parser.add_argument(
    "--class",
    default=None,
    help="La classe dans laquelle vous êtes c'est CLASSE dans https://cahier-de-prepa.fr/CLASSE/"
)

CDP = "https://cahier-de-prepa.fr/"
CLASS = ""
HEADERS = {
    "Host": "cahier-de-prepa.fr",
    "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.61 "
                  "Safari/537.36",
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,"
              "application/signed-exchange;v=b3;q=0.9",
    "Accept-Language": "en-US;q=0.5",
    "Cookie": "CDP_SESSION_PERM=COOKIE",
    "Connection": "keep-alive"
}

NUMBER_OF_FILES = 0
FINISHED_NUMBER_OF_FILES = 0


class CDPFile:

    def __init__(self, is_dir, name, url):
        self.is_dir = is_dir
        self.name = name
        self.url = url

    def __repr__(self):
        return self.name


def parse_main_page(html: str) -> list[tuple[str, str]]:
    regex = re.compile("<a href=\"docs\?([^\"]*)\">")
    urls = []
    for cls in re.findall(regex, html):
        urls.append((cls, CDP + CLASS + "/docs?" + cls))
    return urls


def construct_tree(node, url: str):

    global NUMBER_OF_FILES

    html = requests.get(url, headers=HEADERS)
    regex_dir = re.compile("<a href=\"\?rep=([^\"]*)\">.+<span class=\"nom\">(.+)</span>")
    regex_file = re.compile("<span class=\"docdonnees\">\(([a-z]+).*<a href=\"download\?id=(\d+)\">"
                            ".+<span class=\"nom\">(.+)</span>")

    for redirect_int, name in re.findall(regex_dir, html.text):
        new_url = CDP + CLASS + "/docs?rep=" + redirect_int
        sub = Node(CDPFile(True, name, new_url), parent=node)
        print("Found Directory: " + sub.name.name)
        construct_tree(sub, new_url)

    for filetype, redirect_int, name in re.findall(regex_file, html.text):
        new_url = CDP + CLASS + "/download?id=" + redirect_int
        sub = Node(CDPFile(False, name + "." + filetype, new_url), parent=node)
        print("Found File: " + sub.name.name)
        NUMBER_OF_FILES += 1


def create_hierarchy(tree: Node, total_dir: str):

    global NUMBER_OF_FILES
    global FINISHED_NUMBER_OF_FILES

    if tree.name.is_dir:
        if not os.path.isdir(total_dir):
            os.mkdir(total_dir)

        for node in tree.children:
            create_hierarchy(node, total_dir + "/" + re.sub(re.compile(r"[\"/:<>\\|?*]"), "", node.name.name))

        return

    dl_url = tree.name.url
    request = requests.get(dl_url, headers=HEADERS, stream=True)  # stream=True, avoid ram consumption

    if os.path.exists(total_dir):
        print(f"File: {total_dir} already exists, skipping" +
              " (" + str(FINISHED_NUMBER_OF_FILES) + "/" + str(NUMBER_OF_FILES) + ")")

    else:
        with open(total_dir, "+wb") as file:
            for chunk in request.iter_content(chunk_size=100):  # 100 byte per 100byte
                file.write(chunk)

        print("Downloaded: " + "[" + total_dir + "]" + " (" + str(FINISHED_NUMBER_OF_FILES) + "/" + str(NUMBER_OF_FILES) + ")")

    FINISHED_NUMBER_OF_FILES += 1

    
def main(args):
    global CLASS
    global HEADERS
    CLASS = args.__dict__["class"]
    HEADERS["Cookie"] = "CDP_SESSION_PERM=" + args.cookie

    html = requests.get(CDP + CLASS)

    sub_dir = parse_main_page(html.text)

    node = Node(CDPFile(True, "CDP-" + CLASS, None))

    for sd in sub_dir:
        cls = Node(CDPFile(True, sd[0], sd[1]), parent=node)
        construct_tree(cls, sd[1])

    create_hierarchy(node, node.name.name)


if __name__ == '__main__':
    args = parser.parse_args()
    if args.cookie is None:
        print("Vous devez utiliser un cookie: --help pour plus d'infos.")
        sys.exit(0)

    if args.__dict__["class"] is None:
        print("Vous devez fournir un nom de classe: --help pour plus d'infos")
        sys.exit(0)

    main(args)
