package dev.jlibra.shell.types;

import dev.jlibra.mnemonic.ExtendedPrivKey;
import org.bouncycastle.jcajce.provider.digest.SHA3;

public class MnemonicAccount extends Account {

    private final ExtendedPrivKey extendedPrivKey;

    public MnemonicAccount(ExtendedPrivKey extendedPrivKey) {
        this.accountType = AccountType.MNEMONIC;
        this.extendedPrivKey = extendedPrivKey;
    }

    @Override
    public byte[] getAddress() {
        SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest256();
        return digestSHA3.digest(extendedPrivKey.publicKey.getData());
    }

    @Override
    public byte[] getPublicKey() {
        return extendedPrivKey.publicKey.getData();
    }

    @Override
    public byte[] getPrivateKey() {
        return extendedPrivKey.privateKey.getData();
    }
}
