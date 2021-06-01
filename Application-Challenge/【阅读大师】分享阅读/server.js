var http = require('http');
var io = require('socket.io');

// 创建server服务
var server = http.createServer(function(req, res) {
  var headers = {};
  headers['Access-Control-Allow-Origin'] = '*';
  headers['Access-Control-Allow-Methods'] = 'POST, GET, PUT, DELETE, OPTIONS';
  headers['Access-Control-Allow-Credentials'] = true;
  headers['Access-Control-Max-Age'] = '86400'; // 24 hours
  headers['Access-Control-Allow-Headers'] =
    'X-Requested-With, Access-Control-Allow-Origin, X-HTTP-Method-Override, Content-Type, Authorization, Accept';
  res.writeHead(200, headers);
  res.end();
});

// 启动服务器  监听 3000 端口
server.listen(3000, function() {
  console.log('server runing at 127.0.0.1:3000');
});

// 启动socket服务
var socket = io.listen(server, { origins: '*:*' });

const fs = require('fs');
const readline = require('readline');

// 监听客户端连接
socket.on('connection', function(socket) {
  console.log('客户端连接');

  socket.on('disconnect', () => {
    console.log('客户端断开');
  });

  socket.on('getBook', function(bookData) {
    var data = [];
    const input = fs.createReadStream(
      './src/assets/book/' + bookData.bookId + '.txt'
    );
    input.setEncoding('utf8');
    const rl = readline.createInterface({
      input: input
    });
    rl.on('line', line => {
      data.push(line);
    });
    rl.on('close', () => {
      socket.emit('getBook', data);
    });
  });
});
