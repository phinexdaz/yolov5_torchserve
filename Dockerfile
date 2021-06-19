FROM pytorch/torchserve:latest
USER root
RUN apt-get update && apt-get install -y libgl1-mesa-glx libglib2.0-0 python3-distutils
RUN pip3 install --upgrade pip
RUN pip install opencv-python>=4.1.2 PyYAML>=5.3.1 tensorboard>=2.4.1 seaborn>=0.11.0 pandas