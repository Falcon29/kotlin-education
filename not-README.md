achuprov@achuprov-HP-ProBook-460:~$ curl -X POST http://0.0.0.0:8080/v2/ticket/get -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 319
Content-Type: application/json

{"result":"success","errors":[{"code":"unknown","group":"exceptions","message":"Cannot transform this request's content to org.kotlined.cc.api.v2.models.TicketGetRequest"},{"code":"system-dbNotConfigured","group":"system","message":"System error occurred. Our stuff has been informed, please retry later"}],"ticket":{}}Status Code: 200
achuprov@achuprov-HP-ProBook-460:~$ curl -X POST http://0.0.0.0:8080/v2/ticket/list -i -w "Status Code: %{http_code}\n"
HTTP/1.1 500 Internal Server Error
Vary: Origin
Content-Length: 0

Status Code: 500
achuprov@achuprov-HP-ProBook-460:~$ curl -X POST http://0.0.0.0:8080/v2/ticket/create -i -w "Status Code: %{http_code}\n"
HTTP/1.1 500 Internal Server Error
Vary: Origin
Content-Length: 0


achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ curl -X POST http://0.0.0.0:8080/v2/ticket/create -H "Content-Type: application/json" -d '{"requestType":"create","debug":null,"ticket":{"title":"Where is my mind?","description":"Wheeeresmymind","priority":"medium"}}' -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 201
Content-Type: application/json

{"responseType":"create","result":"success","errors":[{"code":"system-dbNotConfigured","group":"system","message":"System error occurred. Our stuff has been informed, please retry later"}],"ticket":{}}Status Code: 200

achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ curl -X POST http://0.0.0.0:8080/v2/ticket/create -H "Content-Type: application/json" -d '{"requestType":"create","debug":null,"ticket":{"title":"Where is my mind?","description":"Wheeeresmymind","priority":"medium"}}' -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 217
Content-Type: application/json

{"responseType":"create","result":"success","ticket":{"id":"2e559b0f-01b1-4aa6-8cad-b21ab1aabf36","title":"Where is my mind?","description":"Wheeeresmymind","status":"new","clientId":"client-001","priority":"medium"}}Status Code: 200
achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ curl -X POST http://0.0.0.0:8080/v2/ticket/create -H "Content-Type: application/json" -
d '{"requestType":"get","debug":null,"ticket":{"id":"2e559b0f-01b1-4aa6-8cad-b21ab1aabf36"}' -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 206
Content-Type: application/json

{"responseType":"get","result":"success","errors":[{"code":"unknown","group":"exceptions","message":"Failed to convert request body to class org.kotlined.cc.api.v2.models.TicketCreateRequest"}],"ticket":{}}Status Code: 200
achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ curl -X POST http://0.0.0.0:8080/v2/ticket/create -H "Content-Type: application/json" -d '{"requestType":"get","debug":null,{"id":"2e559b0f-01b1-4aa6-8cad-b21ab1aabf36"}' -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 206
Content-Type: application/json

{"responseType":"get","result":"success","errors":[{"code":"unknown","group":"exceptions","message":"Failed to convert request body to class org.kotlined.cc.api.v2.models.TicketCreateRequest"}],"ticket":{}}Status Code: 200
achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ curl -X POST http://0.0.0.0:8080/v2/ticket/get -H "Content-Type: application/json" -d '{"requestType":"get","debug":null,"ticket":{"id":"2e559b0f-01b1-4aa6-8cad-b21ab1aabf36"}' -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 203
Content-Type: application/json

{"responseType":"get","result":"success","errors":[{"code":"unknown","group":"exceptions","message":"Failed to convert request body to class org.kotlined.cc.api.v2.models.TicketGetRequest"}],"ticket":{}}Status Code: 200
achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ curl -X POST http://0.0.0.0:8080/v2/ticket/get -H "Content-Type: application/json" -d '{"requestType":"get","debug":null,"id":"2e559b0f-01b1-4aa6-8cad-b21ab1aabf36"}' -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 221
Content-Type: application/json

{"responseType":"get","result":"success","errors":[{"code":"validation-id-badFormat","group":"validation","field":"id","message":"Validation error for field id: value  must contain only letters and numbers"}],"ticket":{}}Status Code: 200


achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ curl -X POST http://0.0.0.0:8080/v2/ticket/create -H "Content-Type: application/json" -d '{"requestType":"create","requestType":"create","debug":null,"ticket":{"title":"Where is my mind?","description":"Wheeeresmymind","priority":"medium"}}' -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 217
Content-Type: application/json

{"responseType":"create","result":"success","ticket":{"id":"4eaa8475-fb66-4e6e-b073-70cf9b33559d","title":"Where is my mind?","description":"Wheeeresmymind","status":"new","clientId":"client-001","priority":"medium"}}Status Code: 200
achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ curl -X POST http://0.0.0.0:8080/v2/ticket/get -H "Content-Type: application/json" -d '                                                                                                                                                           {"requestType":"get","debug":null,"ticket":{"id":"4eaa8475-fb66-4e6e-b073-70cf9b33559d"}}' -i -w "Status Code: %{http_code}\n"
HTTP/1.1 200 OK
Vary: Origin
Content-Length: 292
Content-Type: application/json

{"responseType":"get","result":"success","ticket":{"id":"4eaa8475-fb66-4e6e-b073-70cf9b33559d","title":"Where is my mind?","description":"Wheeeresmymind","status":"new","clientId":"4eaa8475-fb66-4e6e-b073-70cf9b33559d","operatorId":"4eaa8475-fb66-4e6e-b073-70cf9b33559d","priority":"medium"}}Status Code: 200
achuprov@achuprov-HP-ProBook-460:~/work/education/kotlin-education$ 
