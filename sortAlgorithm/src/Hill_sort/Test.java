package Hill_sort;

public class Test {
	public void sort(int[] in){
		int temp;
		int d=in.length/2;
		while(d>=1){
			for(int i=0;i<d;i++){//1,64,15,5,7
				for(int j=i+d;j<in.length;j+=d){//
					for(int k=i;k<j;k+=d){
						if(in[k]<in[j]){
							temp=in[k];
							in[k]=in[j];
							in[j]=temp;
						}
					}
				}
			}
			d=d/2;
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
