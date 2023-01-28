const settings = require('./config.json');
const Pool = require('pg').Pool;

const pool = new Pool({
    user: process.env.PGUSER || settings.PostgreSQL.user,
    host: process.env.PGHOST || settings.PostgreSQL.host,
    database: process.env.PGDATABASE || settings.PostgreSQL.database,
    password: process.env.PGPASSWORD || settings.PostgreSQL.password,
    port: process.env.PGPORT || settings.PostgreSQL.port,
    ssl: { rejectUnauthorized: false },
})



module.exports = pool;