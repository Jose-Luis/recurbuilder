var proxy = require('express-http-proxy');
var app = require('express')();
 
let url = process.env.AEA_ENV + '.aireuropa.com';
let services = process.env.SERVICES || ""

services.split("+").forEach( service => { 
    app.all("*/" + service + "/*", proxy(service + ":8080")) ;
    console.log("Using local " + service);
});

app.use('*', proxy(url));

app.listen(3000, function () {
});


