# -*- encoding: utf8 -*-
import os

import tornado.web
import tornado.websocket
import tornado.locale

class BaseHandler(object):
    def get_current_user(self):
        user_json = self.get_secure_cookie("user")
        if not user_json:
            return {}
        user_data = tornado.escape.json_decode(user_json)
        if user_data.get("v", 0) < 1:
            return {}
        return user_data

    #def get_access_token(self):
    #    return None

    #def get_user_locale(self):
    #    return tornado.locale.get("zh_CN")
    def get_user_locale(self):
        user_locale = self.get_argument('lang', "zh")
        if user_locale == 'en':
            return tornado.locale.get('en_US')
        elif user_locale == 'zh':
            return tornado.locale.get('zh_CN')

class WebRequest(BaseHandler, tornado.web.RequestHandler):
    def get_error_html(self, status_code, **kwargs):
        return open(os.path.join(os.path.dirname(__file__), "../static/404.html")).read()

class WebSocket(BaseHandler, tornado.websocket.WebSocketHandler):
    def check_origin(self, origin):
        return True
