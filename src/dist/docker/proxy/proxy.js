const proxy = require('express-http-proxy');
const app = require('express')();
const curlify = require('request-as-curl');
var colors = require('colors');

let redirections = process.env.REDIRECTIONS || "";

function proxyOptions(color) {
    return {
        reqAsBuffer: true,
        userResDecorator: function (proxyRes, proxyResData, userReq, userRes) {
            console.log(color(curlify(userReq)));
            console.log(color(colors.bold("RESPONSE CODE: " + userRes.statusCode)));
            return proxyResData;
        }
    }
}

redirections.split("+").forEach(redirection => {
    app.all("*/" + redirection.split("->")[0] + "/*", proxy(redirection.split("->")[1], proxyOptions(colors.green)));
});

app.all('*', (req, res, next ) => { proxy(req.get('host'), proxyOptions(colors.white))(req, res, next)});

app.listen(3000, function () {
    console.log("Proxy started!!");
});



