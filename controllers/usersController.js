const pool = require("../db")

const getAllUsers = (req, res) => {
    let query = req.query;
    
    pool.query(`SELECT * FROM users WHERE username='${query.username}';
                SELECT * FROM users;`).then(results => {
        if (results.rowCount == 0) {
            res.status(200).json("Acesso negado");
        } else {
            if (query.password === results[0].rows[0].password) {

                if (results[0].rows[0].role === "manager") {
                    res.status(200).json(results[0].rows.concat(results[1].rows.filter((user) => user.role !== "manager")));
                } else {
                    res.status(200).json("Acesso negado");
                }

            } else {
                res.status(200).json("O nome de utilizador ou palavra-passe está incorreto");
            }
            
        }
        
    }).catch(error => {
        res.status(500).json({result: "Ocorreu um erro ao tentar obter os utilizadores"});
        console.log(error);
    });

}

const createUser = (req, res) => {
    let body = req.body;

    if (body) {

        pool.query(`SELECT * FROM users WHERE username='${body.username}';`).then(results => {
            if (results.rowCount == 0) {
                pool.query(`INSERT INTO "users"("username", "password", "role") 
                    VALUES('${body.username}', '${body.password}', '${body.role}') RETURNING id;`).then(results => {
                    res.status(200).json(results.rows[0].id);
                }).catch(error => {
                    res.status(500).json("Ocorreu um erro ao tentar criar o utilizador");
                    console.log(error);
                });

            } else {
                res.status(200).json("Já existe um utilizador com esse nome");
            }
        }).catch(error => {
            res.status(500).json("Ocorreu um erro ao tentar criar o utilizador");
            console.log(error);
        });

        
    } else {
        res.status(400).json("Não existem dados para fazer a criação de um novo utilizador");
    }
}

const updateUser = (req, res) => {
    let body = req.body;
    let username = req.params.username;
    
    if (body) {
        pool.query(`UPDATE users SET username = '${body.username}', password = '${body.password}', role = '${body.role}' WHERE username='${username}'`).then(results => {
            if (results.rowCount == 1) {
                res.status(200).json("Utilizador Atualizado");
            } else {
                res.status(200).json("O utilizador já foi eliminado");
            }
        }).catch(error => {
            res.status(500).json("Ocorreu um erro ao tentar atualizar o utilizador");
            console.log(error);
        });
    } else {
        res.status(400).json("Não existem dados para fazer a atualização do utilizador");
    }
}

const deleteUser = (req, res) => {
    let username = req.params.username;

    pool.query(`DELETE FROM users WHERE username='${username}';`).then(results => {
        if (results.rowCount == 1) {
            res.status(200).json("Utilizador Eliminado");
        } else {
            res.status(200).json("O utilizador já foi eliminado");
        }
    }).catch(error => {
        res.status(500).json("Ocorreu um erro ao tentar eliminar o utilizador");
        console.log(error);
    });
}


module.exports = {
    getAllUsers,
    createUser,
    updateUser,
    deleteUser,
}
