#sh indexer_all.sh 1:(app version) 2:(build number)
#sh indexer_all.sh 7.0.1 7011095

sh indexer.sh att $1 $2
sh indexer.sh sprint $1 $2
sh indexer.sh tmobileuk $1 $2
sh indexer.sh vivo $1 $2
