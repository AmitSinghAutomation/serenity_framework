package dryRun;

import java.util.*;

public class checkOutput {

    public static void main (String [] args)
    {
        int [] arr = {9,8,7,6,5,7,8,9};

        Arrays.sort(arr);

        System.out.println("Sorted array"+Arrays.toString(arr));

        int count;
        for(int i = 0; i < arr.length; i++)
        {
          count = 1;
          while ((i < arr.length - 1) && (arr[i] == arr[i+1]))
          {
            count++;
            i++;
          }
          if(count > 1)
          {
              System.out.println(arr[i]+" appears "+count+ " times");
          }
        }
        int uniqueIndex = 0;
        for(int i = 0; i < arr.length - 1; i++)
        {
           if (arr[i] != arr[i+1])
           {
             arr[uniqueIndex++] = arr[i];
           }
        }
        arr[uniqueIndex++] = arr[arr.length - 1];

        int [] uniqueArray = Arrays.copyOf(arr,uniqueIndex);
        System.out.println("Unique array "+Arrays.toString(uniqueArray));
    }
}
