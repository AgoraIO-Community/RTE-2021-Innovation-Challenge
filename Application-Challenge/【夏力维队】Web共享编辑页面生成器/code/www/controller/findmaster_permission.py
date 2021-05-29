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

class PageSetPermissionAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"error","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        permission = self.get_argument("permission","private")
        if permission not in ["public","publish","private"]:
            self.finish({"info":"error","about":"wrong permission"})
            return
        if not block:
            self.finish({"info":"error","about":"no block"})
            return
        if user_id not in block.get("editors",[block.get("owner",None)]):
            self.finish({"info":"error","about":"not in editors"})
            return
        old_permission = block.get("permission","private")
        if permission == old_permission:
            self.finish({"info":"error","about":"same permission"})
            return
        block["permission"]=permission
        updatetime = int(time.time())
        block["updatetime"]=updatetime
        update_aim(block_id,block)
        self.finish({"info":"ok","block_id":block_id,"permission":permission})
        content_data = {
            "permission":permission,
            "action":"reload page"
        }
        msgtype = "COMMENTPAGEPERMISSION"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageEditorsAPIHandler(WebRequest):
    def get(self):
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        editors = block.get("editors",[block.get("owner",None)])
        readers = block.get("readers",[])
        blackers = block.get("blockers",[])
        members = block.get("members",[])
        permission = block.get("permission","private")
        if not self.current_user:
            if permission in ["private"]:
                self.finish({"info":"ok","about":"private","editors":[]})
                return
        if permission in ["private"]:
            user_id = self.current_user["id"]
            if user_id not in editors and user_id not in readers:
                self.finish({"info":"ok","about":"private","editors":[]})
                return
        self.finish({"info":"ok","about":"private","editors":editors})        

class PageReadersAPIHandler(WebRequest):
    def get(self):
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        editors = block.get("editors",[block.get("owner",None)])
        readers = block.get("readers",[])
        blackers = block.get("blockers",[])
        members = block.get("members",[])
        permission = block.get("permission","private")
        if not self.current_user:
            if permission in ["private"]:
                self.finish({"info":"ok","about":"private","readers":[]})
                return
        if permission in ["private"]:
            user_id = self.current_user["id"]
            if user_id not in editors and user_id not in readers:
                self.finish({"info":"ok","about":"private","readers":[]})
                return
        self.finish({"info":"ok","about":"private","readers":readers})        

class PageMembersAPIHandler(WebRequest):
    def get(self):
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        editors = block.get("editors",[block.get("owner",None)])
        readers = block.get("readers",[])
        blackers = block.get("blockers",[])
        members = block.get("members",[])
        permission = block.get("permission","private")
        if not self.current_user:
            if permission in ["private"]:
                self.finish({"info":"ok","about":"private","members":[]})
                return
        if permission in ["private"]:
            user_id = self.current_user["id"]
            if user_id not in editors and user_id not in readers:
                self.finish({"info":"ok","about":"private","members":[]})
                return
        self.finish({"info":"ok","about":"private","members":members})        

class PageBlackersAPIHandler(WebRequest):
    def get(self):
        block_id = self.get_argument("block_id",None)
        block = get_aim(block_id)
        editors = block.get("editors",[block.get("owner",None)])
        readers = block.get("readers",[])
        blackers = block.get("blockers",[])
        members = block.get("members",[])
        permission = block.get("permission","private")
        if not self.current_user:
            if permission in ["private"]:
                self.finish({"info":"ok","about":"private","blackers":[]})
                return
        if permission in ["private"]:
            user_id = self.current_user["id"]
            if user_id not in editors and user_id not in readers:
                self.finish({"info":"ok","about":"private","blackers":[]})
                return
        self.finish({"info":"ok","about":"private","blackers":blackers})        

class PageRulesAPIHandler(WebRequest):
    def get(self):
        pass

class PageAddEditorAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"ok","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        aim_user_id = self.get_argument("aim_user_id",None)

        block = get_aim(block_id)
        editors = block.get("editors",[block.get("owner",None)])
        updatetime = int(time.time())
        # permission = block.get("permission","private")
        if user_id not in editors:
            self.finish({"info":"ok","about":"current user not in editors"})
            return
        if aim_user_id in editors:
            self.finish({"info":"ok","about":"already in editors"})
            return
        else:
            editors.append(aim_user_id)
            block["editors"]=editors
            block["updatetime"]=updatetime
            update_aim(block_id,block)
            self.finish({"info":"ok","about":"add one editor","aim_user_id":aim_user_id})
        content_data = {
            "aim_user_id":aim_user_id,
            "action":"add editor"
        }
        msgtype = "COMMENTPAGEADDEDITOR"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))

class PageDelEditorAPIHandler(WebRequest):
    def post(self):
        if not self.current_user:
            self.finish({"info":"ok","about":"no login"})
            return
        user_id = self.current_user["id"]
        block_id = self.get_argument("block_id",None)
        aim_user_id = self.get_argument("aim_user_id",None)

        block = get_aim(block_id)
        editors = block.get("editors",[block.get("owner",None)])
        updatetime = int(time.time())
        # permission = block.get("permission","private")
        if user_id not in editors:
            self.finish({"info":"ok","about":"current user not in editors"})
            return
        if aim_user_id not in editors:
            self.finish({"info":"ok","about":"already not in editors"})
            return
        else:
            editors.remove(aim_user_id)
            block["editors"]=editors
            block["updatetime"]=updatetime
            update_aim(block_id,block)
            self.finish({"info":"ok","about":"del one editor","aim_user_id":aim_user_id})
        content_data = {
            "aim_user_id":aim_user_id,
            "action":"del editor"
        }
        msgtype = "COMMENTPAGEDELEDITOR"
        msg = [msgtype, {
            "content": content_data,
            "nickname": "",
            "headimgurl": "/static/img/oflogo.png",
            "tel": "",
            "time": updatetime,
            "user_id": user_id,
            "sequence": "",
            "comment_id": ""
        }, block_id]
        DataWebSocket.send_to_all(json_encode(msg))
class PageAddReaderAPIHandler(WebRequest):
    def post(self):
        pass
class PageDelReaderAPIHandler(WebRequest):
    def post(self):
        pass
class PageAddBlackerAPIHandler(WebRequest):
    def post(self):
        pass
class PageDelBlackerAPIHandler(WebRequest):
    def post(self):
        pass