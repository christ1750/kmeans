import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *author: christ
 *data: 2015��9��22������4:38:34
 *function:
 */

public class KMeans {
	private int K;
	/**�洢���ݼ� */
	private List<double[]>dataSet;
	/**�洢���Ľڵ������ */
	private List<double[]>seedSet;
	/**ÿ�����Ӧ�����ݵ�����*/
	private List<Integer>[]subSet;
	/**�Ƿ����Ľڵ㷢���ı�ı��*/
	private boolean flag;
	public KMeans(int k){
		this.K = k;
		seedSet = new ArrayList<double[]>();
		subSet = new ArrayList[K];
		for(int i = 0; i < K; i++){
			subSet[i] = new ArrayList<Integer>();
		}
		flag = true;
	}
	public void setDataSet(List<double[]>dataSet){
		this.dataSet = dataSet;
	}
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * �������K����ʼ���ĵ�
	 */
	public void RandomSeed(){
		Random random = new Random();
		int length = dataSet.size();
		int []temp_random = new int[K]; //����K��������������Ľڵ��� 
		temp_random[0] = random.nextInt(length);
		seedSet.add(dataSet.get(temp_random[0]));
		for(int i = 1; i < K ; i++){
			int temp = random.nextInt(length);
			/*
			 * ��ʱ���ɵ�������������֮ǰ��temp_random����������
			 * ÿ���������������temp����Ҫ��temp_random�е�ÿһ��Ԫ�����±Ƚ�
			 */
			boolean ll = true;
			while(ll){
				ll = false;
				for(int j = 0; j < i ; j++){
					if(temp == temp_random[j]){
						temp = random.nextInt(length);
						ll = true;
						break;
					}
				}
			}
			temp_random[i] = temp;
			seedSet.add(dataSet.get(temp));
		}
	}
	/**
	 * �������е������ĵ�ľ��룬����Զ��Ϊ���е���ࡣ���������ǩ
	 * 
	 * ����subSet���ϣ�subSet[i]��ʾ��i��������ݳ�Ա���ϣ������������ĵ������
	 */
	public void calDistence(){
		/*
		 * ����ÿ���㵽��ͬ���Ľڵ�ľ��룬�ҵ���Сֵ�������ǩ
		 */
		for(int i = 0; i < K; i++){
			subSet[i].clear();
		}
		for(int i = 0; i < dataSet.size(); i++){
			int temp_class = 0;     //�����ʱ�࣬���ѭ����������ֵΪi�������ݵ�������
			double min = Double.MAX_VALUE;
			for(int j = 0; j < seedSet.size(); j++){
				double temp_min = dist(dataSet.get(i), seedSet.get(j));
				if(temp_min < min){
					min = temp_min;
					temp_class = j;
				}
			}
			subSet[temp_class].add(i);
		}
	}
	/**
	 * ���¼������Ľڵ㣬�����Ľڵ㷢���仯���� flagΪtrue,����Ϊfalse
	 * 
	 * ����seedSet�������������Ľڵ�ļ���
	 */
	public void calCenter(){
		/*
		 * ���е����������ڳ��Ե�ĸ���
		 */
		flag = false;
		for(int i = 0; i < subSet.length; i++){   //subSet.length��������K
			int sub_num = subSet[i].size();  //������е�ĸ���
			double[]sub_data = dataSet.get(subSet[i].get(0));
			int di_num = sub_data.length;     //����ά��
			for(int j = 1; j < sub_num; j++){
				int data_num = subSet[i].get(j);    //ȡ����i���д�ŵĵ�j����������
				/*
				 * ��Ӧ�����ŵ�Ԫ�����
				 */
				for(int k = 0; k < di_num; k++){
					sub_data[k] += dataSet.get(data_num)[k];
				}
			}
			for(int len = 0; len < di_num; len++){
				sub_data[len] = sub_data[len]/sub_num;
				if(sub_data[len] != seedSet.get(i)[len])
					flag = true;
				seedSet.get(i)[len] =  sub_data[len];
			}
		}
	}
	/**
	 * ����������ľ���
	 * @param label_a ��һ���������
	 * @param label_b �ڶ����������
	 * @return ����֮��ľ���
	 */
	public double dist(double []label_a,double []label_b){
		int length = label_a.length;
		double sum = 0;
		for(int i = 0; i < length; i++){
			sum += (label_a[i] - label_b[i]) * (label_a[i] - label_b[i]);
		}
		return Math.sqrt(sum);
	}
	
	public void printCluster(){
		for(int i = 0; i < K; i++){
			System.out.println("��" + (i+1) + "����������Ԫ��:");
			Iterator<Integer>it = subSet[i].iterator();
			while(it.hasNext()){
				System.out.print(it.next() + "\t");
			}
			System.out.println();
		}
	}
	
	public void startCluster(){
		RandomSeed();
		while(flag){
			calDistence();
			calCenter();
		}
		printCluster();
	}
	
	public static void main(String[] args) {
		KMeans kmeans = new KMeans(4);
		 ArrayList<double[]> dataSet=new ArrayList<double[]>();  
         
	        dataSet.add(new double[]{1,2});  
	        dataSet.add(new double[]{3,3});  
	        dataSet.add(new double[]{3,4});  
	        dataSet.add(new double[]{5,6});  
	        dataSet.add(new double[]{8,9});  
	        dataSet.add(new double[]{4,5});  
	        dataSet.add(new double[]{6,4});  
	        dataSet.add(new double[]{3,9});  
	        dataSet.add(new double[]{5,9});  
	        dataSet.add(new double[]{4,2});  
	        dataSet.add(new double[]{1,9});  
	        dataSet.add(new double[]{7,8}); 
	    kmeans.setDataSet(dataSet);
	    kmeans.startCluster();
	    
	}
}

