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
