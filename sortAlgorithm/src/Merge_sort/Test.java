package Merge_sort;

import java.lang.reflect.Array;



public class Test<T extends Comparable> {
	public void sort(T[] array){
		sort(array,0,array.length-1);
	}
	public void sort(T[] array,int low,int high){
		int mid=(low+high)/2;
		if(low<high){
			sort(array,low,mid);
			sort(array,mid+1,high);
			merge(array,low,mid,high);
		}
	}
	private void merge(T[] array, int low, int mid, int high) {
		T[] temp=(T[]) Array.newInstance(Comparable.class, high-low+1);
		int i=low;
		int j=mid+1;
		int k=0;
		
		while(i<=mid&&j<=high){
			if( array[i].compareTo(array[j])<0){
				temp[k++]=array[i++];
			}else{
				temp[k++]=array[j++];
			}
		}
		
		while(i<=mid){
			temp[k++]=array[i++];
		}
		while(j<=high){
			temp[k++]=array[j++];
		}
		
		for(int r=0;r<temp.length;r++){
			array[low+r]=temp[r];
		}
	}
	public static void main(String[] args) {
		Integer[] array=new Integer[]{1,6,3,4,8,7,9};
		Test test=new Test();
		test.sort(array);
		for(int i=0;i<array.length;i++){
			System.out.print(array[i]+" ");
		}
	}
}
