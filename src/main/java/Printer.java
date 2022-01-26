import java.util.Arrays;
import java.util.LinkedList;

public class Printer <T extends Job> {
    T thingToPrint;
    LinkedList<T> linkedList;
    T [] array;

    public void print(){
        System.out.println(thingToPrint);
    }

    public String T; String printList(LinkedList<T> list){
        this.linkedList = list;
        String[] str = new String[linkedList.size()];
        for(int i=0; i< linkedList.size(); i++){
            str[i] = linkedList.get(i).getName();
        }
        return Arrays.toString(str);
    }

    public String printArray(){
        String[] str = new String[this.array.length];
        for(int i=0; i< this.array.length; i++){
            str[i] = this.array[i].getName();
        }
        return Arrays.toString(str);
    }

}
