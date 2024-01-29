const path = require("path");

module.exports = {
  entry: {
    libs: "./target/index.js",
  },
  output: {
    filename: "[name].js",
    path: path.resolve(__dirname, "public/js"),
  },
  resolve: {
    alias: {
      three: path.resolve(
        __dirname,
        "node_modules/three/build/three.module.js",
      ),
    },
  },
};
