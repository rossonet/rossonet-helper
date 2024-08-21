FROM quay.io/jupyter/base-notebook:ubuntu-22.04 AS initial
USER root
RUN apt update 
RUN DEBIAN_FRONTEND=noninteractive apt install apt-transport-https ca-certificates curl gnupg lsb-release curl wget git vim -y
RUN DEBIAN_FRONTEND=noninteractive apt-get upgrade -y && apt-get clean && rm -rf /var/lib/apt/lists/*
USER jovyan
RUN conda install -y -c conda-forge grpcio grpcio-tools
USER root
RUN mkdir /home/jovyan/examples
COPY examples /home/jovyan/examples/rossonet
RUN cd /tmp && git clone https://github.com/rossonet/langgraph.git && cd langgraph && mv examples /home/jovyan/examples/langgraph && cd && rm -rf /tmp/langgraph
RUN cd /tmp && git clone https://github.com/rossonet/langchain.git && cd langchain && mv cookbook /home/jovyan/examples/langchain && cd && rm -rf /tmp/langchain
RUN cd /tmp && git clone https://github.com/rossonet/llama-recipes.git && cd llama-recipes && mv recipes /home/jovyan/examples/llama && cd && rm -rf /tmp/llama-recipes
RUN cd /tmp && git clone https://github.com/rossonet/cookbook.git && mv cookbook /home/jovyan/examples/mistral
RUN chown -R jovyan /home/jovyan && chmod -R g+rw /home/jovyan

FROM quay.io/jupyter/base-notebook:ubuntu-22.04
USER jovyan
ENV JUPYTER_ENABLE_LAB=yes
COPY --from=initial / /
