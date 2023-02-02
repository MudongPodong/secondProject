const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  outputDir: "../src/main/resources/templates",
  devServer:{
    proxy:{
        '/': {
            target:'http://localhost:8080',
            changeOrigin: true
        }
    }
  },
  transpileDependencies: true
})
