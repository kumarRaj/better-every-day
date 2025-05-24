const createExpoWebpackConfigAsync = require('@expo/webpack-config');
const path = require('path');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = async function (env, argv) {
  const config = await createExpoWebpackConfigAsync({
    ...env,
    babel: {
      dangerouslyAddModulePathsToTranspile: ['nativewind'],
    },
  }, argv);

  // Remove the original HtmlWebpackPlugin
  const htmlPluginIndex = config.plugins.findIndex(
    plugin => plugin.constructor.name === 'HtmlWebpackPlugin'
  );

  if (htmlPluginIndex !== -1) {
    const originalHtmlPlugin = config.plugins[htmlPluginIndex];
    config.plugins.splice(htmlPluginIndex, 1);

    // Add a new HtmlWebpackPlugin with manifest link
    config.plugins.push(
      new HtmlWebpackPlugin({
        ...originalHtmlPlugin.options,
        template: originalHtmlPlugin.options.template,
        filename: originalHtmlPlugin.options.filename,
        inject: true,
        meta: {
          ...originalHtmlPlugin.options.meta,
          viewport: 'width=device-width, initial-scale=1, shrink-to-fit=no',
        },
        links: [
          { rel: 'manifest', href: '/manifest.json' }
        ]
      })
    );
  }

  config.plugins.push(
    new CleanWebpackPlugin(),
    new CopyWebpackPlugin({
      patterns: [
        {
          from: 'service-worker.js',
          to: 'service-worker.js'
        },
        {
          from: 'assets/images',
          to: 'assets/images'
        },
        {
          from: 'manifest.json',
          to: 'manifest.json'
        }
      ],
    })
  );

  return config;
};