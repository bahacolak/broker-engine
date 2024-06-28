package org.brokerengine.brokerservice.wallet.exception;

public class WalletNotFoundException extends Exception {
    public WalletNotFoundException() {
        super("Wallet not found!");
    }
}
