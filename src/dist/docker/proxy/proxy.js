const proxy = require('express-http-proxy');
const app = require('express')();
const curlify = require('request-as-curl');
var colors = require('colors');

let url = "http://" + process.env.AEA_ENV + '.aireuropa.com';
let services = process.env.SERVICES || "";

function proxyOptions(color) {
    return {
        reqAsBuffer: true,
        userResDecorator: function (proxyRes, proxyResData, userReq, userRes) {
            console.log(color(curlify(userReq)));
            console.log(color(colors.bold("RESPONSE CODE: "+ userRes.statusCode)));
            return proxyResData;
        }
    }
}

services.split("+").forEach( service => {
    app.all("*/" + service + "/*", proxy(service + ":8080", proxyOptions(colors.green)));
    console.log("Using local " + service);
});

app.all('*', proxy(url, proxyOptions(colors.white)));

app.listen(3000, function () {
    console.log("Proxy started!!");
});


