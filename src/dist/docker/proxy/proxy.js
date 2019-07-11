const proxy = require('express-http-proxy');
const express = require('express');
const curlify = require('request-as-curl');
const colors = require('colors');
const readline = require('readline-sync');

let redirections = process.env.REDIRECTIONS || "";

let editions = process.env.EDITIONS || "";

function proxyOptions(color, httpsConnection) {
    return {
        reqAsBuffer: true,
        https: httpsConnection,
        proxyReqBodyDecorator: function (bodyContent, srcReq) {
            if (editions !== undefined && editions !== "" && bodyContent !== undefined && bodyContent.length > 0 && srcReq.url.includes(editions)) {
                console.log(colors.red(bodyContent.toString()));
                var newBody = readline.question('Insert new body (Intro to continue) : ');
                console.log(newBody);
                if (newBody) {
                    return newBody;
                }
                return bodyContent;
            }
            return bodyContent;
        },
        userResDecorator: function (proxyRes, proxyResData, userReq, userRes) {
            console.log(color(curlify(userReq)));
            console.log(color(colors.bold("RESPONSE CODE: " + userRes.statusCode)));
            return proxyResData;
        }
    }
}

var register = function (app, https, editions) {
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



