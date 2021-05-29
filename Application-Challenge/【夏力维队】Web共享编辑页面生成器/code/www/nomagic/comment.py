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

def create_comment(comment):
    comment["type"] = "comment"
    comment["owner"] = comment.get("owner", "")
    comment["owner_type"] = comment.get("owner_type", "entity")
    comment["datetime"] = datetime.datetime.now().isoformat()
    comment["createtime"] = int(time.time())

    new_id = nomagic._new_key()
    while True:
        new_comment = nomagic._get_entity_by_id(new_id)
        if not new_comment:
            break
        else:
            new_id = nomagic._new_key()
    rowcount = nomagic._node(new_id).execute_rowcount("INSERT INTO entities (id, body) VALUES(%s, %s)", new_id, nomagic._pack(comment))
    assert rowcount
    return (new_id, comment)

def update_comment(comment_id, data):
    rowcount = nomagic._node(comment_id).execute_rowcount("UPDATE entities SET body = %s WHERE id = %s", nomagic._pack(data), nomagic._key(comment_id))
    assert rowcount

