const path = require('path');
const webpack = require('webpack');

const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    mode: 'development',
    entry: './web/index.js',

    output: {
        filename: '[name].[hash].js',
        path: path.resolve(__dirname, 'src/main/resources/assets')
    },

    plugins: [
        new webpack.ProgressPlugin(),
        new HtmlWebpackPlugin({
            template: 'web/index.html',
            inlineSource: '.(js|css)$'
        }),
    ],

    module: {
        rules: [
            {
                test: /.(js|jsx)$/,
                include: [path.resolve(__dirname, 'web')],
                loader: 'babel-loader',
                options: {
                    plugins: ['syntax-dynamic-import', '@babel/plugin-proposal-class-properties'],
                    presets: ['@babel/preset-env', '@babel/preset-react'],
                }
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ],
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                use: [
                    'file-loader',
                ],
            },
        ]
    },

    devServer: {
        open: true,
        hot: true
    }
};

