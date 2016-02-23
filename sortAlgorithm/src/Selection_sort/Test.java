package Selection_sort;



public class Test {
	public void sort(int[] in){
		int temp;
		int position=0;
		for(int i=0;i<in.length-1;i++){
			position=i;				
			for(int j=i;j<in.length;j++){
				if(in[position]<in[j]){
					position=j;
				}
			}
			temp=in[i];
			in[i]=in[position];
			in[position]=temp;
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
