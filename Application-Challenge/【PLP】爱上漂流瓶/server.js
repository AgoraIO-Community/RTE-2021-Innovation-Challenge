var http = require('http');
var io = require('socket.io');

// 创建server服务
var server = http.createServer(function(req, res) {
  var headers = {};
  headers['Access-Control-Allow-Origin'] = '*';
  headers['Access-Control-Allow-Methods'] = 'POST, GET, PUT, DELETE, OPTIONS';
  headers['Access-Control-Allow-Credentials'] = true;
  headers['Access-Control-Max-Age'] = '86400';
  headers['Access-Control-Allow-Headers'] =
    'X-Requested-With, Access-Control-Allow-Origin, X-HTTP-Method-Override, Content-Type, Authorization, Accept';
  res.writeHead(200, headers);
  res.end();
});

server.listen(3000, function() {
  console.log('server runing at 127.0.0.1:3000');
});

// 启动socket服务
var socket = io.listen(server, { origins: '*:*' });

var message = [];

// 监听客户端连接
socket.on('connection', function(socket) {
  console.log('连接');

  socket.on('sendMessage', function(value) {
    message.push({ id: socket.id, value: value });
    console.log('sendMessage')
    console.log(message)
  });

  socket.on('getMessage', function() {
    let hasRes = false;
    let tempValue;
    let tempPos;
    for (const temp in message) {
      if (message[temp].id != socket.id) {
        tempValue = message[temp];
        tempPos = temp;
        hasRes = true;
        break;
      }
    }
    console.log('getMessage')
    console.log(message)
    if (hasRes) socket.emit('sendMessage', tempValue.value);
    else socket.emit('sendMessage', '瓶子里什么也没有~~');
    message.splice(tempPos, 1);
  });
});
