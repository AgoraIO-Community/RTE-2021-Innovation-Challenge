#!/usr/bin/env python
# -*- coding: utf8 -*-
# import __init__ as nomagic
import nomagic

class LRUCache(object):
    def __init__(self, capacity):
        self.capacity = capacity
        self.tm = 0
        self.cache = {}
        self.lru = {}

    def get(self, key):
        if key in self.cache:
            self.lru[key] = self.tm
            self.tm += 1
            return self.cache[key]
        return None

    def set(self, key, value):
        if len(self.cache) >= self.capacity:
            # find the LRU entry
            old_key = min(self.lru.keys(), key=lambda k:self.lru[k])
            self.cache.pop(old_key)
            self.lru.pop(old_key)
        self.cache[key] = value
        self.lru[key] = self.tm
        self.tm += 1

    def unset(self, key):
        if key in self.cache:
            self.cache.pop(key)
        if key in self.lru:
            self.lru.pop(key)


def get_entity(cache, entity_id):
    entity = cache.get(entity_id)
    if not entity:
        entity = nomagic._get_entity_by_id(entity_id)
        cache.set(entity_id, entity)
    return entity

def get_entities(cache, entity_ids):
    missing_entity_ids = []
    existing_entities = {}
    for entity_id in entity_ids:
        entity = cache.get(entity_id)
        if entity:
            existing_entities[entity_id] = entity
        else:
            missing_entity_ids.append(entity_id)
    missing_entities = dict(nomagic._get_entities_by_ids(missing_entity_ids))
    result = []
    for entity_id in entity_ids:
        if entity_id in existing_entities:
            result.append((entity_id, existing_entities[entity_id]))
        else:
            entity = missing_entities[entity_id]
            result.append((entity_id, entity))
            cache.set(entity_id, entity)
    return result

def update_entity(cache, entity_id, entity):
    cache.unset(entity_id)
    nomagic._update_entity_by_id(entity_id, entity)


BIG_CACHE = LRUCache(6000)

def get_user(user_id):return get_entity(BIG_CACHE, user_id)
def get_users(user_ids):return get_entities(BIG_CACHE, user_ids)
def update_user(user_id, user):return update_entity(BIG_CACHE, user_id, user)

def get_doc(doc_id):return get_entity(BIG_CACHE, doc_id)
def get_docs(doc_ids):return get_entities(BIG_CACHE, doc_ids)
def update_doc(doc_id, doc):return update_entity(BIG_CACHE, doc_id, doc)

def get_aim(aim_id):return get_entity(BIG_CACHE, aim_id)
def get_aims(aim_ids):return get_entities(BIG_CACHE, aim_ids)
def update_aim(aim_id, aim):return update_entity(BIG_CACHE, aim_id, aim)
