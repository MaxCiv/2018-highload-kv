## До оптимизаций

PUT 2/3
https://overload.yandex.net/153036

PUT 3/3
https://overload.yandex.net/153038

GET 2/3
https://overload.yandex.net/153039

GET 3/3
https://overload.yandex.net/153040

PUT/GET 2/3
https://overload.yandex.net/153044

PUT/GET 3/3
https://overload.yandex.net/153046

#### PUT 3/3
```
maxim.oleynik@WH0106244:~/Documents/myWrk$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   245.88us  223.96us  10.37ms   94.27%
    Req/Sec     8.75k   784.18    10.51k    75.88%
  Latency Distribution
     50%  234.00us
     75%  270.00us
     90%  334.00us
     99%    1.27ms
  2090158 requests in 2.00m, 135.55MB read
  Non-2xx or 3xx responses: 522540
Requests/sec:  17416.80
Transfer/sec:      1.13MB
```

#### GET 3/3
```
maxim.oleynik@WH0106244:~/Documents/myWrk$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   192.56us    0.99ms  70.91ms   99.38%
    Req/Sec    12.11k     1.09k   18.21k    91.21%
  Latency Distribution
     50%  166.00us
     75%  190.00us
     90%  232.00us
     99%    1.02ms
  2894049 requests in 2.00m, 711.38MB read
  Non-2xx or 3xx responses: 723513
Requests/sec:  24096.56
Transfer/sec:      5.92MB
```

#### PUT/GET 3/3
```
maxim.oleynik@WH0106244:~/Documents/myWrk$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   219.01us  261.38us  14.99ms   95.33%
    Req/Sec     9.98k     1.38k   11.29k    88.17%
  Latency Distribution
     50%  181.00us
     75%  242.00us
     90%  328.00us
     99%    1.15ms
  2382812 requests in 2.00m, 370.12MB read
  Non-2xx or 3xx responses: 595703
Requests/sec:  19856.11
Transfer/sec:      3.08MB
```

#### PUT/GET 2/3
```
maxim.oleynik@WH0106244:~/Documents/myWrk$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   212.05us  344.50us  20.14ms   95.38%
    Req/Sec    11.13k     3.21k   15.53k    61.87%
  Latency Distribution
     50%  172.00us
     75%  221.00us
     90%  334.00us
     99%    1.26ms
  2657909 requests in 2.00m, 412.85MB read
  Non-2xx or 3xx responses: 664477
Requests/sec:  22147.88
Transfer/sec:      3.44MB

```


## После оптимизаций
