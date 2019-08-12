package dev.jlibra.shell.commands.message;

import dev.jlibra.extension.signer.Signer;
import dev.jlibra.extension.verifier.Verifier;
import dev.jlibra.shell.types.Account;
import dev.jlibra.shell.types.Wallet;
import dev.jlibra.util.ExtKeyUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;

@ShellComponent
public class MessageCommand {

    @Autowired
    private Wallet wallet;

    @ShellMethod(key = {"sign message", "sign m", "s message", "s m"}, value = "Sign the message with the private key of provided account.")
    public void signMessage(String message, int index) {
        Account account = wallet.getAccountAt(index);
        PrivateKey pk = ExtKeyUtils.privateKeyFromBytes(account.getPrivateKey());
        byte[] pkb = new byte[32];
        System.arraycopy(pk.getEncoded(), 16, pkb, 0, 32);
        System.out.println("\nSigned message is " + Hex.toHexString(Signer.signMessage(pkb, message.getBytes(Charset.forName("UTF-8")))) + "\n");
    }

    @ShellMethod(key = {"verify message", "verify m", "v message", "v m"}, value = "Verify that the message was signed with provided public key or account.")
    public void verifyMessage(String message, String signature, String publicKeyOrIndex) {
        byte[] encodedPk;
        int srcPos = 12;
        if (publicKeyOrIndex.length() <= 12) {
            Account account = wallet.getAccountAt(Integer.parseInt(publicKeyOrIndex));
            PublicKey puk = ExtKeyUtils.publicKeyFromBytes(account.getPublicKey());
            encodedPk = puk.getEncoded();
        } else {
            encodedPk = Hex.decode(publicKeyOrIndex);
            if (encodedPk.length == 32) {
                srcPos = 0;
            }
        }
        byte[] pukb = new byte[32];
        System.arraycopy(encodedPk, srcPos, pukb, 0, 32);
        boolean wasSigned = Verifier.verifyMessage(pukb, message.getBytes(Charset.forName("UTF-8")), Hex.decode(signature));
        System.out.println("\nMessage was" + (wasSigned ? "" : " NOT") + " signed by provided public key.\n");
    }

}
