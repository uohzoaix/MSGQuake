package com.zxq.rts.topology;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

import com.zxq.rts.rabbit.config.BaseMQConfig;
import com.zxq.rts.rabbit.config.CustomeMQConfig;
import com.zxq.rts.rabbit.connection.BaseConnection;
import com.zxq.rts.rabbit.connection.MQConnection;
import com.zxq.rts.rabbit.error.ServiceException;
import com.zxq.rts.schema.JsonSchema;
import com.zxq.rts.schema.StringSchema;
import com.zxq.rts.topology.bolt.RabbitmqBolt;
import com.zxq.rts.topology.spout.RabbitmqSpout;
import com.zxq.rts.utils.PropUtil;

public class MainTopology {

	public static void main(String[] args) throws ServiceException {
		TopologyBuilder builder = new TopologyBuilder();
		BaseMQConfig baseConfig = new BaseMQConfig(PropUtil.getPropVal("mqhost").toString(), 5672, PropUtil.getPropVal("userName").toString(), PropUtil.getPropVal("password").toString(), PropUtil
				.getPropVal("virtualHost").toString(), 10, PropUtil.getPropVal("queneName").toString());

		CustomeMQConfig customeMQConfig = new CustomeMQConfig(baseConfig, Integer.valueOf(PropUtil.getPropVal("prefetch").toString()), Boolean.valueOf(PropUtil.getPropVal("requeueOnFail").toString()));

		BaseConnection connection = new MQConnection(customeMQConfig);

		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("schema", new StringSchema());
		valueMap.put("config", customeMQConfig);
		// valueMap = BeanUtil.getFieldValueMap(valueMap, customeMQConfig);

		builder.setSpout("mqSpout", new RabbitmqSpout(), 2).addConfigurations(valueMap).setMaxSpoutPending(Integer.valueOf(PropUtil.getPropVal("prefetch").toString()));
		valueMap.put("schema", new JsonSchema());
		builder.setBolt("mqBolt", new RabbitmqBolt(), 4).addConfigurations(valueMap).shuffleGrouping("mqSpout");
		// builder.setBolt("statistic", new StatisticBolt(), 4).shuffleGrouping("mqSpout");
		// builder.setBolt("ipdup", new IpDupBolt(), 4).shuffleGrouping("mqSpout");

		Config conf = new Config();
		conf.setDebug(false);
		conf.put(Config.TOPOLOGY_DEBUG, false);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(2);
			// StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("mqTopology", conf, builder.createTopology());
		}
	}
}
