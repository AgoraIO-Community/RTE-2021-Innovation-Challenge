from flask import Flask
from flask_sockets import Sockets
import numpy as np
import cv2
from GestureProcess import process, load

app = Flask(__name__)
sockets = Sockets(app)
args, cap, completed_request_results, hpes, exceptions, mode_info, input_stream = load()
#start
num = 0

@sockets.route('/send-frame')
def send_frame(ws):
    while not ws.closed:
        message = ws.receive()
        #print(type(message))
        if message == "":
            continue
        elif message == "update":
            # Capture frame-by-frame
            ret, frame = cap.read()
            message = "test"
            message = process(args, cap, completed_request_results, hpes, exceptions, mode_info, input_stream)
            print("-************************************************************")
            print(message)
            cv2.putText(frame, message, (100, 100), cv2.FONT_HERSHEY_COMPLEX, 1.5, (0, 0, 255), 2)
            ret, png = cv2.imencode('.png', frame)
            ws.send(png.tostring())
            continue
            #print(type(png.tostring()))
        elif type(message) == bytearray:
            print("yes")
            nparr = np.array(message)
            img = cv2.imdecode(nparr, 1)
            cv2.imshow("dasd", img)
            cv2.waitKey(1)
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
    print('Start')
    server.serve_forever()

