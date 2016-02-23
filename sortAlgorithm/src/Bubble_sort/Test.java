package Bubble_sort;



public class Test<T> {
	public void sort(T[] array){
		T temp;
		for(int i=1;i<array.length;i++){
			for(int j=0;j<i;j++){
				if( ((Comparable)array[i]).compareTo(array[j])>0 ){
					temp=array[i];
					array[i]=array[j];
					array[j]=temp;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Integer[] a={1,64,15,5,7,32,54,26,4};
		Test<Integer> test=new Test();
		test.sort(a);
		for(int i=0;i<a.length;i++){
			System.out.print(a[i]+" ");
		}
	}
}
