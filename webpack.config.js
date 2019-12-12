const path = require('path');
const webpack = require('webpack');

const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    mode: 'development',
    entry: {
        index: './web/index.js',
        blind: './web/blind/index.js',
        penultima: './web/penultima/index.js',
        phone: './web/phone/index.js',
        quidditch: './web/quidditch/index.js',
        sorting: './web/sorting/index.js',
        time: './web/time/index.js',
        pensieve: './web/pensieve/index.js',
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
            filename: 'blind.html',
            template: 'web/index.html',
            chunks: ['blind'],
        }),
        new HtmlWebpackPlugin({
            filename: 'penultima.html',
            template: 'web/index.html',
            chunks: ['penultima'],
        }),
        new HtmlWebpackPlugin({
            filename: 'phone.html',
            template: 'web/index.html',
            chunks: ['phone'],
        }),
        new HtmlWebpackPlugin({
            filename: 'quidditch.html',
            template: 'web/index.html',
            chunks: ['quidditch'],
        }),
        new HtmlWebpackPlugin({
            filename: 'sorting.html',
            template: 'web/index.html',
            chunks: ['sorting'],
        }),
        new HtmlWebpackPlugin({
            filename: 'time.html',
            template: 'web/index.html',
            chunks: ['time'],
        }),
        new HtmlWebpackPlugin({
            filename: 'pensieve.html',
            template: 'web/index.html',
            chunks: ['pensieve'],
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
                test: /\.(png|svg|jpg|gif|otf|mp3)$/,
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

