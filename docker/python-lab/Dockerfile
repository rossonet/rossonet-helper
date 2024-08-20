FROM jupyter/base-notebook:ubuntu-22.04 as initial
USER root
RUN apt update 
RUN DEBIAN_FRONTEND=noninteractive apt install apt-transport-https ca-certificates curl gnupg lsb-release curl wget git vim openjdk-11-jdk iperf iperf3 ostinato -y
RUN DEBIAN_FRONTEND=noninteractive apt-get upgrade -y && apt-get clean && rm -rf /var/lib/apt/lists/*
USER jovyan
RUN conda install -y -c pytorch pytorch cpuonly
RUN conda install -y -c conda-forge torchinfo
RUN conda install -y -c plotly plotly
RUN conda install -y -c conda-forge grpcio grpcio-tools
RUN conda install -y -c conda-forge matplotlib
RUN conda install -y -c conda-forge seaborn
RUN conda install -y -c conda-forge scapy
RUN conda install -y -c conda-forge bokeh
RUN conda install -y -c anaconda paramiko
RUN conda install -y -c conda-forge pyqt
RUN conda install -y -c conda-forge qtconsole
#RUN conda install -y -c conda-forge streamlit
#RUN conda install -y -c anaconda nltk
RUN conda install -y -c conda-forge diffusers
RUN conda install -y -c huggingface transformers
RUN conda install -y -c conda-forge gradio
RUN conda install -y -c conda-forge openai
RUN conda install -y -c conda-forge ibm-watson
RUN conda install -y -c conda-forge librosa
RUN pip install git+https://github.com/openai/whisper.git
RUN conda update ffmpeg
USER root
RUN chown -R jovyan /home/jovyan && chmod -R g+rw /home/jovyan

FROM jupyter/base-notebook:ubuntu-22.04
USER jovyan
ENV JUPYTER_ENABLE_LAB=yes
COPY --from=initial / /
