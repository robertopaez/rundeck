package org.rundeck.security.passwordutil.optionencrypter

import com.dtolabs.rundeck.core.encrypter.OptionEncryptor
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig


class DefaultOptionEncrypter implements OptionEncryptor{
    static String DEFAULT_ALGORITHM = "PBEWithMD5AndTripleDES"

    @Override
    String name() {
        return "DefaultOptionEncrypter"
    }

    @Override
    StandardPBEStringEncryptor getEncryptor() {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor()
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig()
        config.algorithm = DEFAULT_ALGORITHM
        config.password = getMasterPassword()

        encryptor.setConfig(config)

        return encryptor
    }

    static String getMasterPassword() {
        if(getMasterPasswordEnvironment()){
            return getMasterPasswordEnvironment()
        }else{
            return getMasterPasswordSysProperty()
        }

        return null
    }

    static getMasterPasswordEnvironment() {
        def env = "RD_OPTION_ENC_PASSWORD".toUpperCase()
        if (System.getenv(env)) {
            return System.getenv(env)
        }
        return null
    }

    static getMasterPasswordSysProperty() {
        def propName = "rd.option.enc.password"
        if (System.getProperty(propName)) {
            return System.getProperty(propName)
        }
        return null
    }


}
