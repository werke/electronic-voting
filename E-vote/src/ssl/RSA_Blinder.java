package ssl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSA_Blinder {

    private RSAPublicKey pubKey;
    private BigInteger r, e, n;

    public RSA_Blinder(RSAPublicKey pubKey) throws Exception {
        this.pubKey = pubKey;
        this.n = pubKey.getModulus();
        this.e = pubKey.getPublicExponent();
        regenerateRandom();
    }

    public void regenerateRandom() throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG","SUN");
        byte [] randomBytes = new byte[10];
        BigInteger gcd = null;
        BigInteger one = new BigInteger("1");
        do {
            random.nextBytes(randomBytes);
            r = new BigInteger(1, randomBytes);
            gcd = r.gcd(n);
        }
        while(!gcd.equals(one) || r.compareTo(n)>=0 || r.compareTo(one)<=0);
    }

    public byte [] blind(byte [] raw) 
            throws Exception
    {
        BigInteger m = new BigInteger(raw);
        BigInteger b = ((r.modPow(e,n)).multiply(m)).mod(n);

        return b.toByteArray();
    }

    public byte [] unblind(byte [] raw) {
        BigInteger b = new BigInteger(raw);
        BigInteger s = r.modInverse(n).multiply(b).mod(n);
        
        return s.toByteArray();
    }

    public static byte [] sign(byte [] raw, RSAPrivateKey privKey) {
        BigInteger d = privKey.getPrivateExponent();
        BigInteger n = privKey.getModulus();
        BigInteger b = new BigInteger(raw);
        BigInteger bs = b.modPow(d,n);

        return bs.toByteArray();
    }

    public static byte [] unsign(byte [] raw, RSAPublicKey privKey) {
        BigInteger d = privKey.getPublicExponent();
        BigInteger n = privKey.getModulus();
        BigInteger b = new BigInteger(raw);
        BigInteger bs = b.modPow(d,n);

        return bs.toByteArray();
    }
}
