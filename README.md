# Java Libra Client (jlibra Showcase) 

A simple showcase project for `jlibra` and `jlibra-spring-boot-starter`.  
Can be used standalone, but is more targeted for demostrating the usage of the library in custom Java applications.

### Prerequisites

* Java 8+
* Maven 3+ (optional)

### Use

You can [download the binaries](https://github.com/ice09/java-libra-client/releases/download/v0.0-alpha-2/java-libra-client-0.0-alpha-2.jar) and head to paragraph `Run`.  
If you want to build an own application based on `jlibra` and `jlibra-spring-boot-starter` see paragraph `Build`.

### Run

Run the downloaded binary with this command in a shell:
```
java -jar java-libra-client-0.0-alpha-2.jar
```

The application should start with a jlibra splash screen and a shell.  

_Note: As `jlibra-spring-boot-starter` is used, you can just drop a file `application.properties` next to the jar file. The configuration of the file will then be used in the shell session. See paragraph `Configuration` below._  
```
       _ ___ __
      (_) (_) /_  _________ _
     / / / / __ \/ ___/ __ `/    .: Connecting Libra to Java :.
    / / / / /_/ / /  / /_/ /
 __/ /_/_/_.___/_/   \__,_/
/___/

jlibra:>
```

Once the application starts up, type `help` to see all commands. These commands are identical to the Rust client commands.  
You can see the help for a specific command with `help 'command'`, eg. `help 'account create'`.
```
indra:>help 'account create'

NAME
	account create - Create a random account with private/public key pair. Account information will be held in memory only. The created account will not be saved to the chain.

SYNOPSYS
	account create 

ALSO KNOWN AS
	a c
	a create
	account c
```
Each command has a *ALSO KNOWN AS* section which shows aliases, so eg. `account create` can be abbreviated simply with `a c`.

### Configuration

There are several properties that can be configured. The current complete list is defined in the jlibra-spring-boot-starter project. 

```
jlibra.service-url=ac.testnet.libra.org
jlibra.service-port=8000
jlibra.faucet-url=faucet.testnet.libra.org
jlibra.faucet-port=80
jlibra.gas-unit-price=5
jlibra.max-gas-amount=600000
```

### Build

* Clone `jlibra` repo: `git clone -b api-demo https://github.com/ketola/jlibra.git`
* Build jlibra with `mvn clean install`
* Clone `jlibra-spring-boot-starter` repo: `git clone https://github.com/ice09/jlibra-spring-boot-starter`
* Build jlibra-spring-boot-starter with `mvn clean install`
* Clone `libra-message-signing` repo: `git clone https://github.com/ice09/libra-message-signing`
* Build libra-message-signing with `mvn clean install`
* Clone this repo: `git clone https://github.com/ice09/java-libra-shell.git`
* Build java-libra-shell with `mvn clean package`

### Run

From the parent directory, execute the following command to start the application:
```
java -jar target/java-libra-cli-1.0.0.jar
```

### References

* [jlibra](https://github.com/ketola/jlibra)
* [web3j](https://github.com/web3j/web3j)
* [web3j-spring-boot-starter](https://github.com/web3j/web3j-spring-boot-starter)
* [Spring Shell 2.0.0](https://docs.spring.io/spring-shell/docs/2.0.0.M2/reference/htmlsingle/#_getting_started) 
with [Spring Boot 2.0.0](https://docs.spring.io/spring-boot/docs/2.0.0.M5/reference/htmlsingle/) examples.
