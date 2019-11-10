const path = require('path');
const webpack = require('webpack');

const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    mode: 'development',
    entry: {
        index: './web/index.js',
        puzzle3: './web/puzzle3/index.js',
    },

    output: {
        filename: '[name].[hash].js',
        path: path.resolve(__dirname, 'src/main/resources/assets')
    },

    plugins: [
        new webpack.ProgressPlugin(),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'web/index.html',
            chunks: ['index'],
        }),
        new HtmlWebpackPlugin({
            filename: 'puzzle3.html',
            template: 'web/index.html',
            chunks: ['puzzle3'],
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

