package dam.ipt.jogodogalo.volley

import android.content.Context
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import dam.ipt.jogodogalo.volley.service.AuthService
import dam.ipt.jogodogalo.volley.service.UserService
import org.json.JSONArray
import org.json.JSONObject

class VolleyRequest() {

	//vars para conectar ao api
	private val api = "https://jogodogalo-production.up.railway.app"

	private val apiLoginUrl = "${api}/auth"
	fun Auth(): AuthService = AuthService(apiLoginUrl)

	private val apiUsersUrl = "${api}/users"
	fun User(): UserService = UserService(apiUsersUrl)

	fun newStringRequest(context: Context, url: String, method: Int, response: Response.Listener<String>, responseError: Response.ErrorListener,
			jsonBody: JSONObject? = null) {

		val jsonObjectRequest: StringRequest

		if(jsonBody == null){
			jsonObjectRequest = StringRequest(
				method, url, response, responseError
			)
		}else{
			jsonObjectRequest = object : StringRequest(
				method, url, response, responseError
			) {
				override fun getBodyContentType(): String {
					return "application/json; charset=utf-8"
				}
				//carregar as variaveis que pretendemos enviar para o API no body do pedido
				override fun getBody(): ByteArray {
					return jsonBody.toString().toByteArray()
				}

			}
		}

		//Adicionar o pedido à fila
		Volley.newRequestQueue(context).add(jsonObjectRequest)
	}

}