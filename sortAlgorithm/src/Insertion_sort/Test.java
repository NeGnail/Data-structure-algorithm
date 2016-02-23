package Insertion_sort;

public class Test {
	public void sort(int[] in){
		int temp;
		for(int i=1;i<in.length;i++){
			for(int j=0;j<i;j++){
				if(in[j]<in[i]){
					temp=in[j];
					in[j]=in[i];
					in[i]=temp;
				}
			}
		}
		
	
	}
	
	public static void main(String[] args) {
		int[] a={1,64,15,5,7,32,54,26,4};
		Test test=new Test();
		test.sort(a);
		for(int i=0;i<a.length;i++){
			System.out.print(a[i]+" ");
		}
	}
}
