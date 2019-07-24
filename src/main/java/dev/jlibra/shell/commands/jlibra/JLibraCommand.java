package dev.jlibra.shell.commands.jlibra;

import dev.jlibra.JLibra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class JLibraCommand {

    @Autowired
    private JLibra jlibra;

    @ShellMethod(key = {"config"}, value = "Display all configuration of jlibra instantiation.")
    public void display() {
        System.out.println();
        System.out.println(jlibra.getAdmissionControl());
        System.out.println("faucet           " + jlibra.getFaucetHost() + ":" + jlibra.getFaucetPort());
        System.out.println("gas-unit-price   " + jlibra.getGasUnitPrice());
        System.out.println("max-gas-amount   " + jlibra.getMaxGasAmount());
        System.out.println();
    }

}
