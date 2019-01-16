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
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
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
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
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
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
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
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
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
#### PUT 3/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   239.11us  276.18us   5.39ms   93.49%
    Req/Sec     9.86k   527.68    10.86k    73.25%
  Latency Distribution
     50%  192.00us
     75%  227.00us
     90%  318.00us
     99%    1.52ms
  2354001 requests in 2.00m, 152.66MB read
  Non-2xx or 3xx responses: 588501
Requests/sec:  19613.72
Transfer/sec:      1.27MB
```

#### GET 3/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   189.64us  187.28us   9.26ms   96.69%
    Req/Sec    11.02k     2.12k   15.07k    57.54%
  Latency Distribution
     50%  159.00us
     75%  209.00us
     90%  266.00us
     99%    1.17ms
  2631728 requests in 2.00m, 215.84MB read
  Non-2xx or 3xx responses: 657933
Requests/sec:  21930.11
Transfer/sec:      1.80MB
```

#### PUT/GET 3/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   206.10us  238.30us  12.31ms   94.95%
    Req/Sec    10.93k   607.70    12.00k    75.33%
  Latency Distribution
     50%  176.00us
     75%  205.00us
     90%  254.00us
     99%    1.40ms
  2609506 requests in 2.00m, 191.62MB read
  Non-2xx or 3xx responses: 652377
Requests/sec:  21744.37
Transfer/sec:      1.60MB
```

#### PUT/GET 2/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   213.29us  250.24us   4.40ms   94.75%
    Req/Sec    10.72k   617.34    11.91k    74.75%
  Latency Distribution
     50%  178.00us
     75%  208.00us
     90%  268.00us
     99%    1.48ms
  2560374 requests in 2.00m, 188.02MB read
  Non-2xx or 3xx responses: 640094
Requests/sec:  21334.92
Transfer/sec:      1.57MB
```

## После оптимизации Future
#### PUT/GET 3/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   269.04us    1.36ms  71.15ms   99.48%
    Req/Sec     9.78k     1.39k   10.95k    92.62%
  Latency Distribution
     50%  179.00us
     75%  230.00us
     90%  311.00us
     99%    1.32ms
  2334600 requests in 2.00m, 171.44MB read
  Non-2xx or 3xx responses: 583654
Requests/sec:  19452.26
Transfer/sec:      1.43MB
```

#### PUT/GET 2/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   205.26us  191.52us  10.42ms   95.46%
    Req/Sec    10.30k   503.88    15.28k    87.46%
  Latency Distribution
     50%  173.00us
     75%  216.00us
     90%  287.00us
     99%    1.20ms
  2461512 requests in 2.00m, 180.76MB read
  Non-2xx or 3xx responses: 615379
Requests/sec:  20495.06
Transfer/sec:      1.51MB
```
