package org.rundeck.security.passwordutil.optionencrypter

import com.dtolabs.rundeck.core.encrypter.OptionEncryptor
import com.dtolabs.rundeck.core.encrypter.PasswordUtilityEncrypter;
import com.dtolabs.rundeck.core.plugins.configuration.Property;
import com.dtolabs.rundeck.core.plugins.configuration.PropertyUtil
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap;

class OptionValueUtilityEncrypter implements PasswordUtilityEncrypter {

    static private ConcurrentMap<String, StandardPBEStringEncryptor> map = new ConcurrentHashMap<>()

    private List<Property> formProperties = [
            PropertyUtil.string("valueToEncrypt","Value To Encrypt","The text you want to encrypt",true,null),
            PropertyUtil.string("provider","Encryptor Provider Class","Select the encryptor provider",true, "org.rundeck.security.passwordutil.optionencrypter.DefaultOptionEncrypter")
    ]

    @Override
    public List<Property> formProperties() {
        return formProperties;
    }

    @Override
    public String name() {
        return "Option Value Encrypter";
    }

    @Override
    public Map encrypt(Map params) {
        def result = [:]

        String valToEncrypt = params.get("valueToEncrypt")
        String provider = params.get("provider")

        def encryptor = getEncryptor(provider)
        if(!encryptor){
            result.error = "Encryptor not found"
        }else{
            String encryptedText = encryptor.encrypt(valToEncrypt)

            result.option  = "ENC('${encryptedText}')"
        }

        return result;
    }

    static String decrypt(String encryptedValue, String provider){
        def encryptor = getEncryptor(provider)
        try{
            return encryptor.decrypt(encryptedValue)
        }catch(Exception e){
            return null
        }
    }

    static StandardPBEStringEncryptor getEncryptor(String providerName){

        StandardPBEStringEncryptor encryptor = map.get(providerName)
        if(!encryptor){
            Class clazz = Class.forName(providerName);
            if(!OptionEncryptor.class.isAssignableFrom(clazz)) {
                return null
            }

            OptionEncryptor provider = (OptionEncryptor) clazz.newInstance();
            return provider.encryptor
        }
        return encryptor

    }
}
