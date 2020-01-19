package sample;
//BigInteger.probablePrime(160, new Random()) -> generate large prime nr.
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.Label;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller {
    BigInteger n, e, d;
    @FXML
    private TextArea plainText;
    @FXML
    private TextArea encryptedText;
    @FXML
    private TextField size;
    @FXML
    private TextArea privateKey;
    @FXML
    private TextArea publicKey;
    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid characters used!");


    public void pressDecrypt(ActionEvent event){
        String text = this.encryptedText.getText();//get text from field

        BigInteger cipher = new BigInteger(text);
        BigInteger decrypted = cipher.modPow(d,n);//decrypt using the formula
        //need to convert from ascii code to letters now.
        //we do so by getting the ASCII characters from the number 'decrypted' given by the computation
        String l = decrypted.toString();
        char[] finals = l.toCharArray();
        int i=0;
        char added;
        String finalDecrypted = "";
        while (i<finals.length){
            if (finals[i] == '1'){//We don't have any 2 digit ASCII codes starting from one, since [10-19] are not allowed ASCII codes, so it must be a 3 digit one
                added = (char)(Character.getNumericValue(finals[i])*100+Character.getNumericValue(finals[i+1])*10+Character.getNumericValue(finals[i+2]));
                finalDecrypted += added;
                i += 3;
            }
            else{//we need to form a letter from 2 digits
                added = (char)(Character.getNumericValue(finals[i])*10+Character.getNumericValue(finals[i+1]));
                finalDecrypted += added;
                i += 2;
            }
        }
        //System.out.println("finalDecryped: " + finalDecrypted);
        this.plainText.setText(finalDecrypted);
    }

    public void pressEncrypt(ActionEvent event){
        String plaintext = this.plainText.getText();
        if (!validate(plaintext))
            alert.show();
        else {
            //Convert each character from the plain text to it's ASCII code, and create a large number by appending them
            char[] c = plaintext.toCharArray();
            String s = "";
            for (int i = 0; i < c.length; i++) {
                s += (int) c[i];
            }
            BigInteger text = new BigInteger(s);//create a BigInteger from our String (String of ASCII codes)
            BigInteger cipher = text.modPow(e, n);//encrypt the number
            this.encryptedText.setText(cipher.toString());
        }
    }

    public void randomizeKey(ActionEvent event){
        int size = Integer.parseInt(this.size.getText());
        //probablePrime function generates a prime number (or has a VERY HIGH probability to do so)
        BigInteger p = BigInteger.probablePrime(size/2, new Random());
        BigInteger q = p.nextProbablePrime();
        BigInteger s = p.nextProbablePrime();
        this.n = p.multiply(q).multiply(s);//formula
        BigInteger fn = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));//(p-1)(q-1)
        e = BigInteger.probablePrime(fn.bitLength()-1,new Random());//public key

        while (! (fn.gcd(e)).equals(BigInteger.ONE) ) {
            e = BigInteger.probablePrime(fn.bitLength()-1,new Random());
        }// make sure that gcd(e, fn) = 1
        d = e.modInverse(fn);// private key. d = pow(e, -1)(mod fn)

        this.privateKey.setText(d.toString());
        this.publicKey.setText(e.toString());

    }

    public boolean validate(String s){
        char[] text = s.toCharArray();
        for (int i=0; i<text.length;i++){
            if ((int)text[i]<32 || (int)text[i]==127)//no ASCII codes lower than 32 (which is 'space')
                return false;
        }
        return true;
    }

    public void test(BigInteger e, BigInteger n, BigInteger d) {
        BigInteger text = new BigInteger(new Random().nextInt(n.bitLength()-1),new Random());
        BigInteger cipher = text.modPow(e,n);
        BigInteger decrypted = cipher.modPow(d,n);
        boolean isPassed = text.equals(decrypted);
        System.out.println("Test Passed? "+isPassed);

    }
}
