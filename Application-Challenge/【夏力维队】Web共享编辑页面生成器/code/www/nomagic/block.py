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

def create_block(block):
    time_now = int(time.time())
    block["type"] = "block"
    block["owner"] = block.get("owner", "")
    block["datetime"] = datetime.datetime.now().isoformat()
    block["createtime"] = time_now
    block["updatetime"] = time_now

    new_id = nomagic._new_key()
    while True:
        new_block = nomagic._get_entity_by_id(new_id)
        if not new_block:
            break
        else:
            new_id = nomagic._new_key()
    rowcount = nomagic._node(new_id).execute_rowcount("INSERT INTO entities (id, body) VALUES(%s, %s)", new_id, nomagic._pack(block))
    assert rowcount
    return (new_id, block)

def update_block(block_id, data):
    rowcount = nomagic._node(block_id).execute_rowcount("UPDATE entities SET body = %s WHERE id = %s", nomagic._pack(data), nomagic._key(block_id))
    assert rowcount

