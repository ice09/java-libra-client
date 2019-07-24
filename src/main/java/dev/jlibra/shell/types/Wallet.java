package dev.jlibra.shell.types;

import dev.jlibra.KeyUtils;
import dev.jlibra.mnemonic.*;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.ArrayList;

@Component
public class Wallet {

    private ArrayList<Account> accounts;

    public Wallet() {
        this.accounts = new ArrayList<>();
    }

    public void createAccount() {
        try {
            SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance("Ed25519", "BC");
            KeyPair keyPair = kpGen.generateKeyPair();
            BCEdDSAPrivateKey privateKey = (BCEdDSAPrivateKey) keyPair.getPrivate();
            BCEdDSAPublicKey publicKey = (BCEdDSAPublicKey) keyPair.getPublic();
            String libraAddress = new String(Hex.encode(digestSHA3.digest(KeyUtils.stripPublicKeyPrefix(publicKey.getEncoded()))));
            System.out.println();
            System.out.println("Created account with Libra address: " + libraAddress);
            System.out.println("Public key  " + new String(Hex.encode(publicKey.getEncoded())));
            System.out.println("Private key " + new String(Hex.encode(privateKey.getEncoded())));
            System.out.println();
            accounts.add(new GeneratedAccount(privateKey, publicKey));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot create account.", ex);
        }
    }

    public void createAccountFromMnemonic(String mnemonic, int child) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Seed seed = new Seed(Mnemonic.fromString(mnemonic), "LIBRA");
            LibraKeyFactory libraKeyFactory = new LibraKeyFactory(seed);
            ExtendedPrivKey extendedPrivKey = libraKeyFactory.privateChild(new ChildNumber(child));
            String libraAddress = extendedPrivKey.getAddress();
            System.out.println();
            System.out.println("Recovered account from mnemonic with Libra address: " + libraAddress);
            System.out.println("Public key  " + new String(Hex.encode(extendedPrivKey.publicKey.getData())));
            System.out.println("Private key " + new String(Hex.encode(extendedPrivKey.privateKey.getData())));
            System.out.println();
            accounts.add(new MnemonicAccount(extendedPrivKey));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot create account.", ex);
        }
    }

    public void listAccounts() {
        for (int i=0; i<accounts.size(); i++) {
            System.out.println();
            System.out.println("Account     " + i);
            System.out.println("Address     " + new String(Hex.encode(accounts.get(i).getAddress())));
            System.out.println("Public key  " + new String(Hex.encode(accounts.get(i).getPublicKey())));
            System.out.println("Private key " + new String(Hex.encode(accounts.get(i).getPrivateKey())));
        }
        System.out.println();
    }

    public Account getAccountAt(int index) {
        return accounts.get(index);
    }

    public String mint(int accountIndex, BigInteger amount, String url, int port) {
        long amountInMicroLibras = amount.longValue() * 1_000_000L;
        String toAddress = new String(Hex.encode(accounts.get(accountIndex).getAddress()));

        try {
            URL faucet = new URL(String.format("http://" + url + ":" + port + "?amount=%d&address=%s", amountInMicroLibras, toAddress));
            URLConnection uc = faucet.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
            return "OK";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
