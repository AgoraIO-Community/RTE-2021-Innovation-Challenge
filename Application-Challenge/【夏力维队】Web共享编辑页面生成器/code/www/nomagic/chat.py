#!/usr/bin/env python
# -*- coding: utf8 -*-

import time
import datetime
import pickle
import uuid
import binascii

import zlib
#import gzip
import hashlib
import json
import random
import string

# import __init__ as nomagic
import nomagic

from setting import conn

def create_chat(chat):
    chat["type"] = "chat"
    chat["owner"] = chat.get("owner", "")
    chat["owner_type"] = chat.get("owner_type", "entity")
    chat["editors"] = chat.get("editors",[])
    chat["helpers"] = chat.get("helpers",{})
    chat["comment_members"] = chat.get("comment_members",[])
    chat["notifyers"] = chat.get("notifyers",[])
    chat["blackers"] = chat.get("blackers",[])
    chat["datetime"] = datetime.datetime.now().isoformat()
    chat["createtime"] = int(time.time())

    new_id = nomagic._new_key()
    while True:
        new_chat = nomagic._get_entity_by_id(new_id)
        if not new_chat:
            break
        else:
            new_id = nomagic._new_key()
    rowcount = nomagic._node(new_id).execute_rowcount("INSERT INTO entities (id, body) VALUES(%s, %s)", new_id, nomagic._pack(chat))
    assert rowcount
    return (new_id, chat)

def update_chat(chat_id, data):
    rowcount = nomagic._node(chat_id).execute_rowcount("UPDATE entities SET body = %s WHERE id = %s", nomagic._pack(data), nomagic._key(chat_id))
    assert rowcount

