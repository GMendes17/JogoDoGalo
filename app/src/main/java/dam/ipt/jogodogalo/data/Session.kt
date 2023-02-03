package dam.ipt.jogodogalo.data

private lateinit var session: User

class Session() {

    fun getUser(): User {
        return session
    }

    fun setUser(id: Int, name: String, password: String, img: String) {
        session = User(id, name, password, img)
    }

}