package org.rundeck.security.passwordutil.optionencrypter

import spock.lang.Specification

class OptionValueUtilityEncrypterTest extends Specification{


    def "Test Encrypt"() {
        when:

        System.setProperty("rd.option.enc.password","masterKey")
        OptionValueUtilityEncrypter encrypter = new OptionValueUtilityEncrypter()

        Map params = [:]
        params.valueToEncrypt = "passwordTest"
        params.provider = "org.rundeck.security.passwordutil.optionencrypter.DefaultOptionEncrypter"

        def result = encrypter.encrypt(params)

        then:
        result["option"].contains("ENC(")
    }

}
