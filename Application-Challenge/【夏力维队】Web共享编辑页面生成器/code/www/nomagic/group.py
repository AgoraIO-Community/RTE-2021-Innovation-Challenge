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

def create_chat(group):
    group["type"] = "group"
    group["owner"] = group.get("owner", "")
    group["owner_type"] = group.get("owner_type", "entity")
    group["editors"] = group.get("editors",[])
    group["helpers"] = group.get("helpers",{})
    group["comment_members"] = group.get("comment_members",[])
    group["notifyers"] = group.get("notifyers",[])
    group["blackers"] = group.get("blackers",[])
    group["datetime"] = datetime.datetime.now().isoformat()
    group["createtime"] = int(time.time())

    new_id = nomagic._new_key()
    while True:
        new_group = nomagic._get_entity_by_id(new_id)
        if not new_group:
            break
        else:
            new_id = nomagic._new_key()
    rowcount = nomagic._node(new_id).execute_rowcount("INSERT INTO entities (id, body) VALUES(%s, %s)", new_id, nomagic._pack(group))
    assert rowcount
    return (new_id, group)

def update_group(group_id, data):
    rowcount = nomagic._node(group_id).execute_rowcount("UPDATE entities SET body = %s WHERE id = %s", nomagic._pack(data), nomagic._key(group_id))
    assert rowcount

