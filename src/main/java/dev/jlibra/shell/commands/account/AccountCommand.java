package dev.jlibra.shell.commands.account;

import dev.jlibra.JLibra;
import dev.jlibra.shell.types.Wallet;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigInteger;
import java.nio.file.Path;

@ShellComponent
public class AccountCommand {

    @Autowired
    private Wallet wallet;

    @Autowired
    private JLibra jLibra;

    @ShellMethod(key = {"account create", "account c", "a create", "a c"}, value = "Create a random account with private/public key pair. Account information will be held in memory only. The created account will not be saved to the chain.")
    public void create() {
        wallet.createAccount();
    }

    @ShellMethod(key = {"account create_with_mnemonic", "account cwm", "a create_with_mnemonic", "a cwm"}, value = "Recover an account from a mnemonic and an optional child index. Account information will be held in memory only. The created account will not be saved to the chain.")
    public void createWithMnemonic(String mnemonic, @ShellOption(defaultValue = "0") int child) {
        //legal winner thank year wave sausage worth useful legal winner thank year wave sausage worth useful legal will
        wallet.createAccountFromMnemonic(mnemonic, child);
    }

    @ShellMethod(key = {"account list", "account ls", "a list", "a ls"}, value = "Print all accounts that were created or loaded.")
    public void list() {
        wallet.listAccounts();
    }

    @ShellMethod(key = {"account recover", "account r", "a recover", "a r"}, value = "Recover all accounts that were written to a file via the account write command.")
    public void recover(Path recoveryPath) {
        System.out.println("Not supported. Please use 'account create_with_mnemonic' or 'a cwm' instead.");
    }

    @ShellMethod(key = {"account write", "account w", "a write", "a w"}, value = "Save Libra wallet mnemonic recovery seed to disk. This will allow accounts to be recovered via account recover.")
    public void write(String mnemonic, Path recoveryPath) {
        System.out.println("Not supported. Please use 'account create_with_mnemonic' or 'a cwm' instead.");
    }

    @ShellMethod(key = {"account mint", "account m", "a mint", "a m"}, value = "Mint coins to the account.")
    public void mint(String addressOrIndex, BigInteger amount) {
        System.out.println(wallet.mint(Hex.toHexString(wallet.findLibraAccount(addressOrIndex)), amount, jLibra.getFaucetHost(), jLibra.getFaucetPort()));
    }

    @ShellMethod(key = {"account mintb", "account mb", "a mintb", "a mb"}, value = "Mint coins to the account. Blocking.")
    public void mintBlocking(String addressOrIndex, BigInteger amount) {
        mint(addressOrIndex, amount);
    }}
