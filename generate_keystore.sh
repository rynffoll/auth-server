#!/bin/bash

keytool \
    -genkeypair -alias auth_server \
    -keyalg RSA \
    -keypass secret \
    -keystore auth_server.jks \
    -storepass secret
