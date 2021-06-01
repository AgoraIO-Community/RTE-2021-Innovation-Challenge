<?php

return [
    'env' => 'ropsten',
    'ropsten' => [
        // 管理员地址
        'adminAddress' => '0xe46EF0abF04f602268C1198887dC1Bb2f51b778d',
        // GAMT地址
        'tokenAddress' => '0xbee0a3208cD53b24e60Fdf8012dd74Fdff3b2653',
        // 交易连接前缀
        'txLinkPrefix' => 'https://ropsten.etherscan.io/tx/',
        // 地址连接前缀
        'addressLinkPrefix' => 'https://ropsten.etherscan.io/address/'

    ],
    'goerli' => [
        // 管理员地址
        'adminAddress' => '0xe46EF0abF04f602268C1198887dC1Bb2f51b778d',
        // GAMT地址
        'tokenAddress' => '0xDfdD266b2fa2852C4fAA6706F851127E1DCB7e5a',
        // 交易连接前缀
        'txLinkPrefix' => 'https://goerli.etherscan.io/tx/',
        // 地址连接前缀
        'addressLinkPrefix' => 'https://goerli.etherscan.io/address/'
    ],
];
