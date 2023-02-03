package dam.ipt.jogodogalo.volley.service

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import dam.ipt.jogodogalo.volley.VolleyRequest
import org.json.JSONObject

class AuthService(private val api: String) {

    fun login(context: Context, response: Response.Listener<String>, responseError: Response.ErrorListener, jsonBody: JSONObject
    ) =
        VolleyRequest().newStringRequest(context, api, Request.Method.POST, response, responseError, jsonBody)

}