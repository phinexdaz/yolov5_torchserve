# yolov5_torchserve

## start torchserve
docker run --rm -it -p 3000:7070 -v $(pwd)/model-store:/home/model-server/model-store yolov5/torchserve:latest torchserve --start --model-store model-store --models my_model=my_model.mar
