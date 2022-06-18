#!/bin/bash

export DEBIAN_FRONTEND=noninteractive
apt -y install openssl
openssl x509 -in mongo-ca-certificate.crt -inform pem -out mongo-ca-certificate.der -outform der
keytool -importcert -noprompt -alias startssl -keystore "$JAVA_HOME/lib/security/cacerts" -storepass changeit -file mongo-ca-certificate.der