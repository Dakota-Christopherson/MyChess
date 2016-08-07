/**
 * Created by Cody on 8/4/2016.
 */
//Modified from CSCI 1933 with Prof Dovolis, I believe this is also the standard way to write merge sorts

public class MergeSort {
    public static void merge(Move[] a, int start, int mid, int end, Move[] temp, Piece[][] board) {
        int ptr1 = start;
        int ptr2 = mid + 1;
        int resPtr = start;

        while (ptr1 <= mid && ptr2 <= end) {
            if (a[ptr1].getValue(board) >= a[ptr2].getValue(board))
                temp[resPtr++] = a[ptr1++];
            else temp[resPtr++] = a[ptr2++];
        }
        if (ptr1 <= mid)
            for (int i = ptr1; i <= mid; i++)
                temp[resPtr++] = a[i];
        else
            for (int i = ptr2; i <= end; i++)
                temp[resPtr++] = a[i];
        System.arraycopy(temp, start, a, start, end - start + 1);
    }

    public static void mergeSort(Move[] a, int start, int end, Move[] temp, Piece[][] board) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(a, start, mid, temp, board);
            mergeSort(a, mid + 1, end, temp, board);
            merge(a, start, mid, end, temp, board);
        }
    }

}
