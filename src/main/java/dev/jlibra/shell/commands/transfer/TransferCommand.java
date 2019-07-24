package dev.jlibra.shell.commands.transfer;

import dev.jlibra.mnemonic.ExtendedPrivKey;
import dev.jlibra.mnemonic.SecretKey;
import dev.jlibra.shell.types.Account;
import dev.jlibra.shell.types.AccountType;
import dev.jlibra.shell.types.Wallet;
import dev.jlibra.shell.util.KeyUtils;
import dev.jlibra.spring.action.PeerToPeerTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigInteger;

@ShellComponent
public class TransferCommand {

    @Autowired
    private Wallet wallet;

    @Autowired
    private PeerToPeerTransfer peerToPeerTransfer;

    @ShellMethod(key = {"transfer", "t"}, value = "Transfer coins from one account to another.")
    public void transfer(String senderAddressOrIndex, String receiverAddress, BigInteger amount, @ShellOption(defaultValue = "-1") BigInteger gasUnitPrice, @ShellOption(defaultValue = "-1") BigInteger maxGasAmount) {
        Account senderAccount = wallet.getAccountAt(Integer.parseInt(senderAddressOrIndex));
        PeerToPeerTransfer.PeerToPeerTransferReceipt receipt;
        if (senderAccount.getAccountType() == AccountType.GENERATED) {
            receipt = peerToPeerTransfer.transferFunds(receiverAddress, amount.longValue() * 1_000_000, KeyUtils.publicKeyFromBytes(senderAccount.getPublicKey()), KeyUtils.privateKeyFromBytes(senderAccount.getPrivateKey()), gasUnitPrice.longValue(), maxGasAmount.longValue());
        } else {
            receipt = peerToPeerTransfer.transferFunds(receiverAddress, amount.longValue() * 1_000_000, new ExtendedPrivKey(new SecretKey(senderAccount.getPrivateKey())), gasUnitPrice.longValue(), maxGasAmount.longValue());
        }
        System.out.println(receipt.getStatus());
    }

    @ShellMethod(key = {"transferb", "tb"}, value = "Transfer coins from one account to another. Blocking.")
    public void transferBlocking(String senderAddressOrIndex, String receiverAddress, BigInteger amount, @ShellOption(defaultValue = "0") BigInteger gasUnitPrice, @ShellOption(defaultValue = "10000") BigInteger maxGasAmount) {
        transfer(senderAddressOrIndex, receiverAddress, amount, gasUnitPrice, maxGasAmount);
    }

}
