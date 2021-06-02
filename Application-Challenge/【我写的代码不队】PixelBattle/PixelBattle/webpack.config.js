const path = require('path');

module.exports = {
entry: './public/javascripts/main.js',
//mode: 'development',
resolve: {
    alias: {
        vue: 'vue/dist/vue.js'
    },
},
output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, './public/javascripts'),
},
};