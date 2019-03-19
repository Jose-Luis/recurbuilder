const proxy = require('express-http-proxy');
const app = require('express')();
const curlify = require('request-as-curl');
const colors = require('colors');

app.all('*',  proxy(process.env.SERVICE_URL + ":" + process.env.SERVICE_PORT, {
    proxyReqBodyDecorator: function(bodyContent, srcReq) {
        console.log(colors.underline("REQUEST:".yellow));
        console.log(curlify(srcReq));
        console.log(colors.green(bodyContent.toString()));
        return bodyContent;
    },
    userResDecorator: function (proxyReq, proxyRes, userReq, userRes) {
        console.log(colors.underline("RESPONSE:".yellow));
        console.log(colors.cyan(proxyRes.toString()));
        console.log(colors.magenta("RESPONSE CODE: " + userRes.statusCode));
        return proxyRes;
    }
}));

app.listen(process.env.SERVICE_PORT, function () {
    console.log("Proxy started!!");
});



