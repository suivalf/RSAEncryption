# RSAEncryption
🔒 Message encryption using the RSA algorithm. Implemented in Java. Using JavaFX for GUI.

RSA (Rivest–Shamir–Adleman) cryptosystem uses a public key for encrypting a message 
and a private key for decrypting it.

1. Generate 2 (large) distinct prime numbers: p and q.
2. Computes n = p*q
3. Computes ϕ(n) = (p-1)(q-1)
4. Select e between 1 and ϕ(n), having the property gcd(e, ϕ(n)) = 1.
5. Computes d*e = mod(ϕ(n)).
The public key is hte pair (n, e), while the private key is the pair (n, d)

How it looks: https://imgur.com/zATCurMS
