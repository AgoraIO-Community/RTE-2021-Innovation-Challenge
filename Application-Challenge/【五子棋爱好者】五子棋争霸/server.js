var http = require("http");
var io = require("socket.io");

// 创建server服务
var server = http.createServer(function (req, res) {
  var headers = {};
  headers["Access-Control-Allow-Origin"] = "*";
  headers["Access-Control-Allow-Methods"] = "POST, GET, PUT, DELETE, OPTIONS";
  headers["Access-Control-Allow-Credentials"] = true;
  headers["Access-Control-Max-Age"] = "86400"; 
  headers["Access-Control-Allow-Headers"] =
    "X-Requested-With, Access-Control-Allow-Origin, X-HTTP-Method-Override, Content-Type, Authorization, Accept";
  res.writeHead(200, headers);
  res.end();
});

server.listen(3000, function () {
  console.log("server runing at 127.0.0.1:3000");
});

// 启动socket服务
var socket = io.listen(server, { origins: "*:*" });

var socketClients = {};
var fiveDesk = [];

// 监听客户端连接
socket.on("connection", function (socket) {
  var _socketId = socket.id;

  console.log("ID: " + _socketId + " 客户端连接");

  function setOK(result) {
    socket.emit("okResult", result);
  }

  function setError(result) {
    socket.emit("errorResult", result);
  }

  function delDesk(index) {
    fiveDesk.splice(index, 1);
  }

  function getClientByName(name) {
    for (var key in socketClients) {
      var item = socketClients[key];
      if (item.name === name) {
        return item;
      }
    }
  }

  function existsDesk(socketId) {
    for (var item of fiveDesk) {
      if (item.White === socketId || item.Black === socketId) {
        return true;
      }
    }
    return false;
  }

  socket.on("disconnect", () => {
    console.log("ID: " + _socketId + " 客户端断开");

    for (let key in socketClients) {
      if (key === _socketId) {
        delete socketClients[key];
        break;
      }
    }

    for (let i = fiveDesk.length - 1; i >= 0; i--) {
      var desk = fiveDesk[i];
      if (desk.White === _socketId || desk.Black === _socketId) {
        socketClients[
          desk.White === _socketId ? desk.Black : desk.White
        ].socket.emit("break");
        delDesk(i);
        break;
      }
    }

    updateAllClients();
  });

  socket.on("setName", function (name) {
    if (getClientByName(name)) {
      setError({ Message: "对不起，已存在该名称！" });
      return;
    }

    socketClients[socket.id] = {
      id: socket.id,
      socket: socket,
      name: name,
    };

    setOK(name);
    updateAllClients();
  });

  socket.on("tryGetUserName", function () {
    if(socketClients.hasOwnProperty()){
      setOK(socketClients[socket.id].name);
    }    
    updateAllClients();
  });

  function updateAllClients() {
    for (let key in socketClients) {
      var client = socketClients[key];
      var result = [];
      if (!existsDesk(key)) {
        for (let key2 in socketClients) {
          result.push({ name: socketClients[key2].name });
        }
        client.socket.emit("updateClients", result);
      }
    }
  }

  socket.on("initClients", function () {
    var result = [];
    for (let key in socketClients) {
      result.push({ name: socketClients[key].name });
      socket.emit("updateClients", result);
    }
  });

  socket.on("applyConnect", function (name) {
    var thisClient = socketClients[socket.id];
    var client = getClientByName(name);
    if (!client) {
      thisClient.socket.emit("errorResult", {
        IsSuccess: false,
        Message: "对方不在线",
      });
      return;
    }

    var socketId = client.id;

    if (existsDesk(socketId)) {
      thisClient.socket.emit("errorResult", {
        IsSuccess: false,
        Message: "对方已加入其它棋局",
      });
      return;
    }

    if (!thisClient || !client) return;

    client.socket.emit("applyConnect", {
      IsSuccess: true,
      Message: thisClient.name,
    });
  });

  socket.on("agreeConnect", function (name) {
    var thisClient = socketClients[socket.id];
    var client = getClientByName(name);
    if (!client) {
      thisClient.socket.emit("errorResult", {
        IsSuccess: false,
        Message: "对方不在线",
      });
      return;
    }

    var socketId = client.id;

    if (existsDesk(socketId)) {
      thisClient.socket.emit("errorResult", {
        IsSuccess: false,
        Message: "对方已加入其它棋局",
      });
      return;
    }

    fiveDesk.push({
      White: thisClient.id,
      Black: client.id,
      Result: false,
    });
    var deskId = fiveDesk.length - 1;
    var desk = fiveDesk[deskId];
    desk.Result = true;

    console.log("White ", desk.White);
    console.log("Black ", desk.Black);

    socketClients[desk.White].socket.emit("deskBegin", {
      role: 1,
      deskId: deskId,
    });
    socketClients[desk.Black].socket.emit("deskBegin", {
      role: 2,
      deskId: deskId,
    });
  });

  socket.on("clickPiece", function (data) {
    var deskId = data.deskId;
    if (fiveDesk.length > deskId) {
      var desk = fiveDesk[deskId];
      var socket;
      if (data.role === 1) {
        socket = socketClients[desk.Black].socket;
      } else {
        socket = socketClients[desk.White].socket;
      }

      socket.emit("serverClickPiece", { x: data.x, y: data.y });
    }
  });
});
