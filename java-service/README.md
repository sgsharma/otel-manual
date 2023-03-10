# Ad Service

The Ad service provides advertisement based on context keys. If no context keys
are provided then it returns random ads.

## Building Locally

The Ad service requires at least JDK 17 to build and uses gradlew to
compile/install/distribute. Gradle wrapper is already part of the source code.
To build Ad Service, run:

```sh
./gradlew installDist
```

It will create an executable script
`src/adservice/build/install/hipstershop/bin/AdService`.

To run the Ad Service:

```sh
export AD_SERVICE_PORT=8080
./build/install/hipstershop/bin/AdService
```

### Upgrading Gradle

If you need to upgrade the version of gradle then run

```sh
./gradlew wrapper --gradle-version <new-version>
```
### Test with grpcurl
This assumes you have `watch`, `parallel`, and `grpcurl` utilities installed (`brew install watch && brew install parallel && brew install grpcurl`)

```sh
watch -n 10 ./test.sh
```
