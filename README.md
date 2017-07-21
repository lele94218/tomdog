
TOMDOG -- a Lightweight Java Web Container
----

<img src="http://fanaru.com/doge/image/218965-doge-doge.jpg" width="450">

<pre>
 _                      _             
| |                    | |            
| |_ ___  _ __ ___   __| | ___   __ _ 
| __/ _ \| '_ ` _ \ / _` |/ _ \ / _` |
| || (_) | | | | | | (_| | (_) | (_| |
 \__\___/|_| |_| |_|\__,_|\___/ \__, |
                                 __/ |
                                |___/ 
____________________________ Version 0.1.1 __                              
</pre>

Update log
----

1. Processor pool binding thread with each new processor

```
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: I/O exception (java.net.SocketException) caught when processing request to {}->http://localhost:8080: Connection reset by peer
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: Retrying request to {}->http://localhost:8080
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: I/O exception (java.net.SocketException) caught when processing request to {}->http://localhost:8080: Connection reset by peer
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: I/O exception (java.net.SocketException) caught when processing request to {}->http://localhost:8080: Broken pipe
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: Retrying request to {}->http://localhost:8080
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: Retrying request to {}->http://localhost:8080
HTTP/1.1 200 OK
HTTP/1.1 200 OK
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: I/O exception (java.net.SocketException) caught when processing request to {}->http://localhost:8080: Broken pipe
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: Retrying request to {}->http://localhost:8080
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: I/O exception (java.net.SocketException) caught when processing request to {}->http://localhost:8080: Broken pipe
Jul 21, 2017 8:50:45 AM org.apache.http.impl.execchain.RetryExec execute
INFO: Retrying request to {}->http://localhost:8080
HTTP/1.1 200 OK
HTTP/1.1 200 OK
```

```
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
HTTP/1.1 200 OK
```

References
1. [How tomcat works](https://www.amazon.com/How-Tomcat-Works-Budi-Kurniawan/dp/097521280X)