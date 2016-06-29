import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *author: christ
 *data: 2015年9月22日下午4:38:34
 *function:
 */

public class KMeans {
	private int K;
	/**存储数据集 */
	private List<double[]>dataSet;
	/**存储中心节点的坐标 */
	private List<double[]>seedSet;
	/**每个类对应的数据的索引*/
	private List<Integer>[]subSet;
	/**是否中心节点发生改变的标记*/
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
	 * 随机生成K个初始中心点
	 */
	public void RandomSeed(){
		Random random = new Random();
		int length = dataSet.size();
		int []temp_random = new int[K]; //保存K个随机数，即中心节点数 
		temp_random[0] = random.nextInt(length);
		seedSet.add(dataSet.get(temp_random[0]));
		for(int i = 1; i < K ; i++){
			int temp = random.nextInt(length);
			/*
			 * 临时生成的随机数如果等于之前的temp_random则重新生成
			 * 每次重新生成随机数temp后都需要和temp_random中的每一个元素重新比较
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
	 * 计算所有点与中心点的距离，根据远近为所有点聚类。并赋予类标签
	 * 
	 * 生成subSet集合，subSet[i]表示第i个类的数据成员集合，用来计算中心点的坐标
	 */
	public void calDistence(){
		/*
		 * 计算每个点到不同中心节点的距离，找到最小值贴上类标签
		 */
		for(int i = 0; i < K; i++){
			subSet[i].clear();
		}
		for(int i = 0; i < dataSet.size(); i++){
			int temp_class = 0;     //存放临时类，最后循环结束，其值为i这条数据的真正类
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
	 * 重新计算中心节点，若中心节点发生变化设置 flag为true,否则为false
	 * 
	 * 生成seedSet保存各个类的中心节点的集合
	 */
	public void calCenter(){
		/*
		 * 所有点的坐标相加在除以点的个数
		 */
		flag = false;
		for(int i = 0; i < subSet.length; i++){   //subSet.length即类别个数K
			int sub_num = subSet[i].size();  //该类别中点的个数
			double[]sub_data = dataSet.get(subSet[i].get(0));
			int di_num = sub_data.length;     //坐标维度
			for(int j = 1; j < sub_num; j++){
				int data_num = subSet[i].get(j);    //取出第i类中存放的第j个数据索引
				/*
				 * 对应数组存放的元素相加
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
	 * 计算两个点的距离
	 * @param label_a 第一个点的坐标
	 * @param label_b 第二个点的坐标
	 * @return 两点之间的距离
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
			System.out.println("第" + (i+1) + "个类别包含的元素:");
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

