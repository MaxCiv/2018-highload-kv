# Stage 3


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
Requests/sec:  17416.80
Transfer/sec:      1.13MB
```

#### GET 3/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   228.63us  805.13us  52.87ms   98.42%
    Req/Sec    10.43k     1.27k   12.20k    88.17%
  Latency Distribution
     50%  191.00us
     75%  233.00us
     90%  285.00us
     99%    1.18ms
  2489968 requests in 2.00m, 182.85MB read
  Socket errors: connect 0, read 1, write 0, timeout 0
  Non-2xx or 3xx responses: 622497
Requests/sec:  20749.12
Transfer/sec:      1.52MB
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
Requests/sec:  19856.11
Transfer/sec:      3.08MB
```

#### PUT/GET 2/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   217.94us  223.34us   7.69ms   94.89%
    Req/Sec     9.90k     1.16k   11.80k    71.32%
  Latency Distribution
     50%  183.00us
     75%  225.00us
     90%  298.00us
     99%    1.36ms
  2364873 requests in 2.00m, 173.66MB read
  Non-2xx or 3xx responses: 591218
Requests/sec:  19690.46
Transfer/sec:      1.45MB
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
Requests/sec:  21930.11
Transfer/sec:      1.80MB
```

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
Requests/sec:  20495.06
Transfer/sec:      1.51MB
```

## После оптимизации Cache
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
    Latency   239.94us    1.04ms  61.22ms   98.71%
    Req/Sec    10.82k     1.81k   12.59k    91.33%
  Latency Distribution
     50%  170.00us
     75%  204.00us
     90%  274.00us
     99%    1.35ms
  2584878 requests in 2.00m, 189.82MB read
Requests/sec:  21538.18
Transfer/sec:      1.58MB
```

#### PUT/GET 2/3
```
$ wrk --latency -c4 -d2m -s script.lua http://localhost:8080
Running 2m test @ http://localhost:8080
  2 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   208.42us  760.81us  62.00ms   97.83%
    Req/Sec    11.15k   583.12    12.29k    82.96%
  Latency Distribution
     50%  173.00us
     75%  202.00us
     90%  246.00us
     99%    1.26ms
  2662246 requests in 2.00m, 195.50MB read
Requests/sec:  22184.47
Transfer/sec:      1.63MB
```
