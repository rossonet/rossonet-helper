FROM kasmweb/core-ubuntu-focal:1.14.0
USER root

ENV HOME /home/kasm-default-profile
ENV STARTUPDIR /dockerstartup
ENV INST_SCRIPTS $STARTUPDIR/install
WORKDIR $HOME

######### Customize Container Here ###########

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y apt-transport-https
RUN echo "deb https://download.sublimetext.com/ apt/stable/" |  tee /etc/apt/sources.list.d/sublime-text.list \
    && wget -qO - https://download.sublimetext.com/sublimehq-pub.gpg | apt-key add - \
    && apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y sublime-text \
    && cp /usr/share/applications/sublime_text.desktop $HOME/Desktop/ \
    && chmod +x $HOME/Desktop/sublime_text.desktop \
    && chown 1000:1000 $HOME/Desktop/sublime_text.desktop
RUN apt-get update \
    && apt-get install ca-certificates curl gnupg -y \
    && curl -sSL https://pkgs.netbird.io/debian/public.key | gpg --dearmor --output /usr/share/keyrings/netbird-archive-keyring.gpg \
    && echo 'deb [signed-by=/usr/share/keyrings/netbird-archive-keyring.gpg] https://pkgs.netbird.io/debian stable main' | tee /etc/apt/sources.list.d/netbird.list \
    && apt-get update && apt-get install netbird -y

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y openssh-client nmap tcpdump wireshark iproute2 iputils-arping iputils-ping traceroute firefox \
    apt-utils libcanberra-gtk3-module curl wget git vim openjdk-11-jdk openjdk-17-jdk filezilla putty evince thunar-archive-plugin netcat \
    iperf iperf3 corkscrew ostinato iptables
RUN apt-get clean && rm -rf /tmp/* && rm -rf /var/lib/apt/lists/*

WORKDIR /opt

RUN wget -c download.springsource.com/release/STS4/4.1.2.RELEASE/dist/e4.10/spring-tool-suite-4-4.1.2.RELEASE-e4.10.0-linux.gtk.x86_64.tar.gz && \
     sudo tar xfz /opt/spring-tool-suite-4-4.1.2.RELEASE-e4.10.0-linux.gtk.x86_64.tar.gz  && \
     sudo rm spring-tool-suite-4-4.1.2.RELEASE-e4.10.0-linux.gtk.x86_64.tar.gz

WORKDIR $HOME

COPY ./STS.desktop $HOME/Desktop/STS.desktop
RUN chmod +x $HOME/Desktop/STS.desktop
COPY ./wireshark.desktop $HOME/Desktop/wireshark.desktop
RUN chmod +x $HOME/Desktop/wireshark.desktop
COPY ./filezilla.desktop $HOME/Desktop/filezilla.desktop
RUN chmod +x $HOME/Desktop/filezilla.desktop
COPY ./putty.desktop $HOME/Desktop/putty.desktop
RUN chmod +x $HOME/Desktop/putty.desktop
COPY ./exo-terminal-emulator.desktop $HOME/Desktop/exo-terminal-emulator.desktop
RUN chmod +x $HOME/Desktop/exo-terminal-emulator.desktop
COPY ./vnc_startup.sh $STARTUPDIR/vnc_startup.sh
RUN chmod +x $STARTUPDIR/vnc_startup.sh
COPY ./kasmvnc.yaml $HOME/.vnc/kasmvnc.yaml
COPY ./custom_startup.sh $STARTUPDIR/custom_startup.sh
RUN chmod +x $STARTUPDIR/custom_startup.sh

######### End Customizations ###########

RUN chown 1000:0 $HOME
RUN $STARTUPDIR/set_user_permission.sh $HOME

ENV HOME /home/kasm-user
WORKDIR $HOME
RUN mkdir -p $HOME && chown -R 1000:0 $HOME
RUN usermod -a -G netdev kasm-user
RUN usermod -a -G tcpdump kasm-user
RUN usermod -a -G sudo kasm-user
RUN usermod -a -G video kasm-user
RUN groupadd wireshark
RUN usermod -a -G wireshark kasm-user
RUN chgrp wireshark /usr/bin/dumpcap
RUN chmod o-rx /usr/bin/dumpcap
RUN setcap 'CAP_NET_RAW+eip CAP_NET_ADMIN+eip' /usr/bin/dumpcap
RUN getcap /usr/bin/dumpcap
RUN echo "kasm-user ALL=(ALL)	NOPASSWD: ALL" >> /etc/sudoers
COPY ./bg_default.png /usr/share/extra/backgrounds/bg_default.png
#RUN usermod -l rossonet kasm-user

USER 1000
