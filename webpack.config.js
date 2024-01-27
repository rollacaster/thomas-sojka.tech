const path = require("path");

module.exports = {
  entry: {
    libs: "./target/index.js",
  },
  output: {
    filename: "[name].js",
    path: path.resolve(__dirname, "public/js"),
  },
};
