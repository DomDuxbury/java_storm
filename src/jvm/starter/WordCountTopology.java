package starter;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.topology.base.BaseWindowedBolt.Count;
import starter.spout.RandomSentenceSpout;
import starter.bolt.SplitSentenceBolt;
import starter.bolt.WordCountBolt;

public class WordCountTopology {

  private static TopologyBuilder defineTopology() {

    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("sentence-spout", new RandomSentenceSpout(), 1);

    builder.setBolt("split-sentence", new SplitSentenceBolt())
        .shuffleGrouping("sentence-spout");

    builder.setBolt("word-count-bolt", new WordCountBolt())
        .shuffleGrouping("split-sentence");

    return builder;
  }

  private static Config defineConfig() {
    Config conf = new Config();
    conf.setDebug(true);
    conf.setMaxTaskParallelism(3);
    return conf;
  }

  private static void startCluster(Config conf, TopologyBuilder builder) throws Exception{
    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology("word-count", conf, builder.createTopology());
    // Thread.sleep(10000);
    // cluster.shutdown();
  }

  public static void main(String[] args) throws Exception {
    TopologyBuilder builder = defineTopology();
    Config conf = defineConfig();
    startCluster(conf, builder);
  }
}
