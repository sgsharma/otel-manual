#!/bin/sh

seq 1000 | parallel -n0 -j100 \
grpcurl -import-path ./bin/default/build/proto -proto demo.proto -plaintext localhost:8080 hipstershop.AdService/GetAds
