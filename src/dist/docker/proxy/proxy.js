var proxy = require('express-http-proxy');
var app = require('express')();
 
let url = "http://" + process.env.AEA_ENV + '.aireuropa.com';
let services = process.env.SERVICES || "";

services.split("+").forEach( service => {
    app.all("*/" + service + "/*", proxy(service + ":8080", {
        reqAsBuffer: true
    }));
    console.log("Using local " + service);
});

app.all('*', proxy(url, {
    reqAsBuffer: true
}));

app.listen(3000, function () {
    console.log("Proxy started!!");
});


