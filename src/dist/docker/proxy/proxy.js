const proxy = require('express-http-proxy');
const express = require('express');
const curlify = require('request-as-curl');
var colors = require('colors');

let redirections = process.env.REDIRECTIONS || "";

function proxyOptions(color, httpsConnection) {
    return {
        reqAsBuffer: true,
        https: httpsConnection,
        proxyReqOptDecorator: function (proxyReqOpts, originalReq) {
            proxyReqOpts.rejectUnauthorized = false;
            return proxyReqOpts;
        },
        userResDecorator: function (proxyRes, proxyResData, userReq, userRes) {
            console.log(color(curlify(userReq)));
            console.log(color(colors.bold("RESPONSE CODE: " + userRes.statusCode)));
            return proxyResData;
        }
    }
}

var register = function (app, https) {
    redirections.split("+").forEach(redirection => {
        app.all("*/" + redirection.split("->")[0] + "/*", proxy(redirection.split("->")[1], proxyOptions(colors.green, https)));
    });

    app.all('*', (req, res, next) => {
        proxy(req.get('host'), proxyOptions(colors.white, https))(req, res, next)
    });
};

var http = express();
register(http, false);
http.listen(80, function () {
    console.log("Proxy listen on port 80");
});

var https = express();
register(https, true);
https.listen(443, function () {
    console.log("Proxy listen on port 443");
});



