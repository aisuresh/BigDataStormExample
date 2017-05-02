package com.suresh.bigdata.storm.topologies;

import com.suresh.bigdata.storm.bolts.WordCounterBolt;
import com.suresh.bigdata.storm.bolts.WordSpitterBolt;
import com.suresh.bigdata.storm.spouts.LineReaderSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class WordCountStormTopology {

	public static void main(String[] args) throws Exception{
		Config config = new Config();
		config.put("inputFile", args[0]);
		config.setDebug(true);
		config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("line-reader-spout", new LineReaderSpout());
		builder.setBolt("word-spitter", new WordSpitterBolt()).shuffleGrouping("line-reader-spout");
		builder.setBolt("word-counter", new WordCounterBolt()).shuffleGrouping("word-spitter");
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("WordCountStorm", config, builder.createTopology());
		Thread.sleep(10000);
		
		cluster.shutdown();
	}

}
