package com.test.thread.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 生产者与消费者队列实现
 * @author fusj
 */
public class ProducerAndConsumer {
	
	public static void main(String[] args) {
		BlockingQueue<Product> queue = new LinkedBlockingQueue<>(100);
		// 生产者
		Producer producer1 = new Producer("生产者A", queue);
		Producer producer2 = new Producer("生产者B", queue);
		// 消费者
		Consumer consumer1 = new Consumer("消费者1", queue);
		Consumer consumer2 = new Consumer("消费者2", queue);
		Consumer consumer3 = new Consumer("消费者3", queue);
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

	private BlockingQueue<Product> queue;

	public Producer (String name, BlockingQueue<Product> queue) {
		super.setName(name);
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			Product product = produce();
			putProduct(product);
		}
	}

	public Product produce() {
		Random rnd = new Random();
		try {
			Thread.sleep(rnd.nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Product product = new Product();
		product.setName(product_names[rnd.nextInt(5)]);
		System.out.println(Thread.currentThread().getName() + ":生产出了：" + product.getName());
		return product;
	}
	
	public void putProduct(Product product) {
		try {
			queue.put(product);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/**
 * 消费者
 * @author fusj
 */
class Consumer extends Thread {

	private BlockingQueue<Product> queue;

	public Consumer (String name, BlockingQueue<Product> queue) {
		super.setName(name);
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			Product product = pollProduct();
			eating(product);
		}
	}

	public void eating(Product product) {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + ":吃了" +product.getName());
	}
	
	public Product pollProduct() {
		Product product = null;
		try {
			product = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return product;
	}
}
