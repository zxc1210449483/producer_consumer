package com.test.thread.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 生产者与消费者简单实现
 * @author fusj
 */
public class ProducerAndConsumer {
	
	public static void main(String[] args) {
		List<Product> pList = new ArrayList<>();
		// 生产者
		Producer producer1 = new Producer("生产者A", pList);
		Producer producer2 = new Producer("生产者B", pList);
		// 消费者
		Consumer consumer1 = new Consumer("消费者1", pList);
		Consumer consumer2 = new Consumer("消费者2", pList);
		Consumer consumer3 = new Consumer("消费者3", pList);
		// 开启线程
		producer1.start();
		producer2.start();
		consumer1.start();
		consumer2.start();
		consumer3.start();
	}
}

/**
 * 产品类
 * @author fusj
 */
class Product {

	private String name;

	private double price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}

/**
 * 生产者
 * @author fusj
 */
class Producer extends Thread {

	public static final String[] product_names = {"苹果", "橘子", "香蕉", "葡萄", "火龙果"};

	private List<Product> product_list;

	public Producer (String name, List<Product> product_list) {
		super.setName(name);
		this.product_list = product_list;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (product_list) {
				if (product_list.size() >= 100) {
					System.out.println(Thread.currentThread().getName() + ":仓库已满，等待消费");
					try {
						product_list.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					Product product = new Product();
					product.setName(product_names[new Random().nextInt(5)]);
					product_list.add(product);
					System.out.println(Thread.currentThread().getName() + ":生产出了：" + product.getName());
					product_list.notifyAll();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

/**
 * 消费者
 * @author fusj
 */
class Consumer extends Thread {

	private List<Product> product_list;

	public Consumer (String name, List<Product> product_list) {
		super.setName(name);
		this.product_list = product_list;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (product_list) {
				if (this.product_list.size() == 0) {
					System.out.println(Thread.currentThread().getName() + ":没有产品，请等待生产者生产");
					try {
						product_list.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println(Thread.currentThread().getName() + ":吃了" + this.product_list.get(0).getName());
					this.product_list.remove(0);
					product_list.notifyAll();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
