#!/bin/env python
# -*- coding: utf-8 -*-
import sys
import os
import os.path

sys.path.append(os.path.dirname(os.path.abspath(__file__)))
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + '/vendor/')

import re
import uuid
import time
import random
import string
import hashlib
import urllib
import copy
from functools import partial
import logging
import datetime

import markdown
import tornado
import tornado.web
import tornado.escape
import tornado.websocket
import tornado.httpclient
import tornado.gen
from tornado.escape import json_encode, json_decode

import nomagic
import nomagic.auth
import nomagic.block
import nomagic.group
from nomagic.cache import get_user, get_users, update_user, get_doc, get_docs, update_doc, get_aim, get_aims, update_aim, get_entity, get_entities, update_entity
from nomagic.cache import BIG_CACHE
from setting import settings
from setting import conn

# from user_agents import parse as uaparse #早年KJ用来判断设备使用

from .base import WebRequest
from .base import WebSocket
import pymail

from .data import DataWebSocket

class AddCommentAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        #判断是否为编辑者
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        comment_entities = block.get("comment_entities",[])
        group = {
            "owner":block_id,
            "editors":block.get("editors",[]),
            "subtype":"page_comment",
            "owner_type":"page",
        }
        [group_id,group] = nomagic.group.create_chat(group)
        comment_entities.append(group_id)
        block["comment_entities"]=comment_entities
        updatetime = int(time.time())
        block["updatetime"]=updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok","about":"add comment success","comment_entity":group_id})
class GetCommentAPIHandler(WebRequest):
    def get(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        if user_id not in block.get("editors",[block.get("owner",None)]):
        # if block.get("owner",None) != user_id:
            self.finish({"info":"error","about":"no in editors"})
            return
        comment_entities = block.get("comment_entities",[])
        self.finish({"info":"ok","about":"get comment success","comment_entities":comment_entities})












