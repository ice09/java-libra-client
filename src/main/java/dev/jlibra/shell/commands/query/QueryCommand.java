package dev.jlibra.shell.commands.query;

import dev.jlibra.admissioncontrol.query.UpdateToLatestLedgerResult;
import dev.jlibra.shell.types.Wallet;
import dev.jlibra.spring.action.AccountStateQuery;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;
import java.math.BigInteger;

@ShellComponent
public class QueryCommand {

    @Autowired
    private Wallet wallet;

    @Autowired
    private AccountStateQuery accountStateQuery;

    @ShellMethod(key = {"query balance", "query b", "q balance", "q b"}, value = "Get the current balance of an account.")
    public void balance(int accountAddressOrIndex) {
        UpdateToLatestLedgerResult result = accountStateQuery.queryBalance(wallet.getAccountAt(accountAddressOrIndex).getAddress());
        result.getAccountStates().forEach(accountState -> {
            System.out.println();
            System.out.println("Address                 " + new String(Hex.encode(accountState.getAddress())));
            System.out.println("Received events         " + accountState.getReceivedEvents());
            System.out.println("Sent events             " + accountState.getSentEvents());
            System.out.println("Balance (microLibras)   " + accountState.getBalanceInMicroLibras());
            System.out.println("Balance (Libras)        "
                    + new BigDecimal(accountState.getBalanceInMicroLibras()).divide(BigDecimal.valueOf(1000000)));
            System.out.println("Sequence number         " + accountState.getSequenceNumber());
            System.out.println();
        });
    }

    @ShellMethod(key = {"query sequence", "query s", "q sequence", "q s"}, value = "Get the current sequence number for an account.")
    public void sequence(int accountAddressOrIndex, boolean resetSequenceNumber) {
        balance(accountAddressOrIndex);
    }

    @ShellMethod(key = {"query account_state", "query as", "q account_state", "q as"}, value = "Get the latest state for an account.")
    public void accountState(int accountAddressOrIndex) {
        balance(accountAddressOrIndex);
    }

    @ShellMethod(key = {"query txn_acc_seq", "query ts", "q txn_acc_seq", "q ts"}, value = "Get the committed transaction by account and sequence number.")
    public void txnAccSeq(int accountAddressOrIndex, BigInteger sequenceNumber, @ShellOption(defaultValue = "false") boolean fetchEvents) {
        UpdateToLatestLedgerResult result = accountStateQuery.queryTransactionsBySequenceNumber(wallet.getAccountAt(accountAddressOrIndex).getAddress(), sequenceNumber.longValue());
        result.getAccountTransactionsBySequenceNumber().forEach(tx -> {
            System.out.println("Sender public key: " + new String(Hex.encode(tx.getSenderPublicKey())));
            System.out.println("Sender signature: " + new String(Hex.encode(tx.getSenderSignature())));

            tx.getEvents().forEach(e -> {
                System.out
                        .println(new String(Hex.encode(e.getAddress())) + " " + e.getEventPath().getEventType()
                                + " Amount: " + e.getAmount());
            });

        });
    }

    @ShellMethod(key = {"query txn_range", "query tr", "q txn_range", "q tr"}, value = "Get the committed transaction by range.")
    public void txnRange(BigInteger startVersion, BigInteger limit, @ShellOption(defaultValue = "false") boolean fetchEvents) {
        System.out.println("Currently not supported.");
    }

    @ShellMethod(key = {"query event", "query ev", "q event", "q ev"}, value = "Get event by account and path.")
    public void event(String accountAddress, String sentReceived, BigInteger startSequenceNumber, @ShellOption(defaultValue = "true") boolean ascending, BigInteger limit) {
        System.out.println("Currently not supported.");
    }

}
