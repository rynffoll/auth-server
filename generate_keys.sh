#!/bin/bash

keytool -list -rfc --keystore auth_server.jks | openssl x509 -inform pem -pubkey
