#Provision a New Machine on EC2

#Add hostname using hostname command and also in /etc/sysconfig/network
#Restart Syslog

# Both the above activities are done by a startup script in /tmp/ec2/ec2-hostname

#Add DNS Entry using EC

HOSTNAME=`hostname`


#For CDH4
#rpm -ivh http://archive.cloudera.com/cdh4/one-click-install/redhat/5/x86_64/cloudera-cdh-4-0.x86_64.rpm
#sudo rpm -ivh cloudera-cdh-4-0.x86_64.rpm

# For CDH3
yum install -y hadoop-0.20-conf-pseudo


mkdir -p /var/lib/hadoop-hdfs/cache/hdfs/dfs/data
mkdir -p /var/lib/hadoop-hdfs/cache/hdfs/dfs/namesecondary
mkdir -p /var/lib/hadoop-hdfs/cache/hdfs/dfs/name
chown -R hdfs.hdfs /var/lib/hadoop-hdfs/

#For CDH4
#sudo /etc/init.d/hadoop-hdfs-namenode start
#sudo /etc/init.d/hadoop-hdfs-datanode start
#sudo /etc/init.d/hadoop-hdfs-secondarynamenode start
#sudo /etc/init.d/hadoop-0.20-mapreduce-jobtracker start
#sudo /etc/init.d/hadoop-0.20-mapreduce-tasktracker start


sed -i "s/localhost/$HOSTNAME/g" /etc/hadoop-0.20.conf/core-site.xml

sed -i "s/localhost/$HOSTNAME/g" /etc/hadoop-0.20/conf/mapred-site.xml

#Deploy Logshed WAR FILE




sudo perl -pi.bak -e "s/ec2-logshedtemplate-01/ec2d-logshed-02.dyn.mypna.com/g" core-site.xml
sudo perl -pi.bak -e "s/ec2-logshedtemplate-01/ec2d-logshed-02.dyn.mypna.com/g" logshedconfig.properties
sudo perl -pi.bak -e "s/logshed.default.muxemux.root.output.dir=\/usr\/local\/apache-tomcat-6.0.35\/logs/logshed.default.muxemux.root.output.dir=\/usr\/local\/apache-tomcat-7.0.32\/logs/g" logshedconfig.properties
sudo perl -pi.bak -e "s/ec2-logshedtemplate-01/ec2d-logshed-02.dyn.mypna.com/g" hazelcast.xml
sudo perl -pi.bak -e "s/ec2-logshedtemplate-01/ec2d-logshed-02.dyn.mypna.com/g" mapred-site.xml




mkdir -p /usr/local/logs
touch /usr/local/logs/logshed.log
chown -R hdfs.hdfs /usr/local/logs/

logshed.default.muxemux.root.output.dir=/usr/local/apache-tomcat-6.0.35/logs


#CDH3 - Start Services
sudo /etc/init.d/hadoop-0.20-namenode start
sudo /etc/init.d/hadoop-0.20-datanode start
sudo /etc/init.d/hadoop-0.20-secondarynamenode start
sudo /etc/init.d/hadoop-0.20-jobtracker start
sudo /etc/init.d/hadoop-0.20-tasktracker start


#CDH4 - Start Services
#sudo /etc/init.d/hadoop-0.20-mapreduce-tasktracker stop
#sudo /etc/init.d/hadoop-0.20-mapreduce-jobtracker stop
#sudo /etc/init.d/hadoop-hdfs-secondarynamenode stop
#sudo /etc/init.d/hadoop-hdfs-datanode stop
#sudo /etc/init.d/hadoop-hdfs-namenode stop


#CDH3 - Stop Services
#sudo /etc/init.d/hadoop-0.20-tasktracker stop
#sudo /etc/init.d/hadoop-0.20-jobtracker stop
#sudo /etc/init.d/hadoop-0.20-secondarynamenode stop
#sudo /etc/init.d/hadoop-0.20-datanode  stop
#sudo /etc/init.d/hadoop-0.20-namenode stop

mkdir -p /usr/local/logs
touch /usr/local/logs/logshed.log
chown -R hdfs.hdfs /usr/local/logs/



