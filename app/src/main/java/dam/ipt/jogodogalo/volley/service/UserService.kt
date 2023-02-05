package dam.ipt.jogodogalo.volley.service

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import dam.ipt.jogodogalo.data.Session
import dam.ipt.jogodogalo.volley.VolleyRequest
import org.json.JSONArray
import org.json.JSONObject

class UserService(private val api: String)  {

    fun create(context: Context, response: Response.Listener<String>, responseError: Response.ErrorListener, jsonBody: JSONObject
    ) =
        VolleyRequest().newStringRequest(context, api, Request.Method.POST, response, responseError, jsonBody)


    fun update(context: Context, id: String, response: Response.Listener<String>, responseError: Response.ErrorListener, jsonBody: JSONObject
    ) =
        VolleyRequest().newStringRequest(context, "${api}/${id}", Request.Method.PUT, response, responseError, jsonBody)

}