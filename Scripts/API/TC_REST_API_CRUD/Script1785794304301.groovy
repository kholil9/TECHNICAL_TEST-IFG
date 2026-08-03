// ================= TEST REST API M KHOLIL RAHIMULLAH=================

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

/*
 * Skenario:
 * Katalon berperan sebagai PRODUCER -> mengirim (POST) data baru ke REST API
 * Katalon berperan sebagai CONSUMER -> menerima & memvalidasi (GET) data dari REST API
 * API publik yang dipakai: https://jsonplaceholder.typicode.com (tidak butuh auth, aman untuk demo)
 */

String baseUrl = 'https://jsonplaceholder.typicode.com'

// ================= 1. PRODUCER: kirim POST request (create resource) =================
RequestObject postRequest = new RequestObject('POST_CreatePost')
postRequest.setRestUrl(baseUrl + '/posts')
postRequest.setRestRequestMethod('POST')
postRequest.setHttpHeaderProperties([
        new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
])
String requestBody = JsonOutput.toJson([
        title : 'Katalon Test',
        body  : 'Testing RESTful API - Katalon sebagai producer',
        userId: 1
])
postRequest.setBodyContent(requestBody)
postRequest.setBodyText(requestBody, 'application/json')

ResponseObject postResponse = WS.sendRequest(postRequest)
WS.verifyResponseStatusCode(postResponse, 201)

def createdData = new JsonSlurper().parseText(postResponse.getResponseText())
assert createdData.title == 'Katalon Test'
println 'PRODUCER OK - data berhasil dikirim, id baru: ' + createdData.id

// ================= 2. CONSUMER: kirim GET request & validasi response =================
RequestObject getRequest = new RequestObject('GET_Post')
getRequest.setRestUrl(baseUrl + '/posts/1')
getRequest.setRestRequestMethod('GET')

ResponseObject getResponse = WS.sendRequest(getRequest)
WS.verifyResponseStatusCode(getResponse, 200)

def fetchedData = new JsonSlurper().parseText(getResponse.getResponseText())
assert fetchedData.id == 1
assert fetchedData.userId != null
println 'CONSUMER OK - data berhasil diterima & divalidasi: ' + fetchedData

// ================= 3. Tambahan: PUT (update) & DELETE untuk cakupan lebih lengkap =================
RequestObject putRequest = new RequestObject('PUT_UpdatePost')
putRequest.setRestUrl(baseUrl + '/posts/1')
putRequest.setRestRequestMethod('PUT')
putRequest.setHttpHeaderProperties([
        new TestObjectProperty('Content-Type', ConditionType.EQUALS, 'application/json')
])
String putBody = JsonOutput.toJson([id: 1, title: 'Updated by Katalon', body: 'Updated body', userId: 1])
putRequest.setBodyContent(putBody)
putRequest.setBodyText(putBody, 'application/json')

ResponseObject putResponse = WS.sendRequest(putRequest)
WS.verifyResponseStatusCode(putResponse, 200)
println 'UPDATE OK'

RequestObject deleteRequest = new RequestObject('DELETE_Post')
deleteRequest.setRestUrl(baseUrl + '/posts/1')
deleteRequest.setRestRequestMethod('DELETE')

ResponseObject deleteResponse = WS.sendRequest(deleteRequest)
WS.verifyResponseStatusCode(deleteResponse, 200)
println 'DELETE OK'
