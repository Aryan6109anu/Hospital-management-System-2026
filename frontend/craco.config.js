module.exports = {
  webpack: {
    configure: (config) => {
      config.module.exprContextCritical = false;
      return config;
    }
  }
};
