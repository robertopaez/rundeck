package com.dtolabs.rundeck.core.encrypter;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public interface OptionEncryptor {
    String name();
    StandardPBEStringEncryptor getEncryptor();


}