package com.test.hdfc;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class TestHdfc {
	public static void main(String[]args) throws IOException{
		Configuration conf=new Configuration();
		conf.set("fs.defaultFS","hdfs://mini4:9000");
		FileSystem fs=FileSystem.get(conf);
		FSDataOutputStream fos=fs.create(new Path("/hadoop-2.4.1.tar.gz"));
		FileInputStream fileInputStream=new FileInputStream("C:/hadoop-2.4.1.tar.gz");
		IOUtils.copy(fileInputStream,fos);
	}
}
