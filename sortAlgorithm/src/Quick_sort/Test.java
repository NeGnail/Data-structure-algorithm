package Quick_sort;



public class Test<T> {
	public void sort(T[] array){
		sort(array,0,array.length-1);
	}
	private void sort(T[] array,int i,int j){
		if(i<j){
			T key=array[i];
			int low=i;
			int high=j;
			while(low<high){
				while(low<high && ((Comparable)key).compareTo(array[high])<0){
					high--;
				}
				array[low]=array[high];
				while(low<high && ((Comparable)key).compareTo(array[low])>0){
					low++;
				}
				array[high]=array[low];
			}
			array[low]=key;
			sort(array,i,low-1);
			sort(array,low+1,j);
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
