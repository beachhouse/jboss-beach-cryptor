Cryptor
=======

Cryptor is a small sample project with which text can be encrypted and decrypted using a key that is stored in an
outside wallet / vault.

1. Build and install jboss-beach-askpass version 0.1.0.
1. Install WildFly 8.0.0.Beta1.
1. Copy web/target/jboss-beach-cryptor-web-0.1.1-SNAPSHOT.war to standalone/deployments/
1. Set JBOSS_ASKPASS to ssh-askpass.
   ssh-askpass will be executed once to obtain the key with which to encrypt text.
1. Go to http://localhost:8080/cryptor/encrypt to encrypt some text.
1. Go to http://localhost:8080/cryptor/decrypt to decrypt the text.