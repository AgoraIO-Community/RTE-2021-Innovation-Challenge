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
from nomagic.cache import get_user, get_users, update_user, get_doc, get_docs, update_doc, get_aim, get_aims, update_aim, get_entity, get_entities, update_entity
from nomagic.cache import BIG_CACHE
from setting import settings
from setting import conn

# from user_agents import parse as uaparse #早年KJ用来判断设备使用

from .base import WebRequest
from .base import WebSocket
import pymail

from .data import DataWebSocket

class CountConnectNumAPIHandler(WebRequest):
    def get(self):
        block_id = self.get_argument("aim_id",None)
        connects = DataWebSocket.h_clients.get(block_id,None)
        num = 0
        exists = True
        if not connects:
            exists = False
        if exists:
            num = len(connects)
        self.finish({"info":"ok","num":num,"exists":exists})
class AddToTopPageAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"not login"})
            return
        block_id = self.get_argument("aim_id",None)
        user_id = self.current_user["id"]
        user = get_aim(user_id)
        pages = user.get("pages",[])
        if block_id not in pages:
            self.finish({"info":"error","about":"not in pages"})
            return
        pages_top = user.get("pages_top",[])
        if block_id in pages_top and block_id == pages_top[0]:
            self.finish({"info":"error","about":"already top"})
            return
        elif block_id in pages_top:
            pages_top.remove(block_id)
        pages_top.insert(0,block_id)
        user["pages_top"]=pages_top
        user["updatetime"]=int(time.time())
        update_aim(user_id,user)
        self.finish({"info":"ok","about":"add to top success"})
class RemoveFromTopPageAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"not login"})
            return
        block_id = self.get_argument("aim_id",None)
        user_id = self.current_user["id"]
        user = get_aim(user_id)
        pages = user.get("pages",[])
        if block_id not in pages:
            self.finish({"info":"error","about":"not in pages"})
            return
        pages_top = user.get("pages_top",[])
        if block_id not in pages_top:
            self.finish({"info":"error","about":"already remove from top"})
            return
        else:
            pages_top.remove(block_id)
        pages.remove(block_id)
        pages.insert(0,block_id)
        user["pages_top"]=pages_top
        user["pages"]=pages
        user["updatetime"]=int(time.time())
        update_aim(user_id,user)
        self.finish({"info":"ok","about":"remove from top success"})
class RemoveToTrashAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("aim_id",None)
        user = get_aim(user_id)
        pages = user.get("pages",[])
        pages_top_ids = user.get("pages_top",[])
        pages_trash_ids = user.get("pages_trash",[])
        update_need = False
        if block_id in pages:
            update_need = True
            pages.remove(block_id)
        if block_id in pages_top_ids:
            update_need = True
            pages_top_ids.remove(block_id)
        user["pages"]=pages
        user["pages_top"]=pages_top_ids
        if not update_need:
            self.finish({"info":"error","about":"not in already"})
            return
        if block_id not in pages_trash_ids:
            pages_trash_ids.insert(0,block_id)
            user["pages_trash"]=pages_trash_ids
        user["updatetime"]=int(time.time())
        update_aim(user_id,user)
        self.finish({"info":"ok","about":"remove success"})

