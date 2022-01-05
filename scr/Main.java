public class Main {

    public static void main(String[] args) {


        for(int i=0; i<3; i++){
            Multithreading T = new Multithreading(i);
            T.start();
        }
    }
}