from flask import Flask
from flask_sockets import Sockets
import numpy as np
import cv2 as cv
from mypose.process import load_Info, run_demo
app = Flask(__name__)
sockets = Sockets(app)
#cap = cv.VideoCapture(0)
net, cap, args = load_Info()


@sockets.route('/send-frame')
def send_frame(ws):
    while not ws.closed:
        message = ws.receive()
        if message == "":
            continue
        elif message == "update":
            # Capture frame-by-frame
            #ret, frame = cap.read()
            #gray = cv.cvtColor(frame, cv.COLOR_BGR2GRAY)
            ret, frame = cap.read()
            message = run_demo(net, frame, args)
            cv.putText(frame, message, (150,150), cv.FONT_HERSHEY_COMPLEX, 4.5, (0, 0, 255), 2)

            ret, png = cv.imencode('.png', frame)
            ws.send(png.tostring())
        else:
            continue


@app.route('/')
def hello():
    return 'Hello World!'


if __name__ == "__main__":
    from gevent import pywsgi
    from geventwebsocket.handler import WebSocketHandler
    server = pywsgi.WSGIServer(
        ('127.0.0.1', 9999), app, handler_class=WebSocketHandler)
    print("Begin")
    server.serve_forever()

