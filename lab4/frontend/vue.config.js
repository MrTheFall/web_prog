module.exports = {
    devServer: {
        proxy: {
            "/api": {
                target: "http://backend:8080",
                changeOrigin: true
            },
            "/ws": {
                target: "http://backend:9999",
                changeOrigin: true
            }
        },
        public: 'http://0.0.0.0:8080',
        https: false,
        allowedHosts: ['ubuntu']
    }
};