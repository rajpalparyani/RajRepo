wget http://packages.sw.be/rpmforge-release/rpmforge-release-0.5.2-2.el5.rf.x86_64.rpm
sudo rpm --import http://apt.sw.be/RPM-GPG-KEY.dag.txt
sudo rpm -K rpmforge-release-0.5.2-2.el5.rf.x86_64.rpm
sudo rpm -i rpmforge-release-0.5.2-2.el5.rf.x86_64.rpm
sudo yum -y install git-gui
