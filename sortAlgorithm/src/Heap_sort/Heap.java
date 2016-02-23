package Heap_sort;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 数组array位置0为空
 * @author lw
 *
 * @param <T>
 */
public class Heap<T>{
	private T[] array; 
	public Heap(){
	   
	}
	
	public T[] sort(T[] array){
		 T[] newArray=(T[]) Array.newInstance(array[0].getClass(), array.length+1);
		 for(int i=1;i<newArray.length;i++){
		 	newArray[i]=array[i-1];
		 }
		 this.array=newArray;
		 for(int i=(this.array.length-1)/2;i>=1;i--){
			sink(i,this.array.length);
		 }
		 
	
		 
		 for(int i=this.array.length-1;i>1;i--){
			 exchange(1,i);
			 sink(1,i);
		 }
		 return this.array;
	}
	/**
	 * 下沉
	 * @param a
	 */
	private void sink(int a,int length){
		int child;
		T temp;
		for(;2*a<length;a=child){
			child=2*a;
			if(child+1<length){
				if( ((Comparable)array[child]).compareTo(array[child+1])<0 ){
					child++;
				}				
			}
			if( ((Comparable)array[child]).compareTo(array[a])>0 ){
				exchange(a,child);
			} 
		}
		
	}
	
	/**
	 * 交换
	 * @param i
	 * @param j
	 */
	private void exchange(int i,int j){
		T temp=array[i];
		array[i]=array[j];
		array[j]=temp;
	}
	
	
	public static void main(String[] args) {
		Integer[] array=new Integer[]{1,6,3,4,8,7,9};
		Heap<Integer> heap=new Heap<Integer>();
		Integer[] newArray=heap.sort(array);
		for(int i=1;i<newArray.length;i++){
			System.out.print(newArray[i]+" ");
		}
	}
}
