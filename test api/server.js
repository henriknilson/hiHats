var http = require('http');

http.createServer(function (req, res) {
  res.writeHead(200, {"Content-Type": "application/json"});
  var json = JSON.stringify({ 
    firtsName: 'Frellan', 
    lastName: 'Frellster', 
    age:100
  });
  res.end(json);
}).listen(1337, "127.0.0.1");

console.log('Server running at http://127.0.0.1:1337/');