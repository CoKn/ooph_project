// package scr;

public class Multithreading extends Thread{

    /**
     * This class overrides the run method of the Class Thread to create multiple threads for parallel processing
     */

    private final int threadNumber;

    public Multithreading(int threadNumber){
        this.threadNumber = threadNumber;
    }

    @Override
    public void run(){
        for(int i=1; i<4; i++){
            try {
                Thread.sleep(1000);
                System.out.println("Thread " + threadNumber + " form loop round " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
