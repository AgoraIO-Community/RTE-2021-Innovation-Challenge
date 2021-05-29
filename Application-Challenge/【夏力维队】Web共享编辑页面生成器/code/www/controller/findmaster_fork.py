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

class ForkPageAPIHandler(WebRequest):
    def get(self):
        self.post()
    def post(self):
        block_id = self.get_argument("aim_id",None)
        if not self.current_user:
            self.finish({"info":"error","about":"not login"})
            return
        user_id = self.current_user["id"]
        block = get_aim(block_id)
        if not block:
            self.finish({"info":"error","about":"no block"})
            return
        subtype = block.get("subtype","")
        if subtype != "page":
            self.finish({"info":"error","about":"block is not page"})
            return
        fork_allow = block.get("fork_allow",True)
        if not fork_allow:
            self.finish({"info":"error","about":"not allow fork"})
            return

        title = block.get("title","")
        desc = block.get("desc","")
        doms = block.get("doms",[])
        main_area = block.get("main_area",{})
        grid_graph = block.get("grid_graph",{})
        updatetime = int(time.time())
        fork_block = {
            "owner":user_id,
            "subtype":"page",
            "title":title,
            "desc":desc,
            "doms":doms,
            "main_area":main_area,
            "grid_graph":grid_graph,
            "history":[],
            "updatetime":updatetime,
            "fork_allow":True,
            "fork_from":block_id,
        }
        [new_block_id,fork_block]=nomagic.block.create_block(fork_block)
        user = get_aim(user_id)
        pages = user.get("pages",[])
        pages.insert(0,new_block_id)
        user["pages"]=pages
        update_aim(user_id,user)

        fork_entity_id = block.get("fork_entity",None)
        if not fork_entity_id:
            fork_entity_block = {
                "owner":block_id,
                "subtype":"fork",
                "fork_pages":[new_block_id],
                "fork_history":[[new_block_id,user_id,updatetime]],
                "updatetime":updatetime,
            }
            [fork_entity_id,fork_entity_block]=nomagic.block.create_block(fork_entity_block)
            block["fork_entity"]=fork_entity_id
            update_aim(block_id,block)
        else:
            fork_entity_block = get_aim(fork_entity_id)
            fork_pages = fork_entity_block.get("fork_pages",[])
            fork_pages.insert(0,new_block_id)
            fork_history = fork_entity_block.get("fork_history",[])
            fork_history.append([new_block_id,user_id,updatetime])
            fork_entity_block["fork_pages"] = fork_pages
            fork_entity_block["fork_history"] = fork_history
            update_aim(fork_entity_id,fork_entity_block)
        self.redirect("/home/page/edit/%s"%new_block_id)

