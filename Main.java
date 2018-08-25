import java.math.BigInteger;
import java.util.Random;


public class Main {
    public static void main(String[] args){

        //INIT
        int N = 5;  // This is the total number of shareholders
        int K = 3;  // This is the total number of shares to obtains the secret key

        BigInteger secret = new BigInteger("123");   // this is the our secret
        modLength = secret.bitLength() + 1;

     // Function Thats generate the random prime number
        BigInteger primeNum = genPrime();
        BigInteger[] coeff = new BigInteger[K-1];
        BigInteger[] partecipants = new BigInteger[K];
        for (int i=0;i<K;i++)
            partecipants[i] = new BigInteger(Integer.toString(i+1));
        System.out.println("Prime Number: "+primeNum);
        for (int i=0;i<K-1;i++){
            coeff[i] = randomZp(primeNum);
            System.out.println("a"+(i+1)+": "+coeff[i]);
        }

        //This portion describe the How to distributes the shares into shareholder
        BigInteger[] shares = new BigInteger[N];
        for(int i=0;i<N;i++){
            BigInteger toAdd= secret;
            for(int j=0;j<K-1;j++){
                BigInteger power = new BigInteger(Integer.toString((int)(Math.pow((i+1),(j+1)))));
                toAdd=toAdd.add(coeff[j].multiply(power));  
            }
            shares[i] = toAdd.mod(primeNum);
            System.out.println("Share n."+(i+1)+": "+shares[i]);
        }
        //This portion describe the Lagrange interpoltion to retrieve the original shares,
        BigInteger secret2= new BigInteger("0");
        double term;
        for (int i=0;i<K;i++){
            term = 1;
            BigInteger sharePartecipanteNow = shares[(partecipants[i].intValue())-1];
            for (int j=0;j<K;j++){
                if (partecipants[i].intValue()!=partecipants[j].intValue()){

                    BigInteger num = new BigInteger(Integer.toString(partecipants[j].intValue()*(-1)));
                    BigInteger den = new BigInteger(Integer.toString(partecipants[i].intValue()-partecipants[j].intValue()));
                    term = term*(num.doubleValue())/(den.doubleValue());
                }

            }
            term = term*sharePartecipanteNow.intValue();
            secret2 = secret2.add(new BigInteger(Integer.toString((int)term)));
        }
        while(secret2.intValue()<0)
            secret2 = secret2.add(primeNum);
        System.out.println("The secret is: "+secret2.mod(primeNum));
    }


// function to generate the prime number
    private static BigInteger genPrime() { 
        BigInteger p=null; 
        boolean ok=false; 
        do{
            p=BigInteger.probablePrime(modLength, new Random()); 
            if(p.isProbablePrime(CERTAINTY)) 
                ok=true; 
        }while(ok==false); 
        return p; 
    }


// function to generate random coefficients of the polynomial
    private static BigInteger randomZp(BigInteger p) { 
        BigInteger r; 
        do{
            r = new BigInteger(modLength, new Random()); 
        } while (r.compareTo(BigInteger.ZERO) < 0 || r.compareTo(p) >= 0); 
         return r; 
    }

    private static final int CERTAINTY = 50; 
    private static int modLength; 
}
