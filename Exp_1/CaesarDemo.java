import java.util.Scanner;

class CaesarCipher {
    private final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public String encrypt(String plainText,int shiftKey) {
        plainText = plainText.toLowerCase();
        String cipherText="";
        for(int i=0;i<plainText.length();i++) {
            int charPosition = ALPHABET.indexOf(plainText.charAt(i));
            int keyVal = (shiftKey+charPosition)%26;
            char replaceVal = this.ALPHABET.charAt(keyVal);
            cipherText += replaceVal;
        }
        return cipherText;
    }
    public String decrypt(String cipherText, int shiftKey) {
        cipherText = cipherText.toLowerCase();
        String plainText="";
        for(int i=0;i<cipherText.length();i++) {
            int charPosition = this.ALPHABET.indexOf(cipherText.charAt(i));
            int keyVal = (charPosition-shiftKey)%26;
            if(keyVal<0) {
                keyVal = this.ALPHABET.length() + keyVal;
            }
            char replaceVal = this.ALPHABET.charAt(keyVal);
            plainText += replaceVal;
        }
        return plainText;
    }
}

class CaesarDemo {
    public static void main(String args[]) {
        boolean flag=true;
        Scanner s=new Scanner(System.in);
        while(flag){
            System.out.print("1 for Encryption\n2 for Decryption\n3 for Exit\nEnter an option: ");
            int op=s.nextInt();
            switch (op) {
                case 1:
                    System.out.print("Enter a plain text: ");
                    String plainTextE=s.next();
                    int shiftKey;
                    CaesarCipher cce = new CaesarCipher();
                    for(shiftKey=1;shiftKey<26;shiftKey++){
                        String cipherText = cce.encrypt(plainTextE,shiftKey);
                        System.out.println("Key " + shiftKey + ": " + cipherText);
                    }
                    System.out.println();
                    break;
                case 2:
                    System.out.print("Enter a cipher text: ");
                    String cipherText=s.next();
                    int ciphershiftKey;
                    CaesarCipher ccd = new CaesarCipher();
                    for(ciphershiftKey=1;ciphershiftKey<26;ciphershiftKey++){
                        String plainTextD = ccd.decrypt(cipherText,ciphershiftKey);
                        System.out.println("Key " + ciphershiftKey + ": " + plainTextD);
                    }
                    System.out.println();
                    break;
                case 3:
                    flag=false;
                    break;
                default:
                    System.out.println("Invalid\n");
                    break;
            }
        }
        s.close();
    }
}
