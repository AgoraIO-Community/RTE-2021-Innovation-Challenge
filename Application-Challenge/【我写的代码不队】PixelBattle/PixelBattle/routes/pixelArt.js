var express = require('express');
var router = express.Router();
var path = require('path');
var fs = require('fs');
var newestImg=0;
var file=``;
var image = [];
//每delay秒遍历一次数组,写入文件 建议大于60
let delay =60
console.log("init img")
for (let i = 0; i < 400; i++) {
    var b = [];
    for (let j = 0; j < 400; j++) {
        b[j] = {
            r: 255,
            g: 255,
            b: 255
        }
    }
    image[i] = b;
}
//预设图案: RTM
image[5][5]={r:0,g: 0,b:0};image[6][5]={r:0,g: 0,b:0};image[7][5]={r:0,g: 0,b:0};
image[5][6]={r:0,g: 0,b:0};image[7][6]={r:0,g: 0,b:0};
image[5][7]={r:0,g: 0,b:0};image[6][7]={r:0,g: 0,b:0};
image[5][8]={r:0,g: 0,b:0};image[7][8]={r:0,g: 0,b:0};
image[5][9]={r:0,g: 0,b:0};image[7][9]={r:0,g: 0,b:0};
image[9][5]={r:0,g: 0,b:0};image[10][5]={r:0,g: 0,b:0};image[11][5]={r:0,g: 0,b:0};
image[10][6]={r:0,g: 0,b:0};
image[10][7]={r:0,g: 0,b:0};
image[10][8]={r:0,g: 0,b:0};
image[10][9]={r:0,g: 0,b:0};
image[13][5]={r:0,g: 0,b:0};image[14][5]={r:0,g: 0,b:0};image[15][5]={r:0,g: 0,b:0};
image[13][6]={r:0,g: 0,b:0};
image[13][7]={r:0,g: 0,b:0};image[14][7]={r:0,g: 0,b:0};image[15][7]={r:0,g: 0,b:0};
image[13][8]={r:0,g: 0,b:0};
image[13][9]={r:0,g: 0,b:0};image[14][9]={r:0,g: 0,b:0};image[15][9]={r:0,g: 0,b:0};

//像素已矩形的形式渲染
//未来的优化: 对矩阵进行dfs, 合并相同的颜色
function getSvgFile(){
    file=``
    file +=`<?xml version='1.0' encoding='utf-8'?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" id="Layer_1" x="0px" y="0px" viewBox="0 0 400 400" enable-background="new 0 0 400 400"><rect x="0" y="0" width="399" height="399" style="fill:rgb(255,255,255) "/>`;
    for (let i = 0; i < 400; i++) {
        for (let j = 0; j < 400; j++) {
            if(image[i][j].r!=255 &&image[i][j].g!=255&&image[i][j].b!=255){
                file+=`<rect x="`+i+`" y="`+j+`" width="1" height="1" style="fill:rgb(`+image[i][j].r+`,`+image[i][j].g+`,`+image[i][j].b+`) "/>`
            }
        }
    }
    file +=`</svg>`
    return file;
}

setInterval(function () {
    let time = (new Date()).valueOf();
    
    fs.writeFile(path.join(__dirname, "../public/history/")+time+'.svg',getSvgFile(),function(error){
        if(error){
            console.log(error);
            return false;
        }
        newestImg = time;
        file=``
        console.log('创建图片'+time+'.svg');
    })
}, 1000*delay);//创建图片延迟


router.get('/', function (req, res) {
    console.log("sending svg")
    res.sendFile(path.join(__dirname, "../public/history/"+newestImg+".svg"))
    console.log("sent svg")
});

router.get('/changePixel', function (req, res) {
    let x = req.query.x;
    let y = req.query.y;
    let r = req.query.r;
    let g = req.query.g;
    let b = req.query.b;
    image[x][y] = {
        r: r,
        g: g,
        b: b
    }
    console.log("pixel changed"+"x:"+x+"y:"+y+"r:"+r+"g:"+g+"b:"+b)
    res.sendStatus(200);
})
module.exports = router;