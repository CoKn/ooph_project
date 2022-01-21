// package scr;

public class SortingAlgorithm {

    /**
     * Implements selection sort algorithm -> Explanation for documentation/presentation why do we use a selection sort
     * @param arr  Array that has to get sorted
     * @param ascending  sorting order ascending / descending
     */
    public static void selectionSort(Sortable[] arr, boolean ascending, String attribute){

        for(int i =0; i < arr.length; i++){


            //find min/max index
            int minMax = i;


            for(int j = i+1; j < arr.length; j++){
                if(ascending && arr[j].sortValue(attribute) < arr[minMax].sortValue(attribute)){
                    minMax = j;
                }else if(!ascending && arr[j].sortValue(attribute) > arr[minMax].sortValue(attribute)){
                    minMax = j;
                }
            }
            //swap min/max element in place
            swap(arr, minMax, i);
        }
    }


    private static void swap(Sortable[] arr, int n, int m){
        Sortable swap = arr[n];
        arr[n] = arr[m];
        arr[m] = swap;
    }

}
